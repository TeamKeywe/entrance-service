package com.doubleo.entranceservice.global.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EntranceErrorCode implements BaseErrorCode {
    ENTRANCE_SAMPLE_ERROR_CODE(HttpStatus.NOT_FOUND, "entrance sample error"),
    MISSING_DIRECTION_WHEN_BUILDING(HttpStatus.BAD_REQUEST, "건물 출입 시 방향 정보는 필수입니다.");
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String errorClassName() {
        return this.name();
    }
}
