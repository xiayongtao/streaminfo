package com.happok.live.streaminfo.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.controller.result.RestResult;
import com.happok.live.streaminfo.controller.result.RestServiceError;
import com.happok.live.streaminfo.service.StreamInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/")

public class StreamInfoController {

    @Autowired
    private StreamInfoService streamInfoService = null;

    @Autowired
    private RestResult restResult = null;

    @RequestMapping(value = "streams/count", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object getServerUsers(@RequestBody String servers) {

        JSONObject result;
        JSONObject jsonResult = JSONObject.parseObject(servers);
        JSONArray ips = jsonResult.getJSONArray("servers");
        if (null == ips) {
            return restResult.getParamError();
        }

        Object res = streamInfoService.getServerUsers(ips);
        if (null != res) {
            result = restResult.getSuccess();
            result.put("data", res);
        } else {
            result = restResult.getInternalError();
        }

        return result;
    }

    @PostMapping("stream/count")
    public JSONObject getWatchUsers(@RequestBody String body) {

        JSONObject result;
        JSONObject jsonResult = JSONObject.parseObject(body);
        JSONArray ips = jsonResult.getJSONArray("servers");
        JSONArray names = jsonResult.getJSONArray("streams");

        if (null == ips || null == names) {
            return restResult.getParamError();
        }

        Object res = streamInfoService.getWatchUsers(ips, names);
        if (null != res) {
            result = restResult.getSuccess();
            result.put("data", res);
        } else {
            result = restResult.getInternalError();
        }

        return result;
    }
}
