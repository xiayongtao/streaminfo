package com.happok.live.streaminfo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration   //组件类
@ConfigurationProperties(prefix = "ffmpeg")

public class FFmpegConfig {

    private static String basePath;
    private static String baseImagePath;

    private static String ImagePath;
    private static String vframes;
    private static String imageSize;
    private static String startTime;
    private static String imageType;

    private static String videoType;
    private static boolean debug;
    private static String record;

    private static String path;
    private static Integer size;

    public static String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        FFmpegConfig.videoType = videoType;
    }

    public static String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        FFmpegConfig.record = record;
    }

    public static String getPath() {
        return path;
    }

    public void setPath(String path) {
        FFmpegConfig.path = path;
    }



    public static boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        FFmpegConfig.debug = debug;
    }

    public static Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        FFmpegConfig.size = size;
    }

    public static String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        FFmpegConfig.ImagePath = imagePath;
    }

    public static String getBaseImagePath() {
        return baseImagePath;
    }

    public void setBaseImagePath(String baseImagePath) {
        FFmpegConfig.baseImagePath = baseImagePath;
    }

    public static String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        FFmpegConfig.basePath = basePath;
    }

    public static String getVframes() {
        return vframes;
    }

    public void setVframes(String vframes) {
        FFmpegConfig.vframes = vframes;
    }

    public static String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        FFmpegConfig.imageSize = imageSize;
    }

    public static String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        FFmpegConfig.startTime = startTime;
    }

    public static String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        FFmpegConfig.imageType = imageType;
    }
}
