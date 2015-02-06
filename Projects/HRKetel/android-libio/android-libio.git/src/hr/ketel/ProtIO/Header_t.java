package hr.ketel.ProtIO;

/**
 * Created by Rutger on 07/01/14.
 */
public class Header_t
{
    public byte h;
    public byte sora;
    public byte which;

    public Header_t(byte n, byte s, byte w)
    {
        h = n;
        sora = s;
        which = w;
    }
}
