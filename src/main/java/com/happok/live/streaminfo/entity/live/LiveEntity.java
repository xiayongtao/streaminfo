package com.happok.live.streaminfo.entity.live;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.config.SrsConfig;
import com.happok.live.streaminfo.controller.result.RestResult;
import com.happok.live.streaminfo.record.FFmpegManager;
import com.happok.live.streaminfo.record.FFmpegManagerImpl;
import com.happok.live.streaminfo.service.record.CommandAssembly;
import com.happok.live.streaminfo.service.record.CommandAssemblyLive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Repository
public class LiveEntity {

    private String srcUrl;
    private String name;
    private String ip;
    private String port;

    private SrsConfig srsConfig;
    private RestResult restResult;
    private RestTemplate restTemplate;

    private static Logger LogUtil = LoggerFactory.getLogger(FFmpegManagerImpl.class);


    public String getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public SrsConfig getSrsConfig() {
        return srsConfig;
    }

    public void setSrsConfig(SrsConfig srsConfig) {
        this.srsConfig = srsConfig;
    }

    public RestResult getRestResult() {
        return restResult;
    }

    public void setRestResult(RestResult restResult) {
        this.restResult = restResult;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    private FFmpegManager manager = new FFmpegManagerImpl();
    private CommandAssembly commandAssembly = new CommandAssemblyLive();


    private Map<String, String> getCmd(String srcUrl, String name, String ip, String port) {
        Map<String, String> cmd = new HashMap<String, String>();
        manager.setCommandAssembly(commandAssembly);


        cmd.put("appName", name);
        cmd.put("input", srcUrl);
        cmd.put("output", "rtmp://" + ip + ":" + port + "/live/");

        return cmd;
    }

    private JSONObject getResult() {

        JSONObject result = new JSONObject(true);
        String url = srsConfig.getProtocol() + ip + ":" + srsConfig.getPort() + srsConfig.getPrefix();
        url += "streams";

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        JSONObject body = JSONObject.parseObject(responseEntity.getBody());

        JSONArray streams = body.getJSONArray("streams");
        for (int j = 0; j < streams.size(); j++) {
            JSONObject stream = streams.getJSONObject(j);
            Integer streamId = stream.getInteger("id");

            if (streamId != -1 && name.equals(stream.getString("name"))) {

                result.put("id", streamId);
                result.put("name", name);
                JSONObject publish = stream.getJSONObject("publish");
                if (publish.getBoolean("active")) {

                    result.put("status", true);
                    return result;
                } else {
                    result.put("status", false);
                }
            }
        }

        return result;
    }

    public Object Start() {

        JSONObject result = new JSONObject(true);
        Map<String, String> cmd = getCmd(srcUrl, name, ip, port);
        String taskName = manager.start(cmd);
        if (null != taskName) {
            String dstUrl = cmd.get("output") + cmd.get("appName");
            result.put("dstUrl", dstUrl);
            return result;
        }

        return null;
    }

    public Object Stop() {
        manager.stop(name);
        return restResult.getSuccess();
    }

    public Object getStream() {
        return getResult();
    }


    public void Chekc() {
        JSONObject res = getResult();
        if (!res.getBoolean("status")) {
            Stop();
            Start();
            LogUtil.warn("name:" + name + " is status:false");
        }
    }
}
