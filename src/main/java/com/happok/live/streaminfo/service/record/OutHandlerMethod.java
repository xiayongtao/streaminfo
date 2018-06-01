package com.happok.live.streaminfo.service.record;

public interface OutHandlerMethod {
	/**
	 * 解析消息
	 * @param msg
	 * @param msg 
	 */
	public void parse(String type, String msg);
}
