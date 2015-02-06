package hr.ketel.ProtIO;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

/**
 * Created by Rutger on 06/01/14.
 */
public class CommReader extends Communicator
{
    private enum State_e
    {
        S_IDLE,
        S_IDLE_ESCAPE,
        S_HEADER,
        S_MESSAGE,
        S_MESSAGE_ESCAPE,
        S_CRC
    }

    State_e _state;
    Header_t _header;
    CommReaderCallback _fn_callback;

    public CommReader()
    {
        super();
        _state = State_e.S_IDLE;
        _header = null;
        _fn_callback = null;
    }

    public CommReader(CommReaderCallback fn_callback)
    {
        super();
        _state = State_e.S_IDLE;
        _header = null;
        _fn_callback = fn_callback;
    }

    public void append_to_buffer(byte data)
    {
        // TODO: maybe check buffer space and realloc ...

        _buffer[_ptr++] = data;

        System.out.printf("0x%02x(%d), ", data, (data & 0xff));
    }

    public void read(byte data)
    {
        switch (_state)
        {
            case S_IDLE:
                if (data == STX) { _state = State_e.S_HEADER; reset(); System.out.printf("\nSTX, "); }
                if (data == DLE) { _state = State_e.S_IDLE_ESCAPE; }
                break;

            case S_IDLE_ESCAPE:
                _state = State_e.S_IDLE;
                break;

            case S_HEADER:
                System.out.printf("header, ");
                this.crc(data);

                if (_header == null)
                {
                    byte n = (byte) (data & 1);
                    byte s = (byte) ((data >> 1) & 1);
                    byte w = (byte) (data >> 2);
                    this._header = new Header_t(n,s, w);

                    _state = (this._header.h == 1) ? State_e.S_HEADER : State_e.S_MESSAGE;
                } else {
                    _state = ((data & 1) == 1) ? State_e.S_HEADER : State_e.S_MESSAGE;
                }

                break;

            case S_MESSAGE:
                this.crc(data);

                switch (data)
                {
                    case DLE:
                        System.out.printf("DLE, ");
                        _state = State_e.S_MESSAGE_ESCAPE;
                        break;
                    case ETX:
                        System.out.printf("ETX, ");
                        _state = State_e.S_CRC;
                        break;
                    default:
                        this.append_to_buffer(data);
                        break;
                }
                break;

            case S_MESSAGE_ESCAPE:
                this.crc(data);
                this.append_to_buffer(data);
                _state = State_e.S_MESSAGE;
                break;

            case S_CRC:
                System.out.printf("CRC (0x%02x, 0x%02x)\n", _crc, (data & 0xff));
                if (_crc == (data & 0xff))
                {
                    if (_fn_callback != null)
                    {
                        _fn_callback.notify(_header, Arrays.copyOfRange(_buffer, 0, _ptr));
                    }
                }

                _state = State_e.S_IDLE;
                break;

            default:
                _state = State_e.S_IDLE;
                break;
        }
    }

    static public char parseChar(ByteArrayInputStream bais)
    {
        return (char) bais.read();
    }

    static public String parseString(ByteArrayInputStream bais)
    {
        StringBuffer sb = new StringBuffer();
        char c;
        while ( (c = (char) bais.read()) != '\0')
        {
            sb.append(c);
        }
        return sb.toString();
    }

    static public int parseInt(ByteArrayInputStream bais)
    {
        int result = 0;
        for (int i = 0; i < TXRX_INT_SIZE; i++)
        {
            result |= (bais.read() << (8*i));
        }
        return result;
    }

    static public float parseFloat(ByteArrayInputStream bais)
    {
        return Float.parseFloat(CommReader.parseString(bais));
    }
}
