package com.doubleo.entranceservice.domain.controller;

import com.doubleo.entranceservice.domain.dto.request.EnterVerificationInfoRequest;
import com.doubleo.entranceservice.domain.dto.response.EnterVerificationInfoResponse;
import com.doubleo.entranceservice.domain.service.EntranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entrances")
@RequiredArgsConstructor
public class EntranceController {
    private final EntranceService entranceService;

    @PostMapping("/verify")
    public EnterVerificationInfoResponse entranceVerify(
            @RequestHeader("X-Tenant-Id") String tenantId,
            @RequestBody EnterVerificationInfoRequest request) {
        return entranceService.verifyEntrance(tenantId, request);
    }
}
