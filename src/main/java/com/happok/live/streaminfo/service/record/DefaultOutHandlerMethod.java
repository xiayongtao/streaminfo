package com.happok.live.streaminfo.service.record;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认任务消息输出处理
 * @author eguid
 * @since jdk1.7
 * @version 2017年10月13日
 */
public class DefaultOutHandlerMethod implements OutHandlerMethod{

	private static Logger LogUtil = LoggerFactory.getLogger(DefaultOutHandlerMethod.class);

	@Override
	public void parse(String type,String msg) {
//		System.out.println(type+"：完整消息："+msg);
		//过滤消息
		if (msg.indexOf("[rtsp") != -1) {
			LogUtil.error(type + "发生网络异常丢包，消息体：" + msg);
		}else if(msg.indexOf("frame=")!=-1){
			LogUtil.error(type + ":" + msg);
		}
	}
}
