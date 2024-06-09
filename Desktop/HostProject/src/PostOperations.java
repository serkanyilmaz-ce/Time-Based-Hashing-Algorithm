import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PostOperations {

    private int connectionCount;
    private String hostAdress;

    static String postTransaction(String request, String hostAdress) throws IOException {
	DataOutputStream wr = null;
	InputStream is = null;
	BufferedReader rd = null;
	StringBuilder rs = new StringBuilder();
	int responseCode = 0;

	try {
	    URL url = new URL(hostAdress);
	    byte[] postDataBytes = request.getBytes(StandardCharsets.UTF_8);

	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    conn.setRequestProperty("Accept", "application/json" + ";charset=UTF-8");

	    conn.setDoInput(true);
	    conn.setDoOutput(true);
	    conn.setAllowUserInteraction(true);
	    conn.connect();
	    wr = new DataOutputStream(conn.getOutputStream());
	    wr.write(postDataBytes);
	    wr.flush();
	    responseCode = conn.getResponseCode();
	    is = HttpURLConnection.HTTP_OK == responseCode ? conn.getInputStream() : conn.getErrorStream();
	    rd = new BufferedReader(new InputStreamReader(is));
	    String line;
	    while ((line = rd.readLine()) != null) {
		rs.append(line);
		rs.append('\r');
	    }
	    rd.close();

	} catch (Exception e) {

	} finally {
	    closeConnection(is, wr, null, rd);
	}

	return rs.toString();
    }

    public static void closeConnection(InputStream is, DataOutputStream wr, StringWriter writer, BufferedReader rd) {
	if (is != null) {
	    try {
		is.close();
	    } catch (IOException e) {
		System.out.println(e);
	    }
	}
	if (wr != null) {
	    try {
		wr.close();
	    } catch (IOException e) {
		System.out.println(e);
	    }
	}
	if (writer != null) {
	    try {
		writer.close();
	    } catch (IOException e) {
		System.out.println(e);
	    }
	}
	if (rd != null) {
	    try {
		rd.close();
	    } catch (IOException e) {
		System.out.println(e);
	    }
	}
    }

    public int getConnectionCount() {
	return connectionCount;
    }

    public void setConnectionCount(int connectionCount) {
	this.connectionCount = connectionCount;
    }

    public String getHostAdress() {
	return hostAdress;
    }

    public void setHostAdress(String hostAdress) {
	this.hostAdress = hostAdress;
    }

}
