package bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * 用于远程调用时破解对象的传输
 * @author chenjing
 */
public class Meeting implements Serializable {

    /**
     * 会议号
     */
    private int meetingID;

    /**
     * 会议名称
     */
    private String meetingTitle;
    /**
     * 会议开始时间
     */
    private Date meetingStartTime;

    /**
     * 会议结束时间
     */
    private Date meetingEndTime;

    /**
     * 会议发起者，建立者
     */
    public User organizer;

    /**
     * 会议参与者
     */
    public ArrayList<String> participants;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 构造函数
     * @param meetingID 会议号
     * @param meetingTitle 会议标题
     * @param meetingStartTime 会议开始时间
     * @param meetingEndTime 会议结束时间
     * @param organizer 会议组织者
     * @param participants 会议参与者
     */
    public Meeting (int meetingID,String meetingTitle,Date meetingStartTime,
                    Date meetingEndTime,User organizer,ArrayList<String> participants) {
        this.meetingID = meetingID;
        this.meetingTitle = meetingTitle;
        this.meetingStartTime = meetingStartTime;
        this.meetingEndTime = meetingEndTime;
        this.participants = participants;
        this.organizer = organizer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return meetingID == meeting.meetingID && Objects.equals(meetingTitle, meeting.meetingTitle) && Objects.equals(meetingStartTime, meeting.meetingStartTime) && Objects.equals(meetingEndTime, meeting.meetingEndTime) && Objects.equals(organizer, meeting.organizer) && Objects.equals(participants, meeting.participants);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "meetingID=" + meetingID +
                ", meetingTitle='" + meetingTitle + '\'' +
                ", meetingStartTime=" + meetingStartTime +
                ", meetingEndTime=" + meetingEndTime +
                ", organizer=" + organizer +
                ", participants=" + participants +
                '}';
    }

    public int getMeetingID() {
        return meetingID;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public Date getMeetingStartTime() {
        return meetingStartTime;
    }

    public Date getMeetingEndTime() {
        return meetingEndTime;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public User getOrganizer() {
        return organizer;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setMeetingID(int meetingID) {
        this.meetingID = meetingID;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    public void setMeetingStartTime(Date meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public void setMeetingEndTime(Date meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

}
