package com.happok.live.streaminfo.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.entity.ServerUser;
import com.happok.live.streaminfo.entity.SrsServer;
import com.happok.live.streaminfo.service.StreamInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/api/")

public class StreamInfoController {

    @Autowired
    private StreamInfoService streamInfoService = null;

    //@PostMapping("streams/count",produces = "application/json;charset=UTF-8")

    @RequestMapping(value = "streams/count", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getServerUsers(@RequestBody String servers) {


        JSONObject jsonResult = JSONObject.parseObject(servers);

        JSONArray ips = jsonResult.getJSONArray("servers");
        List<SrsServer> serIPs = ips.toJavaList(SrsServer.class);

        return streamInfoService.getServerUsers(serIPs);
    }

    @PostMapping("stream/count")
    public Object getWatchUsers(@RequestParam(value = "servers") List<String> servers,
                                @RequestParam(value = "streamNames") List<String> streamNames) {
       // return streamInfoService.getWatchUsers(servers, streamNames);
        return  null;
    }
}
