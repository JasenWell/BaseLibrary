package com.hjh.baselib.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public final class ResponseListJson<T> implements Serializable {

	private static final long serialVersionUID = -4389432837873704874L;

	@Expose
	@SerializedName("code")//alternate = {""}别名
	private int status;//
	
	@Expose
	@SerializedName("message")
	private String msg;//
	
	@Expose
	@SerializedName(value = "data",alternate = {"items"})
	private  List<T> data;

	private int count;//总数据量
	private int  httpCode;//
	private Map<String, String> param;

	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	public Map<String, String> getParam() {
		return param;
	}

	public int getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	private String json;

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public static  ResponseListJson fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(ResponseListJson.class, clazz);
        return gson.fromJson(json, objectType);
    }

    public  String toJson(Class<T> clazz) {
        Gson gson = new Gson();
        Type objectType = type(ResponseListJson.class, clazz);
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
