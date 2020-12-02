package com.hjh.baselib.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 返回data为对象
 * @author hjh
 * 2015-1-8上午4:10:54
 * @param <T>
 */
public final class ResponseJson<T> implements Serializable {

	private static final long serialVersionUID = -4214315238349813842L;
	@Expose
	@SerializedName("code")
	private int status;
	
	@Expose
	@SerializedName("message")
	private String msg;
	
	@Expose
	private  T data;

	private String json;
	private String error;
	private int  httpCode;//
	private Map<String, String> param;

	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	public Map<String, String> getParam() {
		return param;
	}

	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}

	public int getHttpCode() {
		return httpCode;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getJson() {
		return json;
	}

	public String getMsg() {
		return msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static  ResponseJson fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(ResponseJson.class, clazz);
        return gson.fromJson(json, objectType);
    }
	
	/**
	 * 只导出注解
	 * @param json
	 * @param clazz
	 * @param expose
	 * @return
	 */
	public static ResponseJson fromJson(String json, Class clazz,boolean expose){
		 Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	        Type objectType = type(ResponseJson.class, clazz);
	        return gson.fromJson(json, objectType);
	}

	/**
	 * 将对象转换为JSON字符串(Object to json string)
	 * @param object
	 * @return
	 */
	public static String objectToJson(Object object){
		return new Gson().toJson(object);
	}

    public  String toJson(Class<T> clazz) {
        Gson gson = new Gson();
        Type objectType = type(ResponseJson.class, clazz);
        return gson.toJson(this, objectType);
    }

    private static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

    
    
}
