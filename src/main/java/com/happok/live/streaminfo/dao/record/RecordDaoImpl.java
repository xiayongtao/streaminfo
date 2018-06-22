package com.happok.live.streaminfo.dao.record;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.config.FFmpegConfig;
import com.happok.live.streaminfo.controller.result.RestResult;
import com.happok.live.streaminfo.entity.record.RecordEntity;
import com.happok.live.streaminfo.utils.FFmpegUtil;
import com.happok.live.streaminfo.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

@Repository
@Component
public class RecordDaoImpl implements RecordDao {

    @Resource
    private FFmpegConfig ffConfig = null;
    @Resource
    private RestResult restResult = null;

    private Map<String, RecordEntity> mapRecordEntity = new HashMap<String, RecordEntity>();

    private static Logger LogUtil = LoggerFactory.getLogger(RecordDaoImpl.class);

    public Object Start(String dirName, String srcUrl) {

        RecordEntity recordEntity = mapRecordEntity.get(dirName);
        if (null != recordEntity) {
            LogUtil.warn("dirName:" + dirName + "is Existd");
            return restResult.getExistd();
        }

        recordEntity = new RecordEntity();
        recordEntity.setDirName(dirName);
        recordEntity.setSrcUrl(srcUrl);

        Object res = recordEntity.Start();
        if (null != res) {
            JSONObject result = restResult.getSuccess();
            JSONObject data = (JSONObject) res;
            result.put("data", data);

            mapRecordEntity.put(dirName, recordEntity);
            return result;
        }

        return restResult.getInternalError();
    }

    public Object Stop(String dirName) {

        RecordEntity recordEntity = mapRecordEntity.get(dirName);
        if (null == recordEntity) {
            return restResult.getNotExist();
        }

        JSONObject res = restResult.getSuccess();
        if (recordEntity.Stop()) {
            res.put("data", getFileList(dirName));
        }

        mapRecordEntity.remove(dirName);
        return res;
    }

    public Object getRecords() {

        JSONArray data = new JSONArray();
        for (String key : mapRecordEntity.keySet()) {
            RecordEntity recordEntity = mapRecordEntity.get(key);
            data.add(getFileList(recordEntity.getDirName()));
        }
        JSONObject result = restResult.getSuccess();
        result.put("data", data);
        return result;
    }

    public Object getRecord(String dirName) {

        JSONObject result = restResult.getSuccess();
        result.put("data", getFileList(dirName));
        return result;
    }

    public Object RemoveAllFiles() {
        JSONObject reslut = restResult.getSuccess();
        for (String key : mapRecordEntity.keySet()) {
            RecordEntity recordEntity = mapRecordEntity.get(key);
            DeleteFiles(recordEntity.getDirName());
        }

        return reslut;
    }

    public Object RemoveFiles(String dirName) {
        JSONObject reslut = restResult.getSuccess();
        reslut.put("data", DeleteFiles(dirName));
        return reslut;
    }

    public Object RemoveFile(String dirName, String fileName) {
        JSONObject reslut = restResult.getSuccess();
        reslut.put("data", DeleteFile(dirName, fileName));
        return reslut;
    }

    public Object getRecordStatus(String dirName) {

        JSONObject reslut = restResult.getSuccess();
        RecordEntity recordEntity = mapRecordEntity.get(dirName);
        if (null == recordEntity) {
            return restResult.getNotExist();
        }

        return reslut;
    }

    private JSONArray getFileList(String dirName) {
        JSONArray fileList = new JSONArray();
        List<String> files = FileUtil.getFiles(ffConfig.getRoot() + "/" + ffConfig.getRecordPath() + "/" + dirName, "mp4");
        for (String file : files) {

            JSONObject objfile = new JSONObject(true);
            objfile.put("size", FileUtil.getFileSize(file));

            //getFileDuration
            objfile.put("duration", FFmpegUtil.getFileDuration(file));

            String newFile = file.replaceAll("\\\\", "/");
            file = newFile.replace(ffConfig.getRoot(), "");
            objfile.put("name", file);
            System.out.println("fileName:" + file + " rootPat:" + ffConfig.getRoot());
            fileList.add(objfile);
        }

        return fileList;
    }

    private boolean DeleteFile(String dirName, String fileName) {
        return FileUtil.deleteFile(ffConfig.getRoot() + "/" + ffConfig.getRecordPath() + "/" + dirName + "/" + fileName);
    }

    private boolean DeleteFiles(String dirName) {
        return FileUtil.deleteDirectory(ffConfig.getRoot() + "/" + ffConfig.getRecordPath() + "/" + dirName + "/");
    }

    private void ReStart() {
        for (String key : mapRecordEntity.keySet()) {
            RecordEntity recordEntity = mapRecordEntity.get(key);
            if (null != recordEntity) {
                recordEntity.Stop();
                recordEntity.Start();
            }
        }
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    public void subsection() {
        ReStart();
    }

    @Scheduled(cron = "*/10 * * * * ?")
    public void check() {

        for (String key : mapRecordEntity.keySet()) {
            RecordEntity recordEntity = mapRecordEntity.get(key);
            if (null != recordEntity) {
                if (!recordEntity.Chekc()) {
                    recordEntity.Stop();
                    recordEntity.Start();
                }
            }
        }
    }
}

