package com.happok.live.streaminfo.dao;

import com.happok.live.streaminfo.config.FFmpegConfig;
import com.happok.live.streaminfo.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Repository
public class ScreenShot implements IImageInterface {

    @Autowired
    private FFmpegConfig ffmpegConfig = null;

    private Image image = new Image();

    private String dirName;
    private String srcUrl;

    private String path;

    private boolean getShot() {

        long nowData = new Date().getTime();
        String baseFFmpegPath = ffmpegConfig.getBasePath();
        path = ffmpegConfig.getBaseImagePath() + dirName + "/" + Long.toString(nowData) + "." + ffmpegConfig.getImageType();

        List<String> commands = new ArrayList<String>();
        commands.add(baseFFmpegPath);
        commands.add("-ss");
        commands.add(ffmpegConfig.getStartTime());//这个参数是设置截取视频多少秒时的画面
        commands.add("-i");
        commands.add(srcUrl);
        commands.add("-y");
        commands.add("-f");
        commands.add("image2");
        commands.add("-vframes");
        commands.add(ffmpegConfig.getVframes());
        commands.add("-s");
        commands.add(ffmpegConfig.getImageSize()); //这个参数是设置截取图片的大小
        commands.add(path);
        try {

            System.out.println(commands);
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            builder.start();

            System.out.println("截取成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    public Image getImage(String dirName, String srcUrl) {
        this.setDirName(dirName);
        this.setSrcUrl(srcUrl);

        if (!getShot()) {
            return null;
        }

        image.setPath(path);
        image.setType(ffmpegConfig.getImageType());

        return image;
    }

    public String getPath() {
        return path;
    }
}
