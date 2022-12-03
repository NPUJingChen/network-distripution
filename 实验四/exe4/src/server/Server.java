package server;

import bean.Project;
import bean.User;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * @author chenjing
 */

@WebService(name = "WebService", portName = "servicePort", targetNamespace = "http://www.service.com")
public class Server {

    private final ArrayList<User> users = new ArrayList<User>();

    private final ArrayList<Project> projects = new ArrayList<Project>();

    private int id = 0;

    public static void main(String[] args) throws InterruptedException {

        //页面发布
        //wsimport -encoding utf-8 -keep -s C:\Users\86182\Desktop\网络与分布式\实验四\exe4\src http://127.0.0.1:8000/WebService?wsdl
        Endpoint.publish("http://127.0.0.1:8000/WebService",new Server());
        System.out.println("服务器启动成功");
    }

    /**
     * 根据用户名判断用户是否存在
     * @param username 用户名
     * @return
     */
    public boolean userExist(String username) {

        if(username.isEmpty()) {
            return false;
        }
        for (User user : users) {
            if (user.getName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @WebMethod
    public boolean register(User user) {
        if(userExist(user.getName())) {
            System.out.println("用户名已存在!");
            return false;
        }
        users.add(user);
        System.out.println("添加用户成功!");
        return true;
    }

    /**
     * 添加项目
     * @param user
     * @param project
     * @return
     */
    @WebMethod
    public boolean addProject(User user,Project project) {
        if (!userExist(user.getName())) {
            return false;
        }

        //判断时间是否冲突
        for(Project p:projects) {
            if(p.getStartTime().compareTo(project.getEndTime())>=0 ||
                p.getEndTime().compareTo(project.getStartTime()) <= 0) {
                continue;
            }
            System.out.println("和已有事项时间冲突");
            return false;
        }

        project.setId(id++);
        projects.add(project);
        return true;
    }

    /**
     * 根据开始时间和结束时间查询这个时间段里面的项目
     * @param user
     * @param startTime
     * @param endTime
     * @return
     */
    @WebMethod
    public ArrayList<Project> searchProject(User user, Date startTime,Date endTime) {
        if(!userExist(user.getName())) {
            return null;
        }

        ArrayList<Project> p = new ArrayList<Project>();
        for (Project ps:projects) {
            if(ps.getUser().getName().equals(user.getName())
                && ps.getStartTime().after(startTime)
                && ps.getEndTime().before(endTime)) {
                p.add(ps);
            }
        }
        return p;
    }

    /**
     * 删除用户的指定的ID项目
     * @param user
     * @param ID
     * @return
     */
    @WebMethod
    public boolean delete(User user,int ID) {
        if (!userExist(user.getName())) {
            return false;
        }
        boolean flag = projects.removeIf(p -> p.getID()==ID);
        return flag;
    }

    /**
     * 清空项目列表
     * @param user
     * @return
     */
    @WebMethod
    public boolean clear(User user) {
        if (!userExist(user.getName())) {
            return false;
        }
        boolean flag = projects.removeIf(p -> Objects.equals(p.getUser(),user));
        return flag;
    }
}
