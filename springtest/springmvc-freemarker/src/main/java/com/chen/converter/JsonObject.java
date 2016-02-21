package com.chen.converter;

/**
 * Description: JSON格式数据返回对象,封装controller 返回的对象
 * Author: wei
 * DateTime: 2015-08-14 11:41
 */

public class JsonObject {

	/**
	 * 标示请求结果为成功
	 */
	private Boolean success = Boolean.TRUE;

	/**
	 * controller 方法返回数据对象
	 */
	private Object data;

	public JsonObject(Object data) {
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
