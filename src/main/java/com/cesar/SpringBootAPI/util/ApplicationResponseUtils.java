package com.cesar.SpringBootAPI.util;

import com.cesar.SpringBootAPI.dto.ApplicationResponse;
import java.util.Map;

public class ApplicationResponseUtils {

    public static <T> ApplicationResponse<Map<String, Object>> buildResponse(int code, String message, String dataKey, T dataValue){
        Map<String, Object> data = (dataKey!=null) ? Map.of(dataKey, dataValue) : null;
        return new ApplicationResponse<>(code, message, data);
    }
}