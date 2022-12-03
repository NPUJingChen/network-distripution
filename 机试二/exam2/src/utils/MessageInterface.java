package utils;

import bean.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * 系统功能接口
 * @author chenjing
 */
public interface MessageInterface extends Remote {

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     * @throws RemoteException
     */
    public boolean register(String username,String password) throws RemoteException;

    /**
     * 显示所有注册用户
     * @return
     * @throws RemoteException
     */
    public ArrayList<String> showUsers() throws RemoteException;

    /**
     * 打印用户的所有留言
     * @param username
     * @param password
     * @return
     * @throws RemoteException
     */
    public ArrayList<Message> checkMessages(String username, String password) throws RemoteException;

    /**
     * 给其他用户留言
     * @param username
     * @param password
     * @param receiverName
     * @param message
     * @return
     * @throws RemoteException
     */
    public int leaveMessage(String username,String password,String receiverName,String message) throws RemoteException;
}
