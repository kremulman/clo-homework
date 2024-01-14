package com.sangcheol.clohomework.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonErrorCode {

    NOT_FOUND(HttpStatus.BAD_REQUEST, "대상을 찾을 수 없습니다.", "%s 을(를) 찾을 수 없습니다."),
    ;

    private HttpStatus status;
    private String defaultMessage;
    private String formattedMessage;

    CommonErrorCode(HttpStatus status, String defaultMessage, String formattedMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
        this.formattedMessage = formattedMessage;
    }
}
