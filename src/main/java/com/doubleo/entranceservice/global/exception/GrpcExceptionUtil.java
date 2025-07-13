package com.doubleo.entranceservice.global.exception;

import com.doubleo.entranceservice.global.exception.errorcode.BaseErrorCode;
import com.doubleo.entranceservice.global.exception.errorcode.ErrorCodeRegistry;
import com.doubleo.entranceservice.global.exception.errorcode.GrpcErrorCode;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcExceptionUtil {

    private static final Metadata.Key<String> CODE_KEY =
            Metadata.Key.of("code", Metadata.ASCII_STRING_MARSHALLER);
    private static final Metadata.Key<String> CLASS_KEY =
            Metadata.Key.of("class", Metadata.ASCII_STRING_MARSHALLER);
    private static final Metadata.Key<String> MESSAGE_KEY =
            Metadata.Key.of("message", Metadata.ASCII_STRING_MARSHALLER);

    public static StatusRuntimeException toStatusRuntimeException(BaseErrorCode errorCode) {
        Metadata metadata = new Metadata();
        metadata.put(CODE_KEY, errorCode.errorClassName());
        metadata.put(CLASS_KEY, errorCode.getClass().getSimpleName());
        metadata.put(MESSAGE_KEY, errorCode.getMessage());

        return Status.fromCodeValue(errorCode.getHttpStatus().value())
                .withDescription(errorCode.getMessage())
                .asRuntimeException(metadata);
    }

    public static CommonException fromStatusRuntimeException(StatusRuntimeException e) {
        Metadata metadata = Status.trailersFromThrowable(e);
        if (metadata != null) {
            String code = metadata.get(CODE_KEY);
            String className = metadata.get(CLASS_KEY);
            String message = metadata.get(MESSAGE_KEY);

            log.warn("gRPC Error - code: {}, message: {}", code, message);
            return new CommonException(ErrorCodeRegistry.resolve(className, code));
        }
        return new CommonException(GrpcErrorCode.GRPC_SERVER_RESPONSE_FAILED);
    }
}
