package com.happok.live.streaminfo.dao.live;

public interface LiveDao {
    public Object Start(String srcUrl, String ip, String port, String name);

    public Object Stop(String name);

    public Object getStreams();

    public Object getStream(String name);
}
