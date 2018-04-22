
public class KeyGeneration 
{
    public byte[] ByteSeq;
    public byte[] GenerateBytes()
    {
        byte[] Bytes=new byte[128];
        for(int i=0;i<128;i++)
        {
            Bytes[i] =(byte)i;
            Bytes[i] =(byte) (Bytes[i]<<1);
        }
        return Bytes;
    }
    
    public KeyGeneration()
    {
        ByteSeq=GenerateBytes();
    }    
}
