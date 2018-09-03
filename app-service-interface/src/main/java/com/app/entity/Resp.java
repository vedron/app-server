package com.app.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Resp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 463307427361109020L;

	private int code = 200;

	private String msg = "OK";

	private Object datas = null;

	public Resp() {
		super();
	}

	public Resp(int code) {
		super();
		this.code = code;
	}

	public Resp(Object datas) {
		super();
		this.datas = datas;
	}

	public Resp(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public Resp(int code, String msg, Object datas) {
		super();
		this.code = code;
		this.msg = msg;
		this.datas = datas;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getDatas() {
		return datas;
	}

	public void setDatas(Object datas) {
		this.datas = datas;
	}

	public void setDate(String key, Object value) {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put(key, value);

		this.datas = map;
	}
}
