package com.chumfuchiu.meetingmanagersystem.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by 37266 on 2017/4/18.
 */

public class UserInfo extends BmobUser {
    //permisssion为权限标志位，1001为普通用户，1011为管理用户
    private Integer permission;

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }
}
