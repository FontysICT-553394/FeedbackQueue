package com.beauver.Classes;

import com.beauver.Enums.StatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class Result<T> {

    @Expose
    private int status;
    @Expose
    private String error;
    @Expose
    private T data;

    public Result(int status, String error, T data) {
        this.status = status;
        this.error = error;
        this.data = data;
    }

    public Result(StatusCodes status, T data) {
        this.status = status.getCode();
        this.error = status.getReason();
        this.data = data;
    }

    public Result(StatusCodes status) {
        this.status = status.getCode();
        this.error = status.getReason();
    }

    public Result(int status, String error) {
        this.status = status;
        this.error = error;
    }


    public String toJson(){

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        return gson.toJson(this);
    }

}
