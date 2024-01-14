package com.sangcheol.clohomework.common;

import com.sangcheol.clohomework.common.exception.CommonCloException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Component
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(CommonCloException.class)
    public ResponseEntity handleCommonCloException(CommonCloException e) {
        // CloException은 정의된 예외이니 error로그가 아닌 info 로 남긴다.
        // error로 남길 경우 제대로 된 에러 모니터링이 불가능 할 수 있다.
        log.info(e.getMessage(), e);
        return new ResponseEntity<>(CommonErrorResponse.from(e, null), e.getCode().getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        CommonCloException ex = new CommonCloException(CommonErrorCode.UNKNOWN_ERROR, null);
        return new ResponseEntity<>(CommonErrorResponse.from(ex, null), CommonErrorCode.UNKNOWN_ERROR.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        log.error(e.getMessage(), e);
        CommonCloException ex = new CommonCloException(CommonErrorCode.UNKNOWN_ERROR, null);
        return new ResponseEntity<>(CommonErrorResponse.from(ex, null), CommonErrorCode.UNKNOWN_ERROR.getStatus());
    }
}
