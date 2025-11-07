package com.bohdan.filesharing.common.exception;

public class LoadFileToS3ServerException extends RuntimeException {
    public LoadFileToS3ServerException(String message) {
        super(message);
    }
}
