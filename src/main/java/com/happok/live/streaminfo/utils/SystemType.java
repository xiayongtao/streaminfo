package com.happok.live.streaminfo.utils;

public class SystemType {
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0;
    }
}
