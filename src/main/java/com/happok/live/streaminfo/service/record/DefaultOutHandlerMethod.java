package com.happok.live.streaminfo.service.record;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultOutHandlerMethod implements OutHandlerMethod {

    private Integer count = 100;
    private static Logger LogUtil = LoggerFactory.getLogger(DefaultOutHandlerMethod.class);

    @Override
    public void parse(String type, String msg) {
//		System.out.println(type+"：完整消息："+msg);
        //过滤消息

        if (msg.indexOf("[rtsp") != -1) {
            LogUtil.warn(type + "发生网络异常丢包，消息体：" + msg);
        } else if (msg.indexOf("frame=") != -1) {
            if (count++ > 100) {
                LogUtil.warn(type + "<====>" + msg);
                count = 0;
            }
        }
    }
}
