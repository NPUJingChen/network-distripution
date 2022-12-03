package bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author chenjing
 */
public class Project implements Serializable {

    private int ID;

    private String title;

    private Date startTime;

    private Date endTime;

    private User user;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Project() {

    }

    public Project(int ID, String title, Date startTime, Date endTime, User user) {
        this.ID = ID;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return ID == project.ID && Objects.equals(title, project.title) && Objects.equals(startTime, project.startTime) && Objects.equals(endTime, project.endTime) && Objects.equals(user, project.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, title, startTime, endTime, user);
    }

    @Override
    public String toString() {
        return "Project{" +
                "ID=" + ID +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", user=" + user +
                '}';
    }

    public int getID() {
        return ID;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
