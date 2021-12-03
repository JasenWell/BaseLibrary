package com.git.jasenwell.baselibproject;

import android.content.Context;

public class MainProduct {

    public static class Builder{
        private String name;
        private String des;
        private int id;

        Builder(Context context,int styleId){

        }

        Builder setName(String name) {
            this.name = name;
            return this;
        }

        Builder setId(int id) {
            this.id = id;
            return this;
        }

        Builder setDes(String des) {
            this.des = des;
            return this;
        }

        public MainProduct create(){
            return new MainProduct();
        }
    }
}
