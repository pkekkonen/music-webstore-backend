package com.booleanuk.musicwebstorebackend.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonPropertyOrder({"status", "data"})
public abstract class Response<T> {

    @JsonProperty
    private String status;

    @JsonProperty
    private T data;

    public Response(String status, T data) {
        this.status = status;
        this.data = data;
    }
}