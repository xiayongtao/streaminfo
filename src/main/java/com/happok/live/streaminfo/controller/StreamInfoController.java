package com.happok.live.streaminfo.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.service.StreamInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/")

public class StreamInfoController {

    @Autowired
    private StreamInfoService streamInfoService = null;

    @RequestMapping(value = "streams/count", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getServerUsers(@RequestBody String servers) {

        JSONObject jsonResult = JSONObject.parseObject(servers);
        JSONArray ips = jsonResult.getJSONArray("servers");

        return streamInfoService.getServerUsers(ips);
    }

    @PostMapping("stream/count")
    public Object getWatchUsers(@RequestBody String body) {

        JSONObject jsonResult = JSONObject.parseObject(body);
        JSONArray ips = jsonResult.getJSONArray("servers");
        JSONArray names = jsonResult.getJSONArray("streams");

        return streamInfoService.getWatchUsers(ips, names);
    }
}
