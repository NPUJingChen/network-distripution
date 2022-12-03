package client;

import rface.MeetingInterface;

import javax.lang.model.element.Name;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * 客户端
 * @author chenjing
 */
public class MeetingClient {

    MeetingInterface RMI;

    String username;

    String password;

    Date MAXYears = new Date();

    MeetingClient() {
        MAXYears.setTime(Long.MAX_VALUE);
    }

    public void showMenu() {

        String s = "Menu:\n" +
                "1. add <userNameList> <start> <end> <title>\n" +
                "2. delete <meetingID>\n" +
                "3. clear\n" +
                "4. query <start> <end>\n" +
                "5. help\n" +
                "6. quit";
        System.out.println(s);
    }

    public static void main(String[] args) {
        MeetingClient meetingClient = new MeetingClient();
        //至少需要传入两个参数，分别为远程地址和端口号
        try {
            //建议传入 TODO RMIClient 127.0.0.1 1099
            if (args.length < 3) {
                System.err.println("Parameter Error");
                return;
            }
            String host = args[1];
            String port = args[2];

            // Naming类提供存储和获得“远程对象注册表”上远程对象的引用的方法。
            // Naming 类的每个方法都可将某个名称作为其一个参数，该名称是使用以下形式的 URL 格式
            // 的 java.lang.String：//host:port/name
            // lookup()返回与指定 name 关联的远程对象的引用（一个 stub）。
            meetingClient.RMI = (MeetingInterface) Naming.lookup("//" + host + ":" + port + "/Meeting");

            //登录状态
            boolean loginStatus = false;

            if (args.length > 3) {
                //使用命令行登录和使用

                String order = args[3];
                if ("quit".equals(order)) {
                    return;
                }
                //示例 TODO java client/MeetingClient RMIClient 127.0.0.1 1099 help
                if ("help".equals(order)) {
                    meetingClient.help();
                    return;
                }
                meetingClient.username = args[4];
                meetingClient.password = args[5];

                switch (order) {
                    case "login": {
                        //示例 TODO java client/MeetingClient RMIClient 127.0.0.1 1099 login cj 123
                        loginStatus = meetingClient.RMI.userLogin(meetingClient.username, meetingClient.password);
                        if (!loginStatus) {
                            System.err.println("login failed");
                            return;
                        }
                        break;
                    }
                    case "register": {
                        //示例 TODO java client/MeetingClient RMIClient 127.0.0.1 1099 register cj 123
                        loginStatus = meetingClient.RMI.userLogin(meetingClient.username, meetingClient.password);
                        if (!loginStatus) {
                            System.err.println("register failed");
                            return;
                        }
                        break;
                    }
                    case "add":{
                        //示例 TODO java client/MeetingClient RMIClient 127.0.0.1 1099 add cj 123 cj2 cj3 2022-10-30 12:00:00 2022-10-30 13:00:00 test
                        String title = args[args.length - 1];
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //因为不知道要添加多少个会议参与者，所以开始和结束时间从输入的命令后面开始减
                        Date meetingStartTime = sdf.parse(args[args.length - 5] + " " + args[args.length - 4]);
                        Date meetingEndTime = sdf.parse(args[args.length - 3] + " " + args[args.length - 2]);
                        ArrayList<String> otherUsers = new ArrayList<>(Arrays.asList(args).subList(6, args.length-5));
                        loginStatus = meetingClient.RMI.addMeeting(meetingClient.username, meetingClient.password,
                                otherUsers,title,meetingStartTime,meetingEndTime);
                        break;
                    }
                    case "query":{
                        //示例 TODO java client/MeetingClient RMIClient 127.0.0.1 1099 query cj 123 2022-09-22 12:00:00 2022-10-30 12:00:22
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date meetingStartTime = sdf.parse(args[args.length - 4] + " " + args[args.length - 3]);
                        Date meetingEndTime = sdf.parse(args[args.length - 2] + " "+args[args.length - 1]);
                        System.out.println(meetingClient.RMI.queryMeeting(meetingClient.username, meetingClient.password,
                                            meetingStartTime,meetingEndTime));
                        loginStatus = true;
                        break;
                    }
                    case "delete":{
                        //示例 TODO java client/MeetingClient RMIClient 127.0.0.1 1099 clear cj 123 0
                        int meetingID = Integer.parseInt(args[args.length - 1]);
                        loginStatus = meetingClient.RMI.deleteMeeting(meetingClient.username, meetingClient.password, meetingID);
                        break;
                    }
                    case "clear":{
                        //示例 TODO java client/MeetingClient RMIClient 127.0.0.1 1099 clear cj 123
                        loginStatus = meetingClient.RMI.clearMeeting(meetingClient.username, meetingClient.password);
                        break;
                    }
                    default :{
                        System.out.println(meetingClient.RMI.queryMeeting(meetingClient.username, meetingClient.password, new Date(0), meetingClient.MAXYears));
                    }
                }
                if(loginStatus) {
                    System.out.println("success!");
                } else {
                    System.out.println("failed!");
                }
            } else {
                //ide控制台登录
                while (!loginStatus) {
                    System.out.println("Login or Register?");
                    //只要服务器不关闭，之前保存的都不会被删除
                    System.out.println("[1]Login    [2]Register");
                    Scanner snc = new Scanner(System.in);
                    String choice = snc.nextLine();
                    System.out.println("Username:");
                    meetingClient.username = snc.nextLine();
                    System.out.println("Password:");
                    meetingClient.password = snc.nextLine();

                    switch (choice) {
                        case "1": {
                            loginStatus = meetingClient.RMI.userLogin(meetingClient.username, meetingClient.password);
                            break;
                        }
                        case "2": {
                            loginStatus = meetingClient.RMI.userRegister(meetingClient.username, meetingClient.password);
                            break;
                        }
                        default: {
                            continue;
                        }
                    }
                    if (loginStatus) {
                        //登录成功
                        System.out.println("Welcome " + meetingClient.username + "!");
                        break;
                    }
                    System.err.println((Objects.equals(choice, "1") ? "Login" : "Register") + " failed!");
                    }

                    boolean flag = true;
                    while (flag) {
                        meetingClient.showMenu();
                        System.out.println("输入一个命令:");
                        Scanner scanner = new Scanner(System.in);
                        String order = scanner.nextLine();
                        //将order用空格分开
                        switch (order.split(" ")[0]) {
                            case "add": {
                                if (meetingClient.add(order)) {
                                    System.out.println("添加会议成功");
                                } else {
                                    System.out.println("添加会议失败");
                                }
                                break;
                            }
                            case "delete": {
                                if (meetingClient.delete(order)) {
                                    System.out.println("删除会议成功");
                                } else {
                                    System.out.println("删除会议失败");
                                }
                                break;
                            }
                            case "query": {
                                System.out.println(meetingClient.query(meetingClient.username, meetingClient.password, order));
                                break;
                            }
                            case "clear": {
                                if (meetingClient.clear(order)) {

                                    System.out.println("清除会议成功");
                                } else {
                                    System.out.println("清除会议失败");
                                }
                                break;
                            }
                            case "help":{
                                meetingClient.help();
                                break;
                            }
                            case "quit":{
                                flag = false;
                                break;
                            }
                            default :{
                                System.out.println(meetingClient.RMI.queryMeeting(meetingClient.username,
                                        meetingClient.password, new Date(0),meetingClient.MAXYears));
                            }
                        }
                    }
                }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean clear(String order) throws RemoteException {
        //只可以清除本人创建的会议
        //示例 TODO clear
        RMI.clearMeeting(username,password);
        return true;
    }

    private String query(String username, String password, String order) throws RemoteException, ParseException {

        //将order用空格分开
        String[] params = order.split(" ");
        //query [start] [end]
        //TODO query 2021-01-01 00:00:00 2025-01-01 00:00:00
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date meetingStartTime = sdf.parse(params[1] + " " + params[2]);

        Date meetingEndTime = sdf.parse(params[3] + " "+params[4]);

        return RMI.queryMeeting(username,password,meetingStartTime,meetingEndTime);
    }

    private boolean delete(String order) throws RemoteException {
        //delete [meetingID]
        //TODO delete 1
        String[] params = order.split(" ");
        int meetingID = Integer.parseInt(params[params.length - 1]);
        return RMI.deleteMeeting(username,password,meetingID);
    }

    private boolean add(String order) {
        try {
            String[] params = order.split(" ");

            //add [otherusername] [start] [end] [title]
            //otherusername是必须已经注册过的，时间段也不能和已经有的会议重叠
            //TODO add cj2 cj3 2022-10-30 12:00:00 2022-10-31 12:00:00 test

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //因为不知道要添加多少个会议参与者，所以开始和结束时间从输入的命令后面开始减
            ArrayList<String> otherUsers = new ArrayList<String>(Arrays.asList(params).subList(1, params.length-5));

            String title = params[params.length - 1];

            Date meetingStartTime = sdf.parse(params[params.length - 5] + " " +params[params.length - 4]);
            Date meetingEndTime = sdf.parse(params[params.length - 3] + " "+params[params.length - 2]);

            if(meetingStartTime.compareTo(meetingEndTime)>0) {
                System.err.println("时间错误");
                return false;
            }

            return RMI.addMeeting(username,password,otherUsers,title,meetingStartTime,meetingEndTime);
        } catch (ParseException | RemoteException e) {
            System.err.println("添加失败");
            return false;
        }
    }

    private void help() {

        String help = "支持两种方式的运行\n"
                + "方式一：以参数形式启动\n"
                + "命令：\n"
                + "\t  |注册用户：[clientname] [servername] [portnumber] register [username] [password]\n"
                + "\t  |添加会议：[clientname] [servername] [portnumber] add [username] [password] [otherusername] [start] [end] [title]\n"
                + "\t  |查询会议：[clientname] [servername] [portnumber] query [username] [password] [start] [end]\n"
                + "\t  |删除会议：[clientname] [servername] [portnumber] delete [username] [password] [meetingid]\n"
                + "\t  |清除会议：[clientname] [servername] [portnumber] clear [username] [password]\n"
                + "方式二：以命令行方式启动\n" +
                "启动参数：[clientname] [servername] [portnumber]\n" +
                "首先需要登录，登录后按提示输入相应指令。";

        System.out.println(help);
    }
}
