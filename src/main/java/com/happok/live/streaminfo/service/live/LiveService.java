package com.happok.live.streaminfo.service.live;

import com.happok.live.streaminfo.dao.live.LiveDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LiveService {

    @Autowired
    private LiveDao liveDao = null;

    public Object Start(String srcUrl, String ip, String port, String name) {

        return liveDao.Start(srcUrl, ip, port, name);
    }

    public Object Stop(String name) {
        return liveDao.Stop(name);
    }

    public Object getStreams() {
        return liveDao.getStreams();
    }

    public Object getStream(String name) {
        return liveDao.getStream(name);
    }
}
