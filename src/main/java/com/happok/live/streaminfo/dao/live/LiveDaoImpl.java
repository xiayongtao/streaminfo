package com.happok.live.streaminfo.dao.live;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.config.SrsConfig;
import com.happok.live.streaminfo.controller.result.RestResult;
import com.happok.live.streaminfo.entity.live.LiveEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Repository
@Component
public class LiveDaoImpl implements LiveDao {

    @Autowired
    private SrsConfig srsConfig;
    @Autowired
    private RestResult restResult;
    @Autowired
    private RestTemplate restTemplate;


    private Map<String, LiveEntity> mapEntty = new HashMap<String, LiveEntity>();

    private void setConfig(LiveEntity liveEntity) {
        liveEntity.setRestResult(restResult);
        liveEntity.setRestTemplate(restTemplate);
        liveEntity.setSrsConfig(srsConfig);
    }

    public Object Start(String srcUrl, String ip, String port, String name) {

        if (null != mapEntty.get(name)) {
            return restResult.getExistd();
        }

        LiveEntity liveEntity = new LiveEntity();

        liveEntity.setPort(port);
        liveEntity.setSrcUrl(srcUrl);
        liveEntity.setIp(ip);
        liveEntity.setName(name);

        setConfig(liveEntity);

        Object rs = liveEntity.Start();
        if (null != rs) {
            JSONObject result = restResult.getSuccess();
            result.put("data", rs);

            mapEntty.put(name, liveEntity);

            return result;
        }

        return restResult.getInternalError();
    }

    public Object Stop(String name) {

        LiveEntity liveEntity = mapEntty.get(name);
        if (null != liveEntity) {
            mapEntty.remove(name);
            return liveEntity.Stop();
        }

        return restResult.getNotExist();
    }

    public Object getStreams() {
        JSONArray data = new JSONArray();

        for (String key : mapEntty.keySet()) {
            LiveEntity liveEntity = mapEntty.get(key);
            data.add(liveEntity.getStream());
        }

        return data;
    }

    public Object getStream(String name) {

        LiveEntity liveEntity = mapEntty.get(name);
        if (null != liveEntity) {
            JSONObject res = restResult.getSuccess();
            res.put("data", liveEntity.getStream());
            return res;
        }
        return restResult.getNotExist();
    }

    @Scheduled(cron = "*/10 * * * * ?")
    public void timerCheckStatus() {
        for (String key : mapEntty.keySet()) {
            LiveEntity liveEntity = mapEntty.get(key);
            if (null != liveEntity) {
                liveEntity.Chekc();
            }
        }
    }
}
