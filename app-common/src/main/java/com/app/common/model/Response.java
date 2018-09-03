package com.app.common.model;

public class Response {
	private Meta meta;
    private Object data;
    private int count;

    public Response() {}

    // 成功||失败-状态码-成|败-消息-数据
    public Response(int code, boolean success, String message, Object data) {
        this.meta = new Meta(code, success, message);
        this.data = data;
        this.count = 0;
    }
    public Response(int code, boolean success, String message, Object data,int count) {
        this.meta = new Meta(code, success, message);
        this.data = data;
        this.count = count;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
    
    
}
