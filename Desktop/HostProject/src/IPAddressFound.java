import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPAddressFound {

    public static String findIpAddress() throws UnknownHostException {
	InetAddress localHost = InetAddress.getLocalHost();

	return localHost.getHostAddress();
    }

}
