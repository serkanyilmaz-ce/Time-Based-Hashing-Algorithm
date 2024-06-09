import java.io.IOException;
import java.net.InetAddress;

public class PingOperations {
	
	public static double pingHost(String host) throws IOException{
		double calculatedPingTime = 0.0;
        InetAddress inet = InetAddress.getByName(host);
        long startTime = System.currentTimeMillis();
        boolean status = inet.isReachable(5000);
        long endTime = System.currentTimeMillis();
        if(status) {
            long duration = endTime - startTime;  // Ping süresini hesapla
        	System.out.println(host + " is reachable. Ping time: " + duration + " ms");
        	return duration;
        }else {
            System.out.println("Error pinging host " + host + ": ");
        }
		
		return calculatedPingTime;
	}
	
	public static double averagePingTime(String host) throws IOException {
		double averagePingTimes = 0.0;
		for(int i = 0; i <  3 ; i++ ) {
			averagePingTimes += pingHost(host);
		}
		averagePingTimes = (averagePingTimes / 3);
		System.out.println("avereage ping time to  " + host + " is: " + averagePingTimes);
		return averagePingTimes;
	}

}
