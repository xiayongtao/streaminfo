package com.happok.live.streaminfo.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.config.SrsConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


@Repository
public class SrsStreamInfo implements IStreamInfo {

    @Resource
    private RestTemplate restTemplate = null;

    @Resource
    private SrsConfig srsConfig = null;

    private JSONObject getResult(String ip, JSONObject body) {

        Integer watchCount = 0;
        Integer pushCount = 0;
        Integer sendKbps = 0;
        Integer recvKbps = 0;

        JSONObject result = new JSONObject();

        JSONArray vhosts = body.getJSONArray("vhosts");
        for (int i = 0; i < vhosts.size(); i++) {

            JSONObject obj = vhosts.getJSONObject(i);
            watchCount += obj.getInteger("clients");
            pushCount += obj.getInteger("streams");

            JSONObject kbps = obj.getJSONObject("kbps");
            sendKbps += kbps.getInteger("recv_30s");
            recvKbps += kbps.getInteger("send_30s");

        }

        result.put("ip", ip);
        result.put("watchCount", watchCount);
        result.put("pushCount", pushCount);
        result.put("sendKbps", sendKbps);
        result.put("recvKbps", recvKbps);
        return result;

    }


    public Object getServerUsers(JSONArray servers) {

        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();


        for (int i = 0; i < servers.size(); i++) {

            JSONObject server = servers.getJSONObject(i);
            String ip = server.get("ip").toString();

            String url = srsConfig.getProtocol() + ip + ":" + srsConfig.getPort() + srsConfig.getPrefix();
            url += "vhosts";

            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            JSONObject body = JSONObject.parseObject(responseEntity.getBody());

            result.put("code", responseEntity.getStatusCode());
            data.add(getResult(ip, body));
        }

        result.put("data", data);

        return result;
    }

    public Object getWatchUsers(JSONArray servers, JSONArray streamNames) {
        return null;
    }

}
