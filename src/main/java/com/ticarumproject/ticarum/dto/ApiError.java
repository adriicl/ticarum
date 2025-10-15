package com.ticarumproject.ticarum.dto;

import java.time.OffsetDateTime;

public class ApiError {
    public OffsetDateTime timestamp;
    public int status;
    public String error;
    public String message;
    public String path;
}


