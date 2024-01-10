package com.sangcheol.clohomework.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CloCommonResponse<T> {

    private String status = "success";
    private Object data;
    private String message = "OK";

    public CloCommonResponse(String status) {
        this.status = status;
    }

    public CloCommonResponse(Object data) {
        this.data = data;
    }

    public static CloCommonResponse ok() {
        return new CloCommonResponse();
    }

    public static CloCommonResponse ok(Object data) {
        return new CloCommonResponse(data);
    }

    public static CloCommonResponse error(String message) {
        return new CloCommonResponse("error", null, message);
    }

}
