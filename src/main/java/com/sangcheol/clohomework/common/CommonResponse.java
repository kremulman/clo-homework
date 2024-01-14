package com.sangcheol.clohomework.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommonResponse<T> {

    private Object data;
    private String message = "OK";

    public CommonResponse(Object data) {
        this.data = data;
    }

    public static CommonResponse ok() {
        return new CommonResponse();
    }

    public static CommonResponse ok(Object data) {
        return new CommonResponse(data);
    }

}
