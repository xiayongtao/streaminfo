package com.happok.live.streaminfo.service.record;

import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.dao.record.RecordDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordService {

    @Autowired
    private RecordDaoImpl recordDao = null;

    public Object Star(String dirName, String srcUrl) {

        return  recordDao.Start(dirName,srcUrl);
    }

    public Object Stop(String dirName) {

        return recordDao.Stop(dirName);
    }

    public Object getRecords() {

        return recordDao.getRecords();
    }

    public Object getRecord(String dirName) {

        return recordDao.getRecord(dirName);
    }

    public Object getRecordStatus(String dirName){
        return  recordDao.getRecordStatus(dirName);
    }

    public Object RemoveFile(String dirName, String fileName){
        return recordDao.RemoveFile(dirName,fileName);
    }

    public Object RemoveFiles(String dirName){
        return recordDao.RemoveFiles(dirName);
    }

    public Object RemoveAllFiles(){
        return recordDao.RemoveAllFiles();
    }
}
