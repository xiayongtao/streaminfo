package com.happok.live.streaminfo.controller.live;

import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.controller.result.RestResult;
import com.happok.live.streaminfo.service.live.LiveService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "直播controller", tags = {"直播控制接口"})
@RestController
@RequestMapping("/v1/api/")
public class LiveController {

    @Autowired
    private LiveService liveService = null;

    @Autowired
    private RestResult restResult = null;

    @ApiOperation(value = "开始直播", notes = "传入源流地址和目标流地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ip", value = "目标IP", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "srcUrl", value = "源流地址", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "目标名称", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "port", value = "端口", required = true, paramType = "form", dataType = "Integer")
    })

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
