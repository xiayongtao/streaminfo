package com.happok.live.streaminfo.controller;

import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.entity.Image;
import com.happok.live.streaminfo.service.ShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/api/")
public class ShotController {

    @Autowired
    private ShotService shotService = null;

    @PostMapping("/screenshot")
    public JSONObject getScreenShot(@RequestParam(value = "dirName", required = true) String dirName,
                                    @RequestParam(value = "srcUrl", required = true) String srcUrl) {

        JSONObject result = new JSONObject(true);

        Image image = shotService.getScreenShot(dirName, srcUrl);
        if (image.getPath().isEmpty()) {
            result.put("codec", 1);
        } else {
            result.put("codec", 0);
            JSONObject data = new JSONObject(true);
            data.put("path", image.getPath());
            data.put("type", image.getType());
            result.put("image", data);
        }

        return result;
    }

    @DeleteMapping("/screenshot/{dirname}")
    public JSONObject deleteScreenShot(@PathVariable("dirname") String dirname) {

        JSONObject result = new JSONObject(true);
        result.put("code", shotService.deleteScreenShot(dirname)?0:1);
        return result;
    }
}
