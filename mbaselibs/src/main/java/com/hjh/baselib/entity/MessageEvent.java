package com.hjh.baselib.entity;

import java.io.Serializable;

/**
 * Created by hjh on 2018/2/12.
 */

public class MessageEvent<T> implements Serializable {

    private int type;
    private String des;
    private T data;

    public String getDes() {
        return des;
    }

    public MessageEvent setDes(String des) {
        this.des = des;
        return this;
    }

    public int getType() {
        return type;
    }

    public MessageEvent setType(int type) {
        this.type = type;
        return this;
    }

    public T getData() {
        return data;
    }

    public MessageEvent setData(T data) {
        this.data = data;
        return this;
    }

    public interface EventType{
        int RESET_TYPE_SIZE = 1;
        int RESET_BACKGROUND = 2;
        int REFRESH_MEETING_FILE     = 3;//文件数量发生了改变
        int DELETE_ALL_FILES = 4;//删除了所有文件
        int REFRESH_COLLECT_LIST = 5;//刷新收藏文件列表
        int REFRESH_HISTORY_LIST = 6;//刷新历史记录列表
    }
}
