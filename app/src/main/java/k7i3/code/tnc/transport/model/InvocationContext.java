package k7i3.code.tnc.transport.model;

/**
 * Created by k7i3 on 07.09.15.
 */
public class InvocationContext {
    private String clientIPAddress;
    private String initiator;
    private String password;
    private String userName;

    public InvocationContext(String clientIPAddress, String initiator, String password, String userName) {
        this.clientIPAddress = clientIPAddress;
        this.initiator = initiator;
        this.password = password;
        this.userName = userName;
    }
}
