/// Enum used for identifying the messages sent between Client and Server
public enum MESSAGE_TYPE
{
    ERROR(-1), INPUT_CHAR(2), OUTPUT_CHAR(3), SERVER_STATUS(5), PROGRAM_TRANSFER_START(10), PROGRAM_TRANSFER_END(11), PROGRAM_START(12), CONNECTION_TERMINATION(100), UTF(200);

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
