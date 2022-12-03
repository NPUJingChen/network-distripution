package server;

import utils.InterfaceImplements;
import utils.MessageInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * 服务器端
 * @author chenjing
 */
public class Server {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            MessageInterface messageInterface = new InterfaceImplements();
            Naming.rebind("Message",messageInterface);
            System.out.println("服务器启动成功！");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
