package com.happok.live.streaminfo.entity.record;

import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.config.FFmpegConfig;
import com.happok.live.streaminfo.dao.record.RecordDaoImpl;
import com.happok.live.streaminfo.record.FFmpegManager;
import com.happok.live.streaminfo.record.FFmpegManagerImpl;
import com.happok.live.streaminfo.service.record.CommandAssembly;
import com.happok.live.streaminfo.service.record.CommandAssemblyRecord;
import com.happok.live.streaminfo.utils.FileUtil;
import com.happok.live.streaminfo.utils.FFmpegUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.*;

public class RecordEntity {

    @Resource
    private FFmpegConfig ffConfig = null;

    private String dirName;
    private String srcUrl;
    private String appName;


    private static Logger LogUtil = LoggerFactory.getLogger(RecordDaoImpl.class);
    private CommandAssembly commandAssembly = null;
    private FFmpegManager manager = null;


    public RecordEntity() {
        commandAssembly = new CommandAssemblyRecord();
        manager = new FFmpegManagerImpl(commandAssembly);
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

    private Map<String, String> getCmd() {

        String recordPath = ffConfig.getRoot() + ffConfig.getRecordPath() + "/" + dirName;
        if (!FileUtil.createDir(recordPath)) {
            LogUtil.warn("创建目录" + recordPath + " 目标目录已经存在");
        }

        Map<String, String> cmd = new HashMap<String, String>();
        long nowData = new Date().getTime();
        appName = Long.toString(nowData);
        cmd.put("appName", appName);
        cmd.put("input", srcUrl);
        cmd.put("output", recordPath);
        cmd.put("fmt", "flv");

        return cmd;

    }

    public Object Start() {

        JSONObject result = new JSONObject(true);
        String processId = manager.start(getCmd());
        if (null != processId) {
            result.put("fileName", processId);
            result.put("output", ffConfig.getRecordPath() + "/" + dirName);
            return result;
        }

        return null;
    }

    private boolean DealRecordFile() {
        String fileName = ffConfig.getRoot() + "/" + ffConfig.getRecordPath() + "/" + dirName + "/" + appName + ".flv";
        String mp4FileName = FFmpegUtil.Flv2Mp4(fileName);
        if (!FFmpegUtil.Mp4Box(mp4FileName)) {
            return false;
        }

        return true;
    }

    public boolean Stop() {

        if (!manager.stop(appName)) {
            return false;
        }

        if (!DealRecordFile()) {
            return false;
        }
        return true;
    }
}
