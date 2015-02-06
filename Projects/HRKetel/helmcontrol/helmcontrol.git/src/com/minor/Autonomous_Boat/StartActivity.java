package com.minor.Autonomous_Boat;

import android.app.Activity;
import android.app.Dialog;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class StartActivity extends Activity implements ConnectedListener {
    private static final int SERVERPORT = 9999;
    private static String SERVER_IP = "127.0.0.1";
    int leftDir = 0;
    int rightDir = 0;
    int lspeed = 0;
    int rspeed = 0;
    private Socket socket;
    private WifiManager wifi;
    private DhcpInfo info;
    private String positions;
    private Dialog loading;
    private Thread communicationThread;
    private NumberPicker speedLeft;
    private NumberPicker speedRight;
    private Switch remoteControl;
    private LinearLayout controls;
    private Button leftForward, rightForward, leftReverse, rightReverse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        speedLeft = (NumberPicker) findViewById(R.id.speedLeft);
        speedRight = (NumberPicker) findViewById(R.id.speedRight);

        speedLeft.setMinValue(0);
        speedLeft.setMaxValue(255);

        speedRight.setMinValue(0);
        speedRight.setMaxValue(255);

        loading = new Dialog(this);
        loading.setTitle("Waiting for connection");
        loading.setContentView(R.layout.dialog);
        loading.show();

        Handler handler = new Handler();

        communicationThread = new Thread(new ClientThread(this, handler));
        wifi = (WifiManager) getSystemService(this.WIFI_SERVICE);
        info = wifi.getDhcpInfo();
        String gateway = Formatter.formatIpAddress(info.gateway);
        this.SERVER_IP = gateway;

        communicationThread.start();

        controls = (LinearLayout) findViewById(R.id.controls);
        controls.setVisibility(LinearLayout.GONE);

        remoteControl = (Switch) findViewById(R.id.remoteControl);
        remoteControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (socket != null) {
                    try {
                        PrintWriter out = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())),
                                true);
                        out.println(String.format("{\"action\":\"command\",\"data\":\"%d\"}", isChecked ? 1 : 0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                controls.setVisibility(isChecked ? LinearLayout.VISIBLE : LinearLayout.GONE);
            }
        });

        leftForward = (Button) findViewById(R.id.forwardLeft);
        leftForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        leftDir = 1;
                        leftForward.setPressed(true);
                        lspeed = speedLeft.getValue();
                        sendSpeed();
                        break;
                    case MotionEvent.ACTION_UP:
                        lspeed = 0;
                        leftForward.setPressed(false);
                        sendSpeed();
                        break;
                }

                return true;
            }
        });
        leftReverse = (Button) findViewById(R.id.reverseLeft);
        leftReverse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        leftDir = 0;
                        leftReverse.setPressed(true);
                        lspeed = speedLeft.getValue();
                        sendSpeed();
                        break;
                    case MotionEvent.ACTION_UP:
                        lspeed = 0;
                        leftReverse.setPressed(false);
                        sendSpeed();
                        break;
                }

                return true;
            }
        });

        rightForward = (Button) findViewById(R.id.forwardRight);
        rightForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        rightForward.setPressed(true);
                        rspeed = speedRight.getValue();
                        rightDir = 1;
                        sendSpeed();
                        return true;
                    case MotionEvent.ACTION_UP:
                        rightForward.setPressed(false);
                        rspeed = 0;
                        sendSpeed();
                        return true;
                }

                return false;
            }
        });
        rightReverse = (Button) findViewById(R.id.reverseRight);
        rightReverse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        rspeed = speedRight.getValue();
                        rightReverse.setPressed(true);
                        rightDir = 0;
                        sendSpeed();
                        return true;
                    case MotionEvent.ACTION_UP:
                        rspeed = 0;
                        rightReverse.setPressed(false);
                        sendSpeed();
                        return true;
                }

                return false;
            }
        });
    }

    private void sendSpeed() {
        if (socket == null) {
            Log.d("HRBOOT", "Socket is null");
        }

        if (socket != null) {
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);

                out.println(String.format("{\"action\":\"command\",\"data\":\"%d;%d;%d;%d\"}", leftDir, rightDir, lspeed, rspeed));

            } catch (IOException e) {
                Log.e("HRBOOT", e.getStackTrace().toString());
            }
        }
    }

    public void startAI() {
        ArrayList<LatLng> coords = (ArrayList<LatLng>) getIntent().getExtras().get("coords");
        if (coords.size() > 0) {
            positions = "{\"action\":\"coords\",\"data\":\"";
            for (LatLng coord : coords) {
                positions += Double.toString(coord.latitude) + "," + Double.toString(coord.longitude) + ";";
            }
            positions += "\"}";

            if (socket != null) {
                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),
                            true);
                    out.println(positions);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void OnConnected() {
        if (loading != null) {
            loading.hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_control, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.startAI:
                this.startAI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ClientThread implements Runnable {
        ConnectedListener listener;
        Handler handler;

        public ClientThread(ConnectedListener listener, Handler handler) {
            this.listener = listener;
            this.handler = handler;
        }

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVERPORT);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.OnConnected();
                    }
                });
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
