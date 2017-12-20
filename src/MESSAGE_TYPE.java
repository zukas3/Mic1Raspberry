/// Enum used for identifying the messages sent between Client and Server
public enum MESSAGE_TYPE
{
    UTF(0), ERROR(-1), INPUT(2), OUTPUT(3), FILE_TRANSFER_START(10), FILE_TRANSFER_END(20);

    private int value;
    private MESSAGE_TYPE(int value)
    {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean Compare(int i){return value == i;}
    public static MESSAGE_TYPE GetValue(int _id)
    {
        MESSAGE_TYPE[] As = MESSAGE_TYPE.values();
        for(int i = 0; i < As.length; i++)
        {
            if(As[i].Compare(_id))
                return As[i];
        }
        return MESSAGE_TYPE.ERROR;
    }
}
