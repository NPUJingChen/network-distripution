package rface;

import bean.Meeting;
import bean.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * 实现远程接口类，必须继承UnicastRemoteObject类,
 * 只有继承UnicastRemoteObject类才表示他可以作为远程对象，被注册到注册表当中去供客户端远程调用
 * @author chenjing
 */
public class InterfaceImplements extends UnicastRemoteObject implements MeetingInterface {

    /**
     * 用户列表
     */
    private final ArrayList<User> userList = new ArrayList<User>();

    /**
     * 会议列表
     */
    private final ArrayList<Meeting> meetingList = new ArrayList<Meeting>();

    int meetingID = 1;

    /**
     * 构造函数，设计一些测试用例，用来测试是否可以使用登录功能
     * @throws RemoteException
     */
    public InterfaceImplements() throws RemoteException {
        super();
        userList.add(new User("cj","123"));
        userList.add(new User("cj2","456"));
        userList.add(new User("cj3","789"));
        meetingList.add(new Meeting(0,"test",
                        new Date(100),new Date(200),new User("cj","123"),
                        null));
    }

    @Override
    public boolean userLogin(String username, String password) throws RemoteException {
        return userExists(username,password);
    }

    /**
     * 判断用户是否存在
     * @param username 用户名
     * @param password 登录密码
     * @return
     */
    private boolean userExists(String username, String password) {

        for(User user : userList) {
            if (user.getName().equals(username)&&user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addMeeting(String username, String password, ArrayList<String> otherUsers, String meetingTitle, Date meetingStartTime, Date meetingEndTime) throws RemoteException {

        User user = new User();
        for (User u : userList) {
            if(Objects.equals(u.getName(),username)
            &&Objects.equals(u.getPassword(), password)) {
                user=u;
            }
        }

        //统计user数量
        int count=0;
        for(String other : otherUsers) {
            for(User u : userList) {
                if (Objects.equals(u.getName(), other)) {
                    count++;
                }
            }
        }

        if (count != otherUsers.size()) {

            //add中的参会者没有在server端注册
            System.out.println("有参会者没有在server端注册");
            return false;
        }

        //判断时间是否冲突
        for(Meeting meeting:meetingList) {

            //如果添加的会议的开始时间在会议列表里面一个会议的结束时间之前则添加失败
            //如果添加的会议的结束时间在会议列表里面一个会议的开始时间之气则添加失败
            if(meeting.getMeetingEndTime().compareTo(meetingStartTime)<=0||
                meeting.getMeetingStartTime().compareTo(meetingEndTime)>=0) {
                continue;
            }
            System.out.println("和已有会议的时间安排重叠");
            return false;
        }

        //添加会议
        //meetingID不能重复，这里
        Meeting meeting = new Meeting(meetingID++,meetingTitle,meetingStartTime,
                                        meetingEndTime,user,otherUsers);
        meetingList.add(meeting);

        System.out.println("add meeting");
        System.out.println("------------------------------------");
        for(User u:userList) {
            System.out.println(u.toString());
        }
        System.out.println("------------------------------------");
        return true;
    }

    @Override
    public boolean userRegister(String username, String password) throws RemoteException {
        User user = new User(username,password);
        if (userExists(username,password)) {
        return false;
        //已经注册过了，不可以再次注册
        }
        userList.add(user);
        System.out.println("user register");
        System.out.println("------------------------------------");
        for (User u : userList) {
            System.out.println(u.toString());
        }
        System.out.println("------------------------------------");
        return true;
    }

    @Override
    public String queryMeeting(String username, String password, Date meetingStartTime, Date meetingEndTime) throws RemoteException {
        if(!userExists(username,password)) {
            return null;
        }
        System.out.println("query meeting");
        StringBuilder sb = new StringBuilder();
        for(Meeting m:meetingList) {
            if(m.getMeetingStartTime().compareTo(meetingStartTime)>=0&&m.getMeetingEndTime().compareTo(meetingEndTime)<=0
                &&m.getMeetingStartTime().compareTo(meetingEndTime)<=0&&m.getMeetingEndTime().compareTo(meetingStartTime)>=0
                &&(m.getOrganizer().getName().equals(username)||m.getParticipants().contains(username))) {
                sb.append(m.toString()).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public boolean deleteMeeting(String username, String password, int meetingID) throws RemoteException {
        if(!userExists(username,password)) {
            return false;
        }
        return meetingList.removeIf(m->m.getMeetingID()==meetingID);
    }

    @Override
    public boolean clearMeeting(String username, String password) throws RemoteException {
        if (!userExists(username, password)) {
            return false;
        }
        return meetingList.removeIf(m -> Objects.equals(m.getOrganizer().getName(),username)
                                    &&Objects.equals(m.getOrganizer().getPassword(),password));
    }
}
