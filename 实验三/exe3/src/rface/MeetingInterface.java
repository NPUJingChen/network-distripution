package rface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * 远程接口，这个接口需要继承Remote,并且接口中的方法全部都要抛出RemoteException异常
 * @author chenjing
 */
public interface MeetingInterface extends Remote {

    /**
     * 用于用户登录
     * @param username 用户名
     * @param password 登录密码
     * @return
     * @throws RemoteException
     */
    public boolean userLogin(String username,String password) throws RemoteException;

    /**
     * 用于用户登录
     * java [clientname] [servername] [portnumber] add [username] [password] [otherusername] [start] [end] [title]
     * @param username 用户名
     * @param password 登录密码
     * @param otherUsers 会议参与者
     * @param meetingTitle 会议名称
     * @param meetingStartTime 会议开始时间
     * @param meetingEndTime 会议结束时间
     * @return
     * @throws RemoteException
     */
    public boolean addMeeting(String username, String password,
                                ArrayList<String> otherUsers, String meetingTitle,
                                Date meetingStartTime,Date meetingEndTime) throws RemoteException;

    /**
     * 用于用户注册
     * java [clientname] [servername] [portnumber] register [username] [password]
     * @param username 用户名
     * @param password 登录密码
     * @return
     * @throws RemoteException
     */
    public boolean userRegister(String username,String password) throws RemoteException;

    /**
     * 用于搜索会议
     * java [clientname] [servername] [portnumber] query [username] [password] [start] [end]
     * @param username 用户名
     * @param password 登录密码
     * @param meetingStartTime 会议开始时间
     * @param meetingEndTime 会议结束时间
     * @return
     * @throws RemoteException
     */
    public String queryMeeting(String username,String password,
                                Date meetingStartTime ,Date meetingEndTime) throws RemoteException;

    /**
     * 用于删除会议
     * java [clientname] [servername] [passport] delete [username] [password] [meetingid]
     * @param username 用户名
     * @param password 登录密码
     * @param meetingID 会议号
     * @return
     * @throws RemoteException
     */
    public boolean deleteMeeting(String username,String password,int meetingID) throws RemoteException;

    /**
     * 用于清除会议
     * java [clientname] [servername] [passport] clear [username] [password]
     * @param username 用户名
     * @param password 登录密码
     * @return
     * @throws RemoteException
     */
    public boolean clearMeeting(String username,String password) throws RemoteException;
}
