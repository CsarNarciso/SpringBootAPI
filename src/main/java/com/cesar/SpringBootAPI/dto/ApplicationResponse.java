package com.cesar.SpringBootAPI.dto;

public record ApplicationResponse<T>(int code, String message, T data) {}