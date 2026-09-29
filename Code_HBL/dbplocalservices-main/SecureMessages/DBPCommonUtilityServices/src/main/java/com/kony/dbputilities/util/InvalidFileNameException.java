package com.kony.dbputilities.util;

public class InvalidFileNameException extends Exception {
    private String fileName;

    public InvalidFileNameException(Throwable cause, String fileName) {
        super(cause);
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }
}