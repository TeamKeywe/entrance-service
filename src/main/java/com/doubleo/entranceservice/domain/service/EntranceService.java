package com.doubleo.entranceservice.domain.service;

import com.doubleo.entranceservice.domain.dto.request.EnterVerificationInfoRequest;
import com.doubleo.entranceservice.domain.dto.response.EnterVerificationInfoResponse;

public interface EntranceService {
    EnterVerificationInfoResponse verifyEntrance(
            String tenantId, EnterVerificationInfoRequest request);
}
