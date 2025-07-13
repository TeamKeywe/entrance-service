package com.doubleo.entranceservice.global.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TenantErrorCode implements BaseErrorCode {
    TENANT_ID_NOT_FOUND(HttpStatus.UNAUTHORIZED, "요청에 tenant id가 누락되었습니다."),
    INVALID_TENANT_ID(HttpStatus.FORBIDDEN, "유효한 tenant id가 아닙니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String errorClassName() {
        return this.name();
    }
}
