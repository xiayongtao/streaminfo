package com.happok.live.streaminfo.dao.record;

public interface RecordDao {
    public Object Start(String dirName, String srcUrl);

    public Object Stop(String dirName);

    public Object getRecords();

    public Object getRecord(String dirName);

    public Object RemoveFile(String dirName, String fileName);

    public Object RemoveFiles(String dirName);

    public Object RemoveAllFiles();

    public Object getRecordStatus(String dirName);
}
