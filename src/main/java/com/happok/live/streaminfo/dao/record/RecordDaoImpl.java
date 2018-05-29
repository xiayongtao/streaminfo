package com.happok.live.streaminfo.dao.record;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.config.FFmpegConfig;
import com.happok.live.streaminfo.record.FFmpegManager;
import com.happok.live.streaminfo.record.FFmpegManagerImpl;
import com.happok.live.streaminfo.service.record.CommandAssembly;
import com.happok.live.streaminfo.service.record.CommandAssemblyRecord;
import com.happok.live.streaminfo.utils.CreateFileUtil;
import com.happok.live.streaminfo.utils.DeleteFileUtil;
import com.happok.live.streaminfo.utils.FFConfigUtil;
import com.happok.live.streaminfo.utils.FFmpegUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class RecordDaoImpl implements RecordDao {

    @Resource
    private FFmpegConfig ffConfig = null;
    private static Logger LogUtil = LoggerFactory.getLogger(RecordDaoImpl.class);
    private CommandAssembly commandAssembly = null;
    private FFmpegManager manager = null;


    Map<String, String> cmmondmap = new HashMap<String, String>();
    Map<Integer, JSONObject> records = new HashMap<Integer, JSONObject>();

    public RecordDaoImpl() {
        commandAssembly = new CommandAssemblyRecord();
        manager = new FFmpegManagerImpl(commandAssembly);
    }

    public JSONObject Start(Integer Id, String srcUrl) {

        JSONObject result = new JSONObject(true);
        JSONObject value = records.get(Id);
        if (null != value) {
            result.put("code", 208);
            return result;
        }

        String recordPath = ffConfig.getPath() + ffConfig.getRecord() + "/" + Integer.toString(Id);

        if (!CreateFileUtil.createDir(recordPath)) {
            LogUtil.warn("创建目录" + recordPath + " 目标目录已经存在");
        }

        long nowData = new Date().getTime();
        cmmondmap.put("appName", Long.toString(nowData));
        cmmondmap.put("input", srcUrl);
        cmmondmap.put("output", recordPath);
        cmmondmap.put("fmt", "flv");
        String processId = manager.start(cmmondmap);

        if (null != processId) {
            records.put(Id, result);
            result.put("code", 0);
            result.put("id", Id);
            result.put("fileName", processId);
            result.put("input", srcUrl);
            result.put("output", recordPath);
        } else {
            result.put("code", 1);
        }

        return result;
    }

    public JSONObject Stop(Integer id) {

        JSONObject result = new JSONObject(true);
        JSONObject value = records.get(id);
        if (null != value) {

            if (!manager.stop(value.getString("fileName"))) {
                result.put("code", 1);
                return result;
            }

            records.remove(id);

            String fileName = value.getString("output") + "/" + value.getString("fileName") + ".flv";
            String mp4FileName = FFmpegUtil.Flv2Mp4(fileName);
            if (FFmpegUtil.Mp4Box(mp4FileName)) {
                result.put("fileName", mp4FileName);
                result.put("code", 0);
            } else {

                result.put("code", 1);
            }

            return result;
        }

        result.put("code", 404);
        return result;
    }

    public JSONObject getRecords() {
        JSONObject result = new JSONObject(true);
        JSONArray data = new JSONArray();
        result.put("code", 0);
        for (JSONObject obj : records.values()) {
            data.add(obj);
        }

        result.put("records", data);
        return result;
    }

    public JSONObject getRecord(Integer id) {

        JSONObject result = new JSONObject(true);

        JSONObject value = records.get(id);
        if (!value.isEmpty()) {
            result.put("code", 200);
            result.put("record", value);
        } else {
            result.put("code", 404);
        }

        return result;
    }

    public JSONObject RemoveFile(Integer id) {
        JSONObject reslut = new JSONObject(true);

        String recordPath = ffConfig.getPath() + ffConfig.getRecord() + "/" + Integer.toString(id);
        if (DeleteFileUtil.deleteDirectory(recordPath)) {
            reslut.put("code", 0);
        } else {
            reslut.put("code", 1);
        }

        return reslut;
    }
}

