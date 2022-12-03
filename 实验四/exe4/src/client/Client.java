package client;

import com.service.Project;
import com.service.ServerService;
import com.service.User;
import com.service.WebService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author chenjing
 */

//TODO add cj 123 meeting 2022-11-06 14:00:00 2022-11-06 12:00:00
//TODO add cj 123 meeting 2022-11-06 12:00:00 2022-11-06 14:00:00
//TODO search cj 123 2022-10-06 12:00:00 2022-12-06 14:00:00

public class Client {

    ServerService serverService;

    WebService webService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 将Date类型转换为XMLGregorianCalendar类型
     * @param date
     * @return
     */
    public XMLGregorianCalendar convertToXmlGregorianCalendar(Date date) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar xgc = null;
        try {
            xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return xgc;
    }

    /**
     * 将XMLGregorianCalendar类型转换为Date类型
     * @param xgc
     * @return
     */
    public Date convertToDate(XMLGregorianCalendar xgc) {
        GregorianCalendar cal = xgc.toGregorianCalendar();
        return cal.getTime();
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    Client() {
        serverService = new ServerService();
        webService = serverService.getServicePort();
    }

    private void run() {
        try {
            while (true) {
                showMenu();
                InputStreamReader isr = new InputStreamReader(System.in);
                BufferedReader br = new BufferedReader(isr);
                String command = br.readLine();

                //将命令用空格符分开，取数组首位
                switch (command.split(" ")[0]) {
                    case "register":{
                        register(command);
                        break;
                    }
                    case "add":{
                        add(command);
                        break;
                    }
                    case "search":{
                        search(command);
                        break;
                    }
                    case "delete":{
                        delete(command);
                        break;
                    }
                    case "clear":{
                        clear(command);
                        break;
                    }
                    case "quit":{
                        return;
                    }
                    default :{
                        System.err.println("error command!");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clear(String command) {
        User user = createUser(command);

        if (!webService.clear(user)) {
            System.err.println("clear failed!");
        } else {
            System.out.println("clear success!");
        }
    }

    private void delete(String command) {
        User user = createUser(command);

        int ID = Integer.parseInt(command.split(" ")[3]);

        if(!webService.delete(user,ID)) {
            System.err.println("delete failed!");
        } else {
            System.out.println("delete success");
        }
    }

    /**
     * 搜索项目
     * @param command
     */
    private void search(String command) {
        User user = createUser(command);
        String startTime =command.split(" ")[3] + " " +command.split(" ")[4];
        String endTime = command.split(" ")[5] + " "+command.split(" ")[6];

        ArrayList<Project> projects = new ArrayList<Project>();

        try {
            projects = (ArrayList<Project>) webService.searchProject(user,convertToXmlGregorianCalendar(sdf.parse(startTime)),
                        convertToXmlGregorianCalendar(sdf.parse(endTime)));

            for (Project project : projects) {
                System.out.println("Project ID: " + project.getID()
                        +", Project Title: " + project.getTitle()
                        +", Project User: " + project.getUser().getName()
                        +", Start Time: " + sdf.format(convertToDate(project.getStartTime()))
                        +", End Time: " + sdf.format(convertToDate(project.getEndTime())));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加项目
     * @param command
     */
    private void add(String command) {
        try {
            Project project = createProject(command);

            if(project==null|| !webService.addProject(project.getUser(),project)) {
                System.err.println("add failed!");
            } else {
                System.out.println("add success");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建项目
     * @param command
     * @return
     * @throws ParseException
     */
    private Project createProject(String command) throws ParseException {
        User user = createUser(command);
        String title = command.split(" ")[3];
        String startTime = command.split(" ")[4] + " " + command.split(" ")[5];
        String endTime = command.split(" ")[6] + " "+command.split(" ")[7];

        if (startTime.compareTo(endTime)>0) {
            System.err.println("time error!");
            return null;
        }

        Project project = new Project();
        project.setTitle(title);
        project.setStartTime(convertToXmlGregorianCalendar(sdf.parse(startTime)));
        project.setEndTime(convertToXmlGregorianCalendar(sdf.parse(endTime)));
        project.setUser(user);

        return project;
    }

    /**
     * 用户注册
     * @param command
     */
    private void register(String command) {
        User user = createUser(command);
        if(!webService.register(user)) {
            System.err.println("register failed,username existed!");
        } else {
            System.out.println("register success!");
        }
    }

    /**
     * 创建用户
     * @param command
     * @return
     */
    private User createUser(String command) {
        String username = command.split(" ")[1];
        String password = command.split(" ")[2];

        User user = new User();
        user.setName(username);
        user.setPassword(password);

        return user;
    }

    /**
     * 显示菜单
     */
    private void showMenu() {
        System.out.println("Menu");
        System.out.println("[1] register  <username> <password>");
        System.out.println("[2] add <username> <password> <title> <start time> <end time> (format of time: 2021-11-20 12:00:01)");
        System.out.println("[3] search <username> <password> <start time> <end time>");
        System.out.println("[4] delete <username> <password> <project id>");
        System.out.println("[5] clear <username> <password> ");
        System.out.println("[6] quit");
        System.out.println("Your command:");
    }
}
