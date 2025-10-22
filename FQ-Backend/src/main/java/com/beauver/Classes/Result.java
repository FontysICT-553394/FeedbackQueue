package com.beauver.Classes;

import com.beauver.Enums.StatusCodes;
import com.google.gson.Gson;

public class Result<T> {

    private int status;
    private String error;
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
        return new Gson().toJson(this);
    }

}
