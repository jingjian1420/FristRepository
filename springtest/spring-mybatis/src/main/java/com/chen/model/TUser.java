package com.chen.model;

import java.util.Date;



public class TUser {

    private Long id;

    private String username;

    private String password;

    private Date createDatime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDatime() {
        return createDatime;
    }

    public void setCreateDatime(Date createDatime) {
        this.createDatime = createDatime;
    }
}