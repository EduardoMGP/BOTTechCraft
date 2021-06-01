package br.com.techcraftbrasil.restapi.model;

public class Result {

    private int code;
    private String status;
    private String message;

    public Result(int code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
