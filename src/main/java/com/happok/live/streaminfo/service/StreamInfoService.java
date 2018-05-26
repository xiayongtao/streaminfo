package com.happok.live.streaminfo.service;

import com.alibaba.fastjson.JSONArray;
import com.happok.live.streaminfo.dao.SrsStreamInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StreamInfoService {

    @Autowired
    private SrsStreamInfo srsStreamInfo = null;

    public Object getServerUsers(JSONArray servers){
        return srsStreamInfo.getServerUsers(servers);
    }
    public Object getWatchUsers(JSONArray servers, JSONArray streamNames){
        return srsStreamInfo.getWatchUsers(servers,streamNames);
    }
}
