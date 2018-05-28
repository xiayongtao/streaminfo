package com.happok.live.streaminfo.utils;

import com.happok.live.streaminfo.config.FFmpegConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class FFmpegUtil {

    @Autowired
    private FFmpegConfig ffmpegConfigAutowired = null;
    private static FFmpegConfig ffmpegConfig;

    @PostConstruct
    public void init() {
        ffmpegConfig = this.ffmpegConfigAutowired;
    }

    public static String ScreenShot(String srcUrl, String dirName) {

        long nowData = new Date().getTime();
        String baseFFmpegPath = ffmpegConfig.getBasePath();
        String filePath = ffmpegConfig.getBaseImagePath() + dirName + "/";

        if (!CreateFileUtil.createDir(filePath)) {
            System.out.println("创建目录" + filePath + "失败，目标目录已经存在");
        }

        String fileName = filePath + Long.toString(nowData) + "." + ffmpegConfig.getImageType();

        List<String> commands = new ArrayList<String>();
        commands.add(baseFFmpegPath);
        commands.add("-ss");
        commands.add(ffmpegConfig.getStartTime());//这个参数是设置截取视频多少秒时的画面
        commands.add("-i");
        commands.add(srcUrl);
        commands.add("-y");
        commands.add("-f");
        commands.add("image2");
        commands.add("-vframes");
        commands.add(ffmpegConfig.getVframes());
        commands.add("-s");
        commands.add(ffmpegConfig.getImageSize()); //这个参数是设置截取图片的大小
        commands.add(fileName);


        try {

            System.out.println(commands);
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            builder.redirectErrorStream(true);

            Process process = builder.start();
            InputStream in = process.getInputStream();
            process.getOutputStream();
            byte[] bytes = new byte[1024];

            process.waitFor();

            System.out.print("正在进行截图，请稍候");
            while (in.read(bytes) != -1) {
                System.out.print(".");
            }

            System.out.println("");
            System.out.println("视频截取完成...");

            in.close();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("视频截图失败！");
            return null;
        }


        /*StringBuffer sb = new StringBuffer();
        for (String tmp : commands) {
            sb.append(tmp + " ");
        }
        System.out.printf("视频截图命令：" + sb.toString());
        try {

            Runtime run = Runtime.getRuntime();
            Process process = run.exec(sb.toString());
            process.waitFor(); // 同步
            // 以下代码解决缓冲区不能被及时清除而被塞满导致进程阻塞问题
            InputStream in = process.getInputStream();
            in.close();

            return null;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }*/

    }
}
