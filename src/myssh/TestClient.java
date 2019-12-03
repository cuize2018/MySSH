package myssh;

import myssh.ReadThread;
import myssh.SSHClient;
import ch.ethz.ssh2.Connection;

public class TestClient {
    public static void main(String[] args) {
        String host = "202.117.43.193";
        String user = "xgwang2";
        String pwd  = "1";
        int port = 22;

        SSHClient client = new SSHClient(host, user, pwd, port);
        try {
            Connection con = client.CreatConnection();
            if (client.isAuthenticate(con)){
                client.StartConnection(con);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
