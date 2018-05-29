package com.happok.live.streaminfo.service.record;
/**
 * 输出消息处理
 * @author eguid
 * @since jdk1.7
 * @version 2017年10月13日
 */
public interface OutHandlerMethod {
	/**
	 * 解析消息
	 * @param msg
	 * @param msg 
	 */
	public void parse(String type, String msg);
}
