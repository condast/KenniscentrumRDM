package hr.ketel.ProtIO;


/**
 * Created by Rutger on 07/01/14.
 */
public interface CommReaderCallback
{
    public void notify(Header_t header, byte[] message);
}
