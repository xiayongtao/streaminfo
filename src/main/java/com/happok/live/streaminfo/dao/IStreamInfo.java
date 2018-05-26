package com.happok.live.streaminfo.dao;

import com.happok.live.streaminfo.entity.SrsServer;
import com.happok.live.streaminfo.entity.StreamName;


import java.util.List;

public interface IStreamInfo {
    public Object getServerUsers(List<SrsServer> servers);
    public Object getWatchUsers(List<SrsServer> servers,List<StreamName> streamNames);
}

