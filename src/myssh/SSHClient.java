package myssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.Session;

import java.io.*;
import java.util.Scanner;

import myssh.ReadThread;

public class SSHClient {
    private String host;
    private String user;
    private String pwd;
    private int port;

    public SSHClient(String host, String user, String pwd, int port){
        this.host = host;
        this.user = user;
        this.pwd = pwd;
        this.port = port;
    }

    public Connection CreatConnection() throws IOException {
        Connection connect = new Connection(host, port);
        ConnectionInfo conInfo = connect.connect();
        return connect;
    }

    public boolean isAuthenticate(Connection connect) throws IOException {
        return connect.authenticateWithPassword(user, pwd);
    }

    public void StartConnection(Connection con) throws IOException {
        Session session = con.openSession();
        session.requestPTY("bash");
        session.startShell();

        ReadThread is = new ReadThread(session.getStdout(), new PrintStream(System.out));
        new Thread(is).start();

        ReadThread error = new ReadThread(session.getStderr(), new PrintStream(System.out));
        new Thread(error).start();

        Scanner scanner = new Scanner(System.in);
        while (true){
            String command = scanner.nextLine();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(session.getStdin(), "utf-8"));
            bw.write(command+"\n");
            bw.flush();

            if (command.equals("exit")){
                is.Stop();
                session.close();
                con.close();
                scanner.close();
                break;
            }
        }
    }
}
