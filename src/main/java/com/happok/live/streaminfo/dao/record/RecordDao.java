package com.happok.live.streaminfo.dao.record;

import com.alibaba.fastjson.JSONObject;

public interface RecordDao {
    public JSONObject Start(Integer Id, String srcUrl);

    public JSONObject Stop(Integer id);

    public JSONObject getRecords();

    public JSONObject getRecord(Integer id);

    public JSONObject RemoveFile(Integer id);
}
