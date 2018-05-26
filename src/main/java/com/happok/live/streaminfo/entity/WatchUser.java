package com.happok.live.streaminfo.entity;

public class WatchUser {

    private String StreamName;
    private Integer count;

    public WatchUser() {
    }

    public String getStreamName() {
        return StreamName;
    }

    public void setStreamName(String streamName) {
        StreamName = streamName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
