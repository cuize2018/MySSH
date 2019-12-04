package myssh;

import ch.ethz.ssh2.Connection;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartClient {
    public static void main(String[] args) {
        String host;
        String user;
        String pwd;
        int port;

        System.out.println("如果有保存的配置则输入:配置名.config，否则输入ssh命令：");
        Scanner scanner = new Scanner(System.in);
        String ssh_command;
//        String ssh_pattern = "^ssh\\s([\\s\\S]*)@((?:(?:25[0-5]|2[0-4]\\d|(?:1\\d{2}|[1-9]?\\d))\\.){3}(?:25[0-5]|2[0-4]\\d|(?:1\\d{2}|[1-9]?\\d)))((\\s-p\\s\\d{1,5})*)";
        String ssh_pattern = "^ssh\\s([\\S]*)@(\\d+.\\d+.\\d+.\\d+)((\\s-p\\s\\d{1,5})?)";
        Pattern p = Pattern.compile(ssh_pattern);
        while (true){
           ssh_command = scanner.nextLine();
           if (ssh_command != null){
               //使用配置文件模式
               if (ssh_command.contains(".config")){
                   String configName = ssh_command.replace(".config", "");
                   File file = new File("./config/"+configName + ".json");
                   if (file.exists()){
                       ConfigFile cfp = new ConfigFile();
                       cfp.getDatafromFile(file);
                       StartSSH(cfp.getHost(), cfp.getUsr(), cfp.getPwd(), cfp.getPort(), scanner, "config");
                   }
                   else {
                       System.out.println("输入配置文件名错误，请重新输入");
                   }
               }
               //使用ssh命令模式
               else {
                   Matcher matcher = p.matcher(ssh_command);
                   if (matcher.matches()){
                       host = matcher.group(2);
                       user = matcher.group(1);

                       Pattern tmp = Pattern.compile("((\\s-p\\s)(\\d{1,5}))");
                       Matcher m = tmp.matcher(ssh_command);
                       if (m.find()){
                           port = Integer.parseInt(m.group(3));
                       }
                       else {
                           port = 22;
                       }
                       System.out.println("请输入账户密码：");
                       pwd = scanner.nextLine();
                       StartSSH(host,user,pwd,port,scanner, "order");
                   }
                   else {
                       System.out.println("输入命令格式错误! 请重新输入:" + "\n" + "ssh命令格式如：ssh usr@hostname -p PortNumber(默认22)");
                   }
               }
           }
        }
    }

    private static void StartSSH(String host, String user, String pwd, int port, Scanner scanner, String mode){
        SSHClient client = new SSHClient(host, user, pwd, port);
        try {
            Connection con = client.CreatConnection();
            if (client.isAuthenticate(con)){
                if (mode.equals("order")) {
                    System.out.println("是否保存账号信息？(默认：no)");
                    String ifSaveInfo = scanner.nextLine();

                    if (ifSaveInfo.equals("yes")) {
                        System.out.println("请输入保存文件名：(默认：config)");

                        String filename = scanner.nextLine();
                        if (filename == null) filename = "config";

                        ConfigFile cfp = new ConfigFile(host, user, pwd, port);
                        cfp.saveDataToFile(filename);
                    }
                }
                client.StartConnection(con);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
