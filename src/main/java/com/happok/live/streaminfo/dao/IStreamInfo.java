package com.happok.live.streaminfo.dao;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface IStreamInfo {
    public Object getServerUsers(JSONArray servers);
    public Object getWatchUsers(JSONArray servers, JSONArray streamNames);
}

