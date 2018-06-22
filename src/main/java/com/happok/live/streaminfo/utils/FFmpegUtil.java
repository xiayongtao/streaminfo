package com.happok.live.streaminfo.utils;

import com.alibaba.fastjson.JSON;
import com.happok.live.streaminfo.config.FFmpegConfig;
import com.happok.live.streaminfo.record.FFmpegManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FFmpegUtil {

    private static  String fileSize;

    private static Logger LogUtil = LoggerFactory.getLogger(FFmpegManagerImpl.class);


    @Autowired
    private FFmpegConfig ffmpegConfigAutowired = null;
    private static FFmpegConfig ffmpegConfig = null;

    @PostConstruct
    public void init() {
        ffmpegConfig = this.ffmpegConfigAutowired;
    }

    private static String execCmdString(List<String> commands) {

        fileSize = null;
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
                            if(line.contains("Duration:")){
                                LogUtil.info("Duration: " + line);
                                String [] tmp = line.split(",");
                                if (tmp.length > 0){
                                    String Duration = tmp[0];
                                    fileSize = Duration.replace("Duration: ","");
                                }
                            }
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

                            if(line.contains("Duration:")){
                                LogUtil.info("Duration: " + line);
                                String [] tmp = line.split(",");
                                if (tmp.length > 0){
                                    String Duration = tmp[0];
                                    fileSize = Duration.replace("Duration: ","");
                                }
                            }
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
            return fileSize;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static boolean execCmd(List<String> commands) {

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

    public static String getFileDuration(String mp4File){
        String basePath = ffmpegConfig.getPath();
        List<String> commands = new ArrayList<String>();
        commands.add(basePath + "ffprobe");
        commands.add(mp4File);

        return   execCmdString(commands);
    }
}
