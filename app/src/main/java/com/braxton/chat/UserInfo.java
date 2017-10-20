package com.braxton.chat;

import java.util.ArrayList;

/**
 * Created by Braxton on 10/8/2017.
 */

public class UserInfo {
    public static String username;
    public static String phone;
    public static String online;
    public static String address;

    public static String getStatus() {
        return online;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public  void setOnline(String online) {
        this.online = online;
    }


}
