package com.happok.live.streaminfo.service.record;

import com.alibaba.fastjson.JSONObject;
import com.happok.live.streaminfo.dao.record.RecordDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordService {

    @Autowired
    private RecordDaoImpl recordDao = null;

    public Object Star(JSONObject body) {

        return  recordDao.Start(body.getInteger("id"),body.getString("srcUrl"));
    }

    public Object Stop(Integer id) {

        return recordDao.Stop(id);
    }

    public Object getRecords() {

        return recordDao.getRecords();
    }

    public Object getRecord(Integer id) {

        return recordDao.getRecord(id);
    }

    public Object Delete(Integer id){
        return recordDao.RemoveFile(id);
    }
}
