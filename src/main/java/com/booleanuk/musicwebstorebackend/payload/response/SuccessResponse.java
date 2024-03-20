package com.booleanuk.musicwebstorebackend.payload.response;

public class SuccessResponse<T> extends Response<T>{

    public SuccessResponse(T data) {
        super("success", data);
    }
}