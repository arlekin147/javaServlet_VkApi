package com.webim;

import java.io.IOException;

public class BadResponseException extends IOException {
    int errorCode;
    public BadResponseException(int errorCode){
        this.errorCode = errorCode;
    }
    public String toString(){
        return String.valueOf(this.errorCode);
    }
}
