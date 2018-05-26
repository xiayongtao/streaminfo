package com.happok.live.streaminfo.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.config.SrsConfig;
import com.happok.live.streaminfo.entity.ServerUser;
import com.happok.live.streaminfo.entity.SrsServer;
import com.happok.live.streaminfo.entity.StreamName;
import com.happok.live.streaminfo.entity.WatchUser;
import com.happok.live.streaminfo.entity.srs.Kbps;
import com.happok.live.streaminfo.entity.srs.Vhost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class SrsStreamInfo implements IStreamInfo {

    @Autowired
    private RestTemplate restTemplate = null;

    @Autowired
    private SrsConfig srsConfig = null;

    private JSONObject getResult(String ip, JSONObject body) {

        Integer watchCount = 0;
        Integer pushCount = 0;
        JSONObject result = new JSONObject();

        JSONArray vhosts = body.getJSONArray("vhosts");
        for (int i = 0; i < vhosts.size(); i++) {

            JSONObject obj = vhosts.getJSONObject(i);
            watchCount += obj.getInteger("clients");
            pushCount += obj.getInteger("streams");
        }

        result.put("ip", ip);
        result.put("watchCount", watchCount);
        result.put("pushCount", pushCount);
        return result;

    }


    public Object getServerUsers(List<SrsServer> servers) {

        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();


        for (SrsServer server : servers) {

            String url = srsConfig.getProtocol() + server.getIp() + ":" + srsConfig.getPort() + srsConfig.getPrefix();
            url += "vhosts";

            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            JSONObject body = JSONObject.parseObject(responseEntity.getBody());

            result.put("code", responseEntity.getStatusCode());
            data.add(getResult(server.getIp(), body));

        }

        result.put("data", data);

        return result;
    }

    public Object getWatchUsers(List<SrsServer> servers, List<StreamName> streamNames) {
        return null;
    }

}
