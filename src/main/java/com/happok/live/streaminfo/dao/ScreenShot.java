package com.happok.live.streaminfo.dao;

import com.happok.live.streaminfo.config.FFmpegConfig;
import com.happok.live.streaminfo.entity.Image;
import com.happok.live.streaminfo.record.FFmpegManager;
import com.happok.live.streaminfo.record.FFmpegManagerImpl;
import com.happok.live.streaminfo.service.record.CommandAssembly;
import com.happok.live.streaminfo.service.record.CommandAssemblyShot;
import com.happok.live.streaminfo.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Repository
public class ScreenShot implements IImageInterface {

    @Autowired
    private FFmpegConfig ffmpegConfig = null;

    private static Logger LogUtil = LoggerFactory.getLogger(FFmpegManagerImpl.class);

    private FFmpegManager manager = null;
    private CommandAssembly commandAssembly = null;

    private Image image = new Image();

    private String dirName;
    private String srcUrl;

    private String path;

    public ScreenShot() {
        commandAssembly = new CommandAssemblyShot();
        manager = new FFmpegManagerImpl(commandAssembly);
    }

    private boolean getShot() {

        Map<String, String> cmmondmap = new HashMap<String, String>();

        long nowData = new Date().getTime();
        String ImagePath = ffmpegConfig.getImagePath();
        String baseImagePath = ffmpegConfig.getRoot();
        String filePath = ImagePath + "/" + dirName + "/";
        String fileName = Long.toString(nowData) + "." + ffmpegConfig.getImageType();

        if (!FileUtil.createDir(baseImagePath + "/" + filePath)) {
            LogUtil.warn("创建目录" + filePath + " 目标目录已经存在");
        }

        cmmondmap.put("appName", Long.toString(nowData));
        cmmondmap.put("input", srcUrl);
        cmmondmap.put("output", baseImagePath + "/" + filePath + fileName);

        String processId = manager.start(cmmondmap);

        if (null != processId) {
            path = filePath + fileName;
            return true;
        }

        return false;
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
        return FileUtil.deleteDirectory(ffmpegConfig.getRoot() + "/" + ffmpegConfig.getImagePath() + "/" + dirName);
    }

    public String getPath() {
        return path;
    }
}
