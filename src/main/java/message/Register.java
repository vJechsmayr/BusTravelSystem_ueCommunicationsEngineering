package message;

/**
 * Created by jexmaster on 06.01.2016.
 *
 * @author Viktoria Jechsmayr
 */
public class Register {
    private final String systemName;
    private final String systemIP;
    private final String systemPort;

    public Register(String name, String ip, String port)
    {
        this.systemIP = ip;
        this.systemName = name;
        this.systemPort = port;
    }

    public String getSystemName()
    {
        return systemName;
    }

    public String getSystemIP()
    {
        return systemIP;
    }

    public String getSystemPort()
    {
        return systemPort;
    }

}
