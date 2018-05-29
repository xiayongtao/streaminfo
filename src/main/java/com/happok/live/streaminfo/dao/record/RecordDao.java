package com.happok.live.streaminfo.dao.record;

import com.alibaba.fastjson.JSONObject;

public interface RecordDao {
    public Object Start(Integer Id, String srcUrl);

    public Object Stop(Integer id);

    public Object getRecords();

    public Object getRecord(Integer id);

    public Object RemoveFile(Integer id);
}
