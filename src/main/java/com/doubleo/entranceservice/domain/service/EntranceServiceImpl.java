package com.doubleo.entranceservice.domain.service;

import com.doubleo.entranceservice.domain.dto.request.EnterVerificationInfoRequest;
import com.doubleo.entranceservice.domain.dto.response.EnterVerificationInfoResponse;
import com.doubleo.entranceservice.domain.enums.DeviceLocationType;
import com.doubleo.entranceservice.domain.enums.Direction;
import com.doubleo.entranceservice.domain.enums.VisitCategory;
import com.doubleo.entranceservice.global.exception.CommonException;
import com.doubleo.entranceservice.global.exception.errorcode.EntranceErrorCode;
import com.doubleo.entranceservice.grpc.client.HospitalClient;
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
    private final HospitalClient hospitalClient;

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

        if (VisitCategory.GUARDIAN.equals(request.visitCategory()) && Direction.IN.equals(request.direction())) {
            long maxGuardianNum = hospitalClient.getMaximumGuardianNum(tenantId);

            // 현재 입장 상태인 보호자 수 임시 값 사용
            // 입장 상태 보호자 수 snapshot table 기준으로 redis 캐싱 방식으로 수정 예정
            long currentGuardianCount = 2;

            if (currentGuardianCount >= maxGuardianNum) {
                return new EnterVerificationInfoResponse(false, "최대 보호자 입장 수를 초과하였습니다.");
            }
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
                        request.visitCategory());
            } else {
                logClient.createEnterLog(
                        tenantId,
                        request.deviceAreaId(),
                        request.memberId(),
                        request.memberName(),
                        request.passId(),
                        request.visitCategory());
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
