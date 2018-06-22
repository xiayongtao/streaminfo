package com.happok.live.streaminfo.service;

import com.happok.live.streaminfo.dao.IImageInterface;
import com.happok.live.streaminfo.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShotService {

    @Autowired
    private IImageInterface iImageInterface = null;

    public Image getScreenShot(String dirName, String srcUrl){
        return iImageInterface.getImage(dirName,srcUrl);
    }

    public boolean deleteAllScreenShot(String dirName){
        return iImageInterface.deleteAllImage(dirName);
    }
    public boolean deleteScreenShot(String dirname,String filename){
        return iImageInterface.deleteImage(dirname,filename);
    }
}
