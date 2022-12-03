package client;

import bean.Message;
import utils.MessageInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * 客户端
 * @author chenjing
 */
public class Client {

    public static void main(String[] args) {

        //设置为127.0.0.1
        String hostname;
        //设置为1099
        int port;
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        //主机名和端口号设为程序实参
        if (args.length != 2) {
            System.err.println("参数传递错误！");
            return;
        } else {
            hostname = args[0];
            port = Integer.parseInt(args[1]);
        }

        MessageInterface messageInterface = null;

        try {
            messageInterface = (MessageInterface) Naming.lookup("rmi://" + hostname + ":" + port + "/Message");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

        int choice = 0;

        while (true) {
            menu();
            try {
                choice = Integer.parseInt(keyboard.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String username, password, receiverName, message;
            switch (choice) {
                case 1: {
                    //register
                    try {
                        System.out.println("请输入用户名：");
                        username = keyboard.readLine();
                        System.out.println("请输入密码：");
                        password = keyboard.readLine();

                        assert messageInterface != null;

                        if (!messageInterface.register(username, password)) {
                            System.err.println("注册失败，该用户名已被占用！");
                        } else {
                            System.out.println("注册成功！");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 2: {
                    //showUsers
                    try {
                        assert messageInterface != null;
                        ArrayList<String> users = messageInterface.showUsers();
                        System.out.println("---------用户列表---------");
                        for (String user : users) {
                            System.out.println(user);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 3: {
                    //checkMessages
                    try {
                        System.out.println("请输入用户名：");
                        username = keyboard.readLine();
                        System.out.println("请输入密码：");
                        password = keyboard.readLine();
                        assert messageInterface != null;

                        ArrayList<Message> messages = messageInterface.checkMessages(username, password);

                        if (messages == null) {
                            System.err.println("用户名或密码输入错误");
                        } else {
                            if (messages.size() == 0) {
                                System.out.println("您没有任何留言！");
                            } else {
                                System.out.println("----------留言列表----------");
                                for (Message info : messages) {
                                    System.out.println(info);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 4: {
                    //leaveMessage
                    try {
                        System.out.println("请输入用户名：");
                        username = keyboard.readLine();
                        System.out.println("请输入密码：");
                        password = keyboard.readLine();
                        System.out.println("请输入接收用户名：");
                        receiverName = keyboard.readLine();
                        System.out.println("请输入消息：");
                        message = keyboard.readLine();

                        assert messageInterface != null;

                        int result = messageInterface.leaveMessage(username,password,receiverName,message);

                        if(result == -1) {
                            System.err.println("用户名或密码错误！");
                        } else if (result == 0) {
                            System.err.println("接收者不存在！");
                        } else if (result == 1) {
                            System.out.println("留言成功！");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case 5:{
                    System.out.println("再见！");
                    System.exit(0);
                }
                default :{
                    System.err.println("选项输入错误！请输入1-5");
                }
            }
        }
    }

    /**
     * 显示菜单
     */
    private static void menu() {
        System.out.println("-----------------------------------");
        System.out.println("分布式消息中心--功能列表");
        System.out.println("[1] 注册新用户  ");
        System.out.println("[2] 展示系统中所有用户名");
        System.out.println("[3] 打印指定用户的留言板");
        System.out.println("[4] 给指定用户留言");
        System.out.println("[5] 退出");
        System.out.println("-----------------------------------");
        System.out.println("输入您的选择->");
    }
}
