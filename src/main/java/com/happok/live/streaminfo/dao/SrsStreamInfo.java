package com.happok.live.streaminfo.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.config.SrsConfig;
import com.happok.live.streaminfo.record.FFmpegManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


@Repository
public class SrsStreamInfo implements IStreamInfo {

    private static Logger LogUtil = LoggerFactory.getLogger(FFmpegManagerImpl.class);


    @Resource
    private RestTemplate restTemplate = null;

    @Resource
    private SrsConfig srsConfig = null;


    private JSONObject getStreamsResult(String ip, JSONObject body) {

        Integer watchCount = 0;
        Integer pushCount = 0;
        Integer sendKbps = 0;
        Integer recvKbps = 0;

        JSONObject result = new JSONObject(true);

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
        result.put("sendKbps", sendKbps);
        result.put("pushCount", pushCount);
        result.put("recvKbps", recvKbps);
        return result;

    }


    public Object getServerUsers(JSONArray servers) {

        JSONArray data = new JSONArray();

        for (int i = 0; i < servers.size(); i++) {

            JSONObject server = servers.getJSONObject(i);
            String ip = server.get("ip").toString();

            String url = srsConfig.getProtocol() + ip + ":" + srsConfig.getPort() + srsConfig.getPrefix();
            url += "vhosts";

            try {
                ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
                JSONObject body = JSONObject.parseObject(responseEntity.getBody());

                data.add(getStreamsResult(ip, body));
            }catch (RestClientException e){
                LogUtil.error(e.toString());
            }
        }

        return data;
    }

    private JSONObject getStreamResult(JSONArray servers, String streamName) {

        JSONObject result = new JSONObject(true);
        Integer watchCount = 0;
        Integer sendKbps = 0;
        Integer recvKbps = 0;

        for (int i = 0; i < servers.size(); i++) {
            JSONObject server = servers.getJSONObject(i);
            String ip = server.get("ip").toString();

            String url = srsConfig.getProtocol() + ip + ":" + srsConfig.getPort() + srsConfig.getPrefix();
            url += "streams";
            try {
                ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
                JSONObject body = JSONObject.parseObject(responseEntity.getBody());

                JSONArray streams = body.getJSONArray("streams");
                for (int j = 0; j < streams.size(); j++) {

                    JSONObject stream = streams.getJSONObject(j);
                    if (stream.getInteger("id") != -1 && streamName.equals(stream.getString("name"))) {
                        System.out.println("stream.toJSONString() = " + stream.toJSONString());

                        JSONObject publish = stream.getJSONObject("publish");
                        if (publish.getBoolean("active")) {
                            JSONObject kbps = stream.getJSONObject("kbps");
                            sendKbps += kbps.getInteger("send_30s");
                            recvKbps += kbps.getInteger("recv_30s");
                            watchCount += stream.getInteger("clients");
                        }

                        break;
                    }
                }

                result.put("name", streamName);
                result.put("watchCount", watchCount);
                result.put("sendKbps", sendKbps);
                result.put("recvKbps", recvKbps);
            }catch (RestClientException e){
                LogUtil.error(e.toString());
            }

            return result;
        }

        return null;
    }

    public Object getWatchUsers(JSONArray servers, JSONArray streamNames) {

        JSONArray data = new JSONArray();
        for (int j = 0; j < streamNames.size(); j++) {

            JSONObject name = streamNames.getJSONObject(j);
            String streanName = name.getString("name");
            JSONObject result = getStreamResult(servers, streanName);
            data.add(result);
        }

        return data;
    }
}
