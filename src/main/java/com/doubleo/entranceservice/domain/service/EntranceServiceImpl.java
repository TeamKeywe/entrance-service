package com.doubleo.entranceservice.domain.service;

import com.doubleo.entranceservice.domain.dto.request.EnterVerificationInfoRequest;
import com.doubleo.entranceservice.domain.dto.response.EnterVerificationInfoResponse;
import com.doubleo.entranceservice.domain.enums.DeviceLocationType;
import com.doubleo.entranceservice.domain.enums.VisitCategory;
import com.doubleo.entranceservice.global.exception.CommonException;
import com.doubleo.entranceservice.global.exception.errorcode.EntranceErrorCode;
import com.doubleo.entranceservice.grpc.client.LogClient;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EntranceServiceImpl implements EntranceService {

    private final LogClient logClient;

    @Override
    public EnterVerificationInfoResponse verifyEntrance(
            String tenantId, EnterVerificationInfoRequest request) {

        if (request.deviceLocationType() == DeviceLocationType.BUILDING
                && request.direction() == null) {
            throw new CommonException(EntranceErrorCode.MISSING_DIRECTION_WHEN_BUILDING);
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(request.startedAt()) || now.isAfter(request.expiredAt())) {
            return new EnterVerificationInfoResponse(false, "출입 유효 시간이 아닙니다.");
        }

        boolean isAllowed = verifyAreaCode(request.deviceAreaCode(), request.accessAreaCodes());
        if (!isAllowed) {
            return new EnterVerificationInfoResponse(false, "해당 구역에는 출입 권한이 없습니다.");
        }

        try {
            if (request.deviceLocationType() == DeviceLocationType.BUILDING) {
                logClient.createBuildingEnterLog(
                        tenantId,
                        request.deviceAreaId(),
                        request.memberId(),
                        request.memberName(),
                        request.passId(),
                        request.direction(),
                        VisitCategory.valueOf(request.visitCategory()));
            } else {
                logClient.createEnterLog(
                        tenantId,
                        request.deviceAreaId(),
                        request.memberId(),
                        request.memberName(),
                        request.passId(),
                        VisitCategory.valueOf(request.visitCategory()));
            }
        } catch (Exception e) {
            log.warn("출입 로그 기록 실패: {}", e.getMessage());
        }

        return new EnterVerificationInfoResponse(true, "출입이 허용되었습니다.");
    }

    private boolean verifyAreaCode(String deviceAreaCode, List<String> allowedAreas) {
        if (allowedAreas == null || allowedAreas.isEmpty()) return false;

        return allowedAreas.stream()
                .anyMatch(
                        allowed ->
                                allowed.equals(deviceAreaCode)
                                        || allowed.startsWith(deviceAreaCode + "_"));
    }
}
