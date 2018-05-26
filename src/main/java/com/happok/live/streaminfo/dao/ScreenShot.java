package com.happok.live.streaminfo.dao;

import com.happok.live.streaminfo.config.FFmpegConfig;
import com.happok.live.streaminfo.entity.Image;
import com.happok.live.streaminfo.utils.DeleteFileUtil;
import com.happok.live.streaminfo.utils.FFmpegUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ScreenShot implements IImageInterface {

    @Autowired
    private FFmpegConfig ffmpegConfig = null;

    private Image image = new Image();

    private String dirName;
    private String srcUrl;

    private String path;

    private boolean getShot() {

        path = FFmpegUtil.ScreenShot(srcUrl, dirName);

        if (null != path) {
            return true;
        } else {
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

    @Override
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

    @Override
    public boolean deleteImage(String dirName) {
        return DeleteFileUtil.deleteDirectory(ffmpegConfig.getBaseImagePath() + dirName);
    }

    public String getPath() {
        return path;
    }
}
