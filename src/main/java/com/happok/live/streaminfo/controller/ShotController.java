package com.happok.live.streaminfo.controller;

import com.happok.live.streaminfo.entity.Image;
import com.happok.live.streaminfo.service.ShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/api/screenshot")
public class ShotController {

    @Autowired
    private ShotService shotService = null;

    @PostMapping("/")
    public Image getScreenShot(@RequestParam(value = "dirName", required = true) String dirName,
                               @RequestParam(value = "srcUrl", required = true) String srcUrl) {

        return shotService.getScreenShot(dirName,srcUrl);
    }

    @DeleteMapping("/{dirname}")
    public boolean deleteScreenShot(@PathVariable("dirname") String dirname){
        return shotService.deleteScreenShot(dirname);
    }
}
