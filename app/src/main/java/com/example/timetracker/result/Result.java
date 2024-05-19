package com.example.timetracker.result;

public class Result {
    private boolean success;
    private String message;

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isFail() {
        return !success;
    }

    public static class Error extends Result {
        public Error(String message) {
            super(false, message);
        }
    }

    public static class Success extends Result {
        public Success(String message) {
            super(true, message);
        }
    }


}
