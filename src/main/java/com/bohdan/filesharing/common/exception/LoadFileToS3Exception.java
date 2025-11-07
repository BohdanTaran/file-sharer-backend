package com.bohdan.filesharing.common.exception;

public class LoadFileToS3Exception extends RuntimeException {
    public LoadFileToS3Exception(String message) {
        super(message);
    }
}
