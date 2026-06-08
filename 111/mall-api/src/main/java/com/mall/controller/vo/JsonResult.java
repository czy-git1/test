package com.mall.controller.vo;

public class JsonResult {

    private Object data;
    private Integer code=200;
    private String msg;
    private boolean success = true;

    /**
     * 若没有数据返回，默认状态码为 0，提示信息为“操作成功！”
     */
    public JsonResult() {
        this.code = 200;
        this.msg = "操作成功！";
    }

    /**
     * 若没有数据返回，可以人为指定状态码和提示信息
     * @param code
     * @param msg
     */
    public JsonResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(boolean success, Integer code, String msg){
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    /**
     * 有数据返回时，状态码为 0，默认提示信息为“操作成功！”
     * @param data
     */
    public JsonResult(Object data) {
        this.data = data;
        this.code = 200;
        this.msg = "操作成功！";
    }

    /**
     * 有数据返回，状态码为 0，人为指定提示信息
     * @param data
     * @param msg
     */
    public JsonResult(Object data, String msg) {
        this.data = data;
        this.code = 200;
        this.msg = msg;
    }

    public JsonResult(Integer code, Object data, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    /**
     * 有数据返回，状态码为 0，人为指定提示信息
     * @param data
     * @param msg
     */
    public static JsonResult success(Object data, String msg) {
        JsonResult jr = new JsonResult();
        jr.setCode(200);
        jr.setData(data);
        jr.setMsg(msg);
        jr.setSuccess(true);
        return jr;
    }

    public static JsonResult error(Integer code, Object data, String msg) {
        JsonResult jr = new JsonResult();
        jr.setCode(code);
        jr.setData(data);
        jr.setMsg(msg);
        jr.setSuccess(false);
        return jr;
    }

    public static JsonResult error(Object data, String msg) {
        JsonResult jr = new JsonResult();
        jr.setData(data);
        jr.setMsg(msg);
        jr.setSuccess(false);
        return jr;
    }
    
    public static JsonResult success() {
        JsonResult jr = new JsonResult();
        jr.setSuccess(true);
        jr.setCode(200);
        jr.setMsg("操作成功");
    	return jr;
    }
    
    public static JsonResult success(Object data) {
    	JsonResult ret = new JsonResult("0", "操作成功");
    	ret.setData(data);
    	return ret;
    }
    
    public static JsonResult error() {
    	JsonResult ret = new JsonResult("500", "系统繁忙");
        ret.setSuccess(false);
    	return ret;
    }
    
    public static JsonResult error(String code, String msg) {
    	JsonResult ret = new JsonResult(code, msg);
        ret.setSuccess(false);
    	return ret;
    }

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.code = 200;
        this.success = success;
    }
}