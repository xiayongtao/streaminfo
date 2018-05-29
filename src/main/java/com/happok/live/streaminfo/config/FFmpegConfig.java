package com.happok.live.streaminfo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration   //组件类
@ConfigurationProperties(prefix = "ffmpeg")

public class FFmpegConfig {

    private static String root;

    private static String imagePath;
    private static String imageType;
    private static String imageSize;
    private static String vframes;
    private static String startTime;


    private static String videoType;
    private static boolean debug;
    private static String recordPath;

    private static String path;
    private static Integer size;

    public static String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        FFmpegConfig.videoType = videoType;
    }


    public static String getPath() {
        return path;
    }

    public void setPath(String path) {
        FFmpegConfig.path = path;
    }

    public static String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public static String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public static String getRecordPath() {
        return recordPath;
    }

    public void setRecordPath(String recordPath) {
        this.recordPath = recordPath;
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
