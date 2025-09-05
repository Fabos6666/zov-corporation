package ru.zov_corporation.api.file.exception;

public class FileSaveException extends FileProcessingException {
    public FileSaveException(String message) {
        super(message);
    }

    public FileSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}