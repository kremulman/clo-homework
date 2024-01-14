package com.sangcheol.clohomework.common;

import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "대상을 찾을 수 없습니다.", "%s 을(를) 찾을 수 없습니다."),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.", "알 수 없는 오류가 발생했습니다.")
    ;

    private HttpStatus status;
    private String defaultMessage;
    private String formattedMessage;

    CommonErrorCode(HttpStatus status, String defaultMessage, String formattedMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
        this.formattedMessage = formattedMessage;
    }

    public String getFormattedMessage(String arg) {
        if (Strings.isBlank(arg)) {
            return this.formattedMessage;
        }
        return String.format(this.formattedMessage, arg);
    }
}
