package com.sangcheol.clohomework.common.exception;

import com.sangcheol.clohomework.common.CommonErrorCode;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

@Getter
public class CommonCloException extends RuntimeException {

    private CommonErrorCode code;
    private String message;

    public CommonCloException(CommonErrorCode code, String message) {
        String applyMessage = Strings.isBlank(message) ? code.getDefaultMessage() : message;
        this.code = code;
        this.message = applyMessage;
    }
}
