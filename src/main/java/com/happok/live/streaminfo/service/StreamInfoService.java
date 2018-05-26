package com.happok.live.streaminfo.service;

import com.happok.live.streaminfo.dao.SrsStreamInfo;
import com.happok.live.streaminfo.entity.ServerUser;
import com.happok.live.streaminfo.entity.SrsServer;
import com.happok.live.streaminfo.entity.StreamName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StreamInfoService {

    @Autowired
    private SrsStreamInfo srsStreamInfo = null;

    public Object getServerUsers(List<SrsServer> servers){
        return srsStreamInfo.getServerUsers(servers);
    }
    public Object getWatchUsers(List<SrsServer> servers, List<StreamName> streamNames){
        return srsStreamInfo.getWatchUsers(servers,streamNames);
    }
}
