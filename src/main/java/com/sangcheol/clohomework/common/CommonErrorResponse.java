package com.sangcheol.clohomework.common;

import com.sangcheol.clohomework.common.exception.CommonCloException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommonErrorResponse {

    private int code;
    private String message;
    private Object data;

    public static CommonErrorResponse from(CommonCloException e, Object data) {
        return CommonErrorResponse.builder()
                .code(e.getCode().getStatus().value())
                .message(e.getMessage())
                .data(data)
                .build();
    }

}
