package com.chumfuchiu.meetingmanagersystem.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by ChumFuchiu on 2017/5/18.
 */

public class RoomInfo extends BmobObject implements Serializable{
    private String buildingOfRoom;//教室所属楼宇（逸夫楼，机电楼，教学楼）
    private String numberOfRoom;//教室号
    private String stateOfRoom;//教室的状态，禁用，使用中，未使用
    private String resourceOfRoom;//教室所含有的资源
    private int personsOfRoom;//建议申请人数，用于模糊匹配。
    private String flag;
    //
    private String usingTime;
    private String usingReason;
    private String usingPerson;
    private String usingPersonid;
    private String usingPersonAvatar;

    public String getUsingPersonid() {
        return usingPersonid;
    }

    public void setUsingPersonid(String usingPersonid) {
        this.usingPersonid = usingPersonid;
    }

    public String getUsingPersonAvatar() {
        return usingPersonAvatar;
    }

    public void setUsingPersonAvatar(String usingPersonAvatar) {
        this.usingPersonAvatar = usingPersonAvatar;
    }

    public String getUsingTime() {
        return usingTime;
    }

    public void setUsingTime(String usingTime) {
        this.usingTime = usingTime;
    }

    public String getUsingReason() {
        return usingReason;
    }

    public void setUsingReason(String usingReason) {
        this.usingReason = usingReason;
    }

    public String getUsingPerson() {
        return usingPerson;
    }

    public void setUsingPerson(String usingPerson) {
        this.usingPerson = usingPerson;
    }

    //get and set方法。
    public String getBuildingOfRoom() {
        return buildingOfRoom;
    }

    public void setBuildingOfRoom(String buildingOfRoom) {
        this.buildingOfRoom = buildingOfRoom;
    }

    public String getNumberOfRoom() {
        return numberOfRoom;
    }

    public void setNumberOfRoom(String numberOfRoom) {
        this.numberOfRoom = numberOfRoom;
    }

    public String getStateOfRoom() {
        return stateOfRoom;
    }

    public void setStateOfRoom(String stateOfRoom) {
        this.stateOfRoom = stateOfRoom;
    }

    public String getResourceOfRoom() {
        return resourceOfRoom;
    }

    public void setResourceOfRoom(String resourceOfRoom) {
        this.resourceOfRoom = resourceOfRoom;
    }

    public int getPersonsOfRoom() {
        return personsOfRoom;
    }

    public void setPersonsOfRoom(int personsOfRoom) {
        this.personsOfRoom = personsOfRoom;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
