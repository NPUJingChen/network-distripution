package server;

import rface.InterfaceImplements;
import rface.MeetingInterface;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * 服务器启动
 * @author chenjing
 */
public class MeetringServer {

    /**
     * 用于连接的端口号
     */
    private static final int PORT = 1099;

    public static void main(String[] args) {
        try {
            //启动RMI注册服务，指定端口为1099
            LocateRegistry.createRegistry(PORT);
            MeetingInterface meetingInterface =new InterfaceImplements();

            //命名服务绑定127.0.0.1:1099/Meeting
            Naming.bind("Meeting",meetingInterface);
            System.out.println("Server started successfully!");
        }catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
