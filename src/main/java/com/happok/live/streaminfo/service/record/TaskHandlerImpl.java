package com.happok.live.streaminfo.service.record;

import com.happok.live.streaminfo.config.FFmpegConfig;
import com.happok.live.streaminfo.entity.record.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class TaskHandlerImpl implements TaskHandler {

    @Autowired
    private FFmpegConfig config = null;

    private static Logger LogUtil = LoggerFactory.getLogger(TaskHandlerImpl.class);

    private Runtime runtime = null;

    private OutHandlerMethod ohm = null;

    public TaskHandlerImpl(OutHandlerMethod ohm) {
        this.ohm = ohm;
    }

    public void setOhm(OutHandlerMethod ohm) {
        this.ohm = ohm;
    }

    @Override
    public TaskEntity process(String id, String command) {
        Process process = null;
        OutHandler outHandler = null;
        TaskEntity tasker = null;
        try {
            if (runtime == null) {
                runtime = Runtime.getRuntime();
            }
            if (config.isDebug())
                LogUtil.info("执行命令：" + command);

            process = runtime.exec(command);// 执行本地命令获取任务主进程
            outHandler = new OutHandler(process.getErrorStream(), id, this.ohm);
            outHandler.start();
            tasker = new TaskEntity(id, process, outHandler);
        } catch (IOException e) {
            if (config.isDebug())
                LogUtil.error("执行命令失败！正在停止进程和输出线程...");
            stop(outHandler);
            stop(process);
            // 出现异常说明开启失败，返回null
            return null;
        }
        return tasker;
    }

    @Override
    public boolean stop(Process process) {
        if (process != null) {
            process.destroy();
            LogUtil.info("正在停止进程...");
            return true;
        }

        LogUtil.error("停止进程失败...");
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean stop(Thread outHandler) {
        if (outHandler != null && outHandler.isAlive()) {
            outHandler.stop();
            //outHandler.destroy();
            return true;
        }
        return false;
    }

    @Override
    public boolean stop(Process process, Thread thread) {
        boolean ret;
        ret = stop(thread);
        ret = stop(process);
        return ret;
    }
}
