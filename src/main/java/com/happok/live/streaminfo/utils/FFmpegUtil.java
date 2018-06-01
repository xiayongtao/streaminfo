package com.happok.live.streaminfo.utils;

import com.happok.live.streaminfo.config.FFmpegConfig;
import com.happok.live.streaminfo.record.FFmpegManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class FFmpegUtil {

    private static Logger LogUtil = LoggerFactory.getLogger(FFmpegManagerImpl.class);


    @Autowired
    private FFmpegConfig ffmpegConfigAutowired = null;
    private static FFmpegConfig ffmpegConfig = null;

    @PostConstruct
    public void init() {
        ffmpegConfig = this.ffmpegConfigAutowired;
    }

    private static boolean execCmd(List<String> commands) {
        /*try {

            StringBuffer sb = new StringBuffer();
            for (String tmp : commands) {
                sb.append(tmp + " ");
            }

            Runtime run = Runtime.getRuntime();
            LogUtil.debug("start" + sb.toString());
            Process process = run.exec(sb.toString());
            LogUtil.debug("end");
            Integer code = process.exitValue();
            LogUtil.error("视频截图失败！" + Integer.toString(code));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("视频截图失败！" + e.toString());
            return false;
        }*/

        try {
            StringBuffer sb = new StringBuffer();
            for (String tmp : commands) {
                sb.append(tmp + " ");
            }


            final Process process = Runtime.getRuntime().exec(sb.toString());
            LogUtil.info("start run cmd=" + sb.toString());

            //处理InputStream的线程
            new Thread() {
                @Override
                public void run() {
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = null;

                    try {
                        while ((line = in.readLine()) != null) {
                            LogUtil.info("output: " + line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String line = null;

                    try {
                        while ((line = err.readLine()) != null) {
                            LogUtil.info("err: " + line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            err.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            process.waitFor();
            LogUtil.info("finish run cmd=" + sb.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String ScreenShot(String srcUrl, String dirName) {
/*
        long nowData = new Date().getTime();
        String basePath = ffmpegConfig.getBasePath();
        String ImagePath = ffmpegConfig.getImagePath();
        String baseImagePath = ffmpegConfig.getBaseImagePath();
        String filePath = ImagePath + "/" + dirName + "/";


        if (!CreateFileUtil.createDir(baseImagePath + "/" + filePath)) {
            LogUtil.warn("创建目录" + filePath + " 目标目录已经存在");
        }

        String fileName = filePath + Long.toString(nowData) + "." + ffmpegConfig.getImageType();

        List<String> commands = new ArrayList<String>();
        commands.add(basePath + "ffmpeg");
        //commands.add("-ss");
        //commands.add(ffmpegConfig.getStartTime());//这个参数是设置截取视频多少秒时的画面
        commands.add("-i");
        //commands.add("\"" + srcUrl + "  live=1 buffer=0 " + "\"");
        commands.add(srcUrl);
        commands.add("-y");
        commands.add("-f");
        commands.add("image2");
        commands.add("-vframes");
        commands.add(ffmpegConfig.getVframes());
       // commands.add("-s");
        //commands.add(ffmpegConfig.getImageSize());
        commands.add(baseImagePath + "/" + fileName);

        if (execCmd(commands)) {
            return fileName;
        }

*/
        return null;

    }

    public static String Flv2Mp4(String flvFile) {
        String basePath = ffmpegConfig.getPath();
        String fileName = flvFile.substring(0, flvFile.lastIndexOf("."));
        String mp4File = fileName + ".mp4";

        List<String> commands = new ArrayList<String>();
        commands.add(basePath + "ffmpeg");
        commands.add("-i");
        commands.add(flvFile);
        commands.add("-vcodec copy -acodec copy");
        commands.add("-y");
        commands.add(mp4File);

        if (execCmd(commands)) {
            FileUtil.deleteFile(flvFile);
            return mp4File;
        }

        return null;
    }

    public static boolean Mp4Box(String mp4File) {

        String basePath = ffmpegConfig.getPath();
        List<String> commands = new ArrayList<String>();
        commands.add(basePath + "MP4Box");
        commands.add("-isma");
        commands.add(mp4File);

        return execCmd(commands);
    }
}
