package com.happok.live.streaminfo.dao.record;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.config.FFmpegConfig;
import com.happok.live.streaminfo.controller.result.RestResult;
import com.happok.live.streaminfo.record.FFmpegManager;
import com.happok.live.streaminfo.record.FFmpegManagerImpl;
import com.happok.live.streaminfo.service.record.CommandAssembly;
import com.happok.live.streaminfo.service.record.CommandAssemblyRecord;
import com.happok.live.streaminfo.utils.CreateFileUtil;
import com.happok.live.streaminfo.utils.DeleteFileUtil;
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
    private RestResult restResult = null;

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

    public Object Start(Integer Id, String srcUrl) {

        JSONObject result;
        JSONObject data = new JSONObject(true);
        JSONObject value = records.get(Id);
        if (null != value) {
            return restResult.getExistd();
        }


        String recordPath = ffConfig.getRoot() + ffConfig.getRecordPath() + "/" + Integer.toString(Id);

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

            result = restResult.getSuccess();
            data.put("id", Id);
            data.put("fileName", processId);
            data.put("input", srcUrl);
            data.put("output", ffConfig.getRecordPath() + "/" + Integer.toString(Id));
            result.put("data", data);

            records.put(Id, data);
        } else {
            result = restResult.getInternalError();
        }

        return result;
    }

    public Object Stop(Integer id) {

        JSONObject result;
        JSONObject value = records.get(id);
        if (null != value) {

            if (!manager.stop(value.getString("fileName"))) {
                return restResult.getInternalError();
            }

            records.remove(id);
            String fileName = ffConfig.getRoot() + "/" + value.getString("output") + "/" + value.getString("fileName") + ".flv";
            String mp4FileName = FFmpegUtil.Flv2Mp4(fileName);
            if (FFmpegUtil.Mp4Box(mp4FileName)) {

                result = restResult.getSuccess();
                JSONObject data = new JSONObject(true);
                data.put("fileName", mp4FileName);
                result.put("data", data);
            } else {

                result = restResult.getInternalError();
            }

            return result;
        }

        result = restResult.getNotExist();
        return result;
    }

    public Object getRecords() {
        JSONObject result = null;
        JSONArray data = new JSONArray();

        for (JSONObject obj : records.values()) {
            data.add(obj);
        }

        result = restResult.getSuccess();
        result.put("records", data);

        return result;
    }

    public Object getRecord(Integer id) {

        JSONObject result = null;

        JSONObject value = records.get(id);
        if (!value.isEmpty()) {
            result = restResult.getSuccess();
            result.put("record", value);
        } else {
            result = restResult.getNotExist();
        }

        return result;
    }

    public Object RemoveFile(Integer id) {
        JSONObject reslut;

        String recordPath = ffConfig.getRoot() + ffConfig.getRecordPath() + "/" + Integer.toString(id);
        if (DeleteFileUtil.deleteDirectory(recordPath)) {
            reslut = restResult.getSuccess();
        } else {
            reslut = restResult.getNotExist();
        }

        return reslut;
    }
}

