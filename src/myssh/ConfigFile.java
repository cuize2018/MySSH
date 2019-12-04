package myssh;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigFile {
    private String host;
    private String usr;
    private String pwd;
    private int port;

    public ConfigFile(String h, String usr, String pwd, int port){
        this.host = h;
        this.usr = usr;
        this.pwd = pwd;
        this.port = port;
    }

    public ConfigFile(){
    }

    public String getHost(){
        return host;
    }

    public String getUsr() {
        return usr;
    }

    public String getPwd() {
        return pwd;
    }

    public int getPort() {
        return port;
    }

    public void saveDataToFile(String fileName) {
        BufferedWriter writer = null;
        File file = new File(".\\config\\"+ fileName + ".json");
        //如果文件不存在，则新建一个
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写入
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false), "UTF-8"));
            writer.write("{" + "\n");
            writer.write("\t" + "host: " + this.host + "\n");
            writer.write("\t" + "user: " + this.usr + "\n");
            writer.write("\t" + "pwd: "  + this.pwd + "\n");
            writer.write("\t" + "port: " + this.port + "\n");
            writer.write("}" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("文件保存成功！");
    }


    public void getDatafromFile(File f) {
        BufferedReader reader = null;
        List<String> info = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                tempString = tempString.trim();
                if (tempString.contains(":")) {
                    String[] tmp = tempString.split(":");
                    info.add(tmp[1].trim());
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        host = info.get(0);
        usr = info.get(1);
        pwd = info.get(2);
        port = Integer.parseInt(info.get(3));
    }



}
