package com.happok.live.streaminfo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration   //组件类
@ConfigurationProperties(prefix = "ffmpeg")

public class FFmpegConfig {
    private String basePath;
    private String baseImagePath;
    private String vframes;
    private String imageSize;
    private String startTime;
    private String imageType;


    public FFmpegConfig() {
    }


    public String getBaseImagePath() {
        return baseImagePath;
    }

    public void setBaseImagePath(String baseImagePath) {
        this.baseImagePath = baseImagePath;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getVframes() {
        return vframes;
    }

    public void setVframes(String vframes) {
        this.vframes = vframes;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
