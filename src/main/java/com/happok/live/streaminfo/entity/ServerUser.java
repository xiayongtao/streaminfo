package com.happok.live.streaminfo.entity;

public class ServerUser {
    private String ip;
    private Integer count;

    public ServerUser() {
    }

    public String getServerIp() {
        return ip;
    }

    public void setServerIp(String serverIp) {
        this.ip = serverIp;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
