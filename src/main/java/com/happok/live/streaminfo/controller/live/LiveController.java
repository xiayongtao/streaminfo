package com.happok.live.streaminfo.controller.live;

import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.controller.result.RestResult;
import com.happok.live.streaminfo.service.live.LiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/")
public class LiveController {

    @Autowired
    private LiveService liveService = null;

    @Autowired
    private RestResult restResult = null;

    @PostMapping("/stream")
    public Object Start(@RequestBody String body) {

        JSONObject jsonResult = JSONObject.parseObject(body);
        String ip = jsonResult.getString("ip");
        String srcUrl = jsonResult.getString("srcUrl");
        String name = jsonResult.getString("name");
        String prot = jsonResult.getString("port");
        if (null == ip || null == srcUrl || null == name) {
            return restResult.getParamError();
        }

        if (null == prot) {
            prot = "1935";
        }

        return liveService.Start(srcUrl, ip, prot, name);
    }

    @GetMapping("/streams")
    public Object getStreams() {

        JSONObject result = null;
        Object data = liveService.getStreams();

        if (null != data) {
            result = restResult.getSuccess();
            result.put("data", data);
        }

        return result;
    }

    @GetMapping("/stream/{name}")
    public Object getStream(@PathVariable("name") String name) {

        return liveService.getStream(name);
    }

    @DeleteMapping("/stream/{name}")
    public Object Stop(@PathVariable("name") String name) {

        return liveService.Stop(name);
    }

}
