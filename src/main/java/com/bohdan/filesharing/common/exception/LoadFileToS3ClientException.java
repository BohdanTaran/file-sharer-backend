package com.bohdan.filesharing.common.exception;

public class LoadFileToS3ClientException extends RuntimeException {
    public LoadFileToS3ClientException(String message) {
        super(message);
    }
}
