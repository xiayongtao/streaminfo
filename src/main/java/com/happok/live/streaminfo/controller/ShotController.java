package com.happok.live.streaminfo.controller;

import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.controller.result.RestResult;
import com.happok.live.streaminfo.entity.Image;
import com.happok.live.streaminfo.service.ShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/api/")
public class ShotController {

    @Autowired
    private ShotService shotService = null;

    @Autowired
    private RestResult restResult = null;

    @PostMapping("/screenshot")
    public JSONObject getScreenShot(@RequestBody String body) {


        JSONObject jsonResult = JSONObject.parseObject(body);
        JSONObject result;

        if (null == jsonResult.getString("dirName") || null == jsonResult.getString("srcUrl")) {

            return restResult.getParamError();
        }

        Image image = shotService.getScreenShot(jsonResult.getString("dirName"), jsonResult.getString("srcUrl"));
        if (image.getPath().isEmpty()) {
            return restResult.getInternalError();
        } else {

            result = restResult.getSuccess();
            JSONObject data = new JSONObject(true);
            data.put("path", image.getPath());
            data.put("type", image.getType());
            result.put("image", data);
        }

        return result;
    }

    @DeleteMapping("/screenshot/{dirname}")
    public JSONObject deleteAllScreenShot(@PathVariable("dirname") String dirname) {

        JSONObject result;
        if (shotService.deleteAllScreenShot(dirname)) {
            result = restResult.getSuccess();
        } else {
            result = restResult.getInternalError();
        }

        return result;
    }

    @DeleteMapping("/screenshot/{dirname}/file/{filename}")
    public JSONObject deleteScreenShot(@PathVariable("dirname") String dirname, @PathVariable("filename") String filename) {

        JSONObject result;
        if (shotService.deleteScreenShot(dirname,filename)) {
            result = restResult.getSuccess();
        } else {
            result = restResult.getNotExist();
        }

        return result;
    }
}
