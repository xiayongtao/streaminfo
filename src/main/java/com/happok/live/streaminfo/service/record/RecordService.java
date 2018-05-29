package com.happok.live.streaminfo.service.record;

import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.dao.record.RecordDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordService {

    @Autowired
    private RecordDaoImpl recordDao = null;

    public JSONObject Star(JSONObject body) {

        return  recordDao.Start(body.getInteger("id"),body.getString("srcUrl"));
    }

    public JSONObject Stop(Integer id) {

        return recordDao.Stop(id);
    }

    public JSONObject getRecords() {

        return recordDao.getRecords();
    }

    public JSONObject getRecord(Integer id) {

        return recordDao.getRecord(id);
    }

    public JSONObject Delete(Integer id){
        return recordDao.RemoveFile(id);
    }
}
