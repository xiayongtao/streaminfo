package com.happok.live.streaminfo.service.record;

import com.happok.live.streaminfo.config.FFmpegConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 任务消息输出处理器
 *
 * @author eguid
 * @version 2017年10月13日
 * @since jdk1.7
 */
public class OutHandler extends Thread {

    private static Logger LogUtil = LoggerFactory.getLogger(OutHandler.class);

    @Autowired
    private FFmpegConfig config = null;
    /**
     * 控制状态
     */
    private volatile boolean desstatus = true;

    /**
     * 读取输出流
     */
    private BufferedReader br = null;

    /**
     * 输出类型
     */
    private String type = null;

    /**
     * 消息处理方法
     */
    private OutHandlerMethod ohm;

    public void setOhm(OutHandlerMethod ohm) {
        this.ohm = ohm;
    }

    public void setDesStatus(boolean desStatus) {
        this.desstatus = desStatus;
    }

    public OutHandler(InputStream is, String type, OutHandlerMethod ohm) {
        br = new BufferedReader(new InputStreamReader(is));
        this.type = type;
        this.ohm = ohm;
    }

    /**
     * 重写线程销毁方法，安全的关闭线程
     */
    @Override
    public void destroy() {
        setDesStatus(false);
    }

    /**
     * 执行输出线程
     */
    @Override
    public void run() {
        String msg = null;
        try {
            if (config.isDebug()) {
                LogUtil.info(type + "开始执行！");
                while (desstatus && (msg = br.readLine()) != null) {
                    ohm.parse(type, msg);
                }
            } else {
                Thread.yield();
            }
        } catch (IOException e) {
            LogUtil.error("发生内部异常错误，自动关闭[" + this.getId() + "]线程");
            destroy();
        } finally {
            if (this.isAlive()) {
                destroy();
                LogUtil.info("自动关闭[" + this.getId() + "]线程");
            }
        }
    }

}
