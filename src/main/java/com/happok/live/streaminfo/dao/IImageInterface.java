package com.happok.live.streaminfo.dao;

import com.happok.live.streaminfo.entity.Image;

public interface IImageInterface {

    public Image getImage(String dirName, String srcUrl);
    public boolean deleteImage(String dirName);
}
