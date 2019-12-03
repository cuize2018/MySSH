package myssh;

import java.io.*;

public class ReadThread implements Runnable {

    private InputStream in;
    private PrintStream out;
    private String charSet;
    private boolean runFlag;

    public ReadThread(InputStream in, PrintStream out){
        super();
        this.in = in;
        this.out = out;
        this.charSet = "UTF-8";
        this.runFlag = true;
    }

    public void Stop(){
        this.runFlag = false;
    }

    @Override
    public void run() {
        BufferedReader br = null;

        try {
           br = new BufferedReader(new InputStreamReader(in, charSet));
           String temp;
           while ((temp = br.readLine()) != null && runFlag){
               if (out != null){
                   out.println(temp);
                   out.flush();
               }
           }
        }
        catch (Exception e){
            e.printStackTrace();;
        }
    }
}
