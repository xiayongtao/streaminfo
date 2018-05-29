package com.happok.live.streaminfo.utils;

import com.happok.live.streaminfo.config.FFmpegConfig;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class FFConfigUtil {

    @Autowired
    private FFmpegConfig ffmpegConfigAutowired = null;
    private static FFmpegConfig ffmpegConfig;


    @PostConstruct
    public void init() {
        ffmpegConfig = this.ffmpegConfigAutowired;
    }

    public static  FFmpegConfig getConfig(){
        return ffmpegConfig;
    }
}
