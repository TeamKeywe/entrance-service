package com.doubleo.entranceservice.global.exception.errorcode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ErrorCodeRegistry {
    private static final Map<String, Function<String, ? extends BaseErrorCode>> registry =
            new HashMap<>();

    static {
        registry.put("GlobalErrorCode", GlobalErrorCode::valueOf);
        registry.put("GrpcErrorCode", GrpcErrorCode::valueOf);
    }

    public static BaseErrorCode resolve(String className, String code) {
        Function<String, ? extends BaseErrorCode> parser = registry.get(className);
        if (parser == null) {
            throw new IllegalArgumentException("Unknown class " + className);
        }
        try {
            return parser.apply(code);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid error code " + code + " for class " + className, e);
        }
    }
}
