package hr.ketel.ProtIO;

import android.util.Log;

/**
 * Created by Rutger on 07/01/14.
 */
public class CommWriter extends Communicator
{
    private CommWriterOutput _cwo;

    public CommWriter() {
    }

    public CommWriter(CommWriterOutput cwo) {
        _cwo = cwo;
    }

    public byte createHeader(int sora, int which)
    {
        return this.createHeader((byte) 0x00, (byte) sora, (byte) which);
    }

    public byte createHeader(byte n, byte sora, byte which)
    {
        byte header = (byte) (n | (sora << 1) | (which << 2));
        _buffer[_ptr++] = header;
        this.crc(header);

        return header;
    }

    public CommWriter addByte(byte data)
    {
        return this.add(data);
    }

    public CommWriter add(byte data)
    {
        if (Communicator.isCtrlChar(data) > 0)
        {
            _buffer[_ptr++] = (byte) Communicator.DLE;
            this.crc(Communicator.DLE);
        }
        _buffer[_ptr++] = data;
        this.crc(data);

        return this;
    }

    public CommWriter addChar(char data)
    {
        return add((byte) data);
    }

    public CommWriter addString(String data)
    {
        for (char c : data.toCharArray())
        {
            addChar(c);
        }
        add(TXRX_GENERIC_DELIMITER);
        return this;
    }

    public CommWriter addInt(int data)
    {
        for (int i = 0; i < TXRX_INT_SIZE; i++)
        {
            byte tmp = (byte) ((data >> (8*i)) & 0xff);
            this.add((byte) tmp);
        }
        return this;
    }

    public CommWriter addDouble(double data)
    {
        return addString(Double.toString(data));
    }

    public void send()
    {

//        byte output[] = new byte[_bb.position()];
//        System.arraycopy(_bb.array(), 0, output, 0, _bb.position());
//        _bb = ByteBuffer.allocate(Communicator.TXRX_BUFF_SIZE);


//        _cwo.push(STX);
//        _cwo.push(content(false));
//        _cwo.push(ETX);
//        this.crc(ETX); // YES, INCLUDE THIS!
//        _cwo.push((byte) (_crc & 0xff));
//        _cwo.flush();

        _cwo.sendData(content(true));
        reset();
    }
}
