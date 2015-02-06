package com.minor.HRketel;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.minor.HRketel.arbitrators.Agent;
import com.minor.HRketel.behaviours.AdjustVelocity;
import com.minor.HRketel.behaviours.ControlManually;
import com.minor.HRketel.behaviours.MapCourse;
import com.minor.HRketel.behaviours.Move;
import com.minor.HRketel.interfaces.Behaviour;
import com.minor.HRketel.modules.AgentActuatorManager;
import com.minor.HRketel.modules.AgentLocationManager;
import com.minor.HRketel.modules.AgentSensorManager;
import com.minor.HRketel.modules.Motor;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends IOIOActivity {
    /**
     * Called when the activity is first created.
     */
    private ToggleButton tBtnBoardLed;
    private TextView textView;

    private Agent agent;
    private Thread thread;
    private Runnable runnable;

    private Handler handler;
    private Motor motor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        textView = (TextView) findViewById(R.id.test_tv);
        tBtnBoardLed = (ToggleButton) findViewById(R.id.tBtn_boardled);


        handler = new Handler();

        AgentSensorManager agentSensorManager = new AgentSensorManager(this);
        AgentActuatorManager agentActuatorManager = new AgentActuatorManager();
        AgentLocationManager agentLocationManager = new AgentLocationManager();

        agent = new Agent(agentSensorManager, agentActuatorManager, agentLocationManager);

        motor = new Motor();

        Behaviour move = new Move();
        Behaviour adjustVelocity = new AdjustVelocity();
        Behaviour mapCourse = new MapCourse();
        Behaviour controlManually = new ControlManually();

        agent.addBehaviours(move);
        agent.addBehaviours(adjustVelocity);
        agent.addBehaviours(mapCourse);
        agent.addBehaviours(controlManually);
        agent.getAgentSensorManager().initialize();
    }

    public void startAgent(View view)
    {
        agent.setIsOn(true);
        agent.getAgentSensorManager().activateSensors();
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("Boat: ", "Agent is on");
                while(agent.isOn())
                {
                    agent.run();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(agent.status());
                            //progress.setProgress(1);
                        }
                    });
                }
                Log.d("Boat: ","Agent is off");
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }

    public void stopAgent(View view)
    {
        agent.setIsOn(false);
        agent.getAgentSensorManager().deactivateSensors();
    }

    class Looper extends BaseIOIOLooper
    {
        private DigitalOutput _boardLed;

        private Uart _uart;
        private InputStream _uart_in;
        private OutputStream _uart_out;

        private final int PIN_RX = 4;
        private final int PIN_TX = 3;


        @Override
        protected void setup() throws ConnectionLostException
        {
            _boardLed = ioio_.openDigitalOutput(0, true);

            _uart = ioio_.openUart(PIN_RX, PIN_TX, 9600, Uart.Parity.NONE, Uart.StopBits.ONE);
            _uart_in = _uart.getInputStream();
            _uart_out = _uart.getOutputStream();

            synchronized (_uart_out) {
                agent.getAgentActuatorManager().getMotor().setOstream(_uart_out);
            }


        }

        @Override
        public void loop() throws ConnectionLostException
        {
            _boardLed.write(!tBtnBoardLed.isChecked());

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }


    }

    @Override
    protected IOIOLooper createIOIOLooper() {
        return new Looper();
    }

}
