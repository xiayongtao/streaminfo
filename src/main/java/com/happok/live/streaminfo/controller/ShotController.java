package com.happok.live.streaminfo.controller;

import com.happok.live.streaminfo.entity.Image;
import com.happok.live.streaminfo.service.ShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/v1/api")
public class ShotController {

    @Autowired
    private ShotService shotService = null;

    @PostMapping("/screenshot")
    public Image getScreenShot(@RequestParam(value = "dirName", required = true) String dirName,
                               @RequestParam(value = "srcUrl", required = true) String srcUrl) {


        return shotService.getScreenShot(dirName,srcUrl);
    }
}
