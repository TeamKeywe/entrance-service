package com.doubleo.entranceservice.grpc.client;

import com.doubleo.entranceservice.domain.enums.Direction;
import com.doubleo.entranceservice.domain.enums.VisitCategory;
import com.doubleo.entranceservice.global.exception.GrpcExceptionUtil;
import com.doubleo.logservice.grpc.server.*;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogClient {

    @GrpcClient("log-service")
    private LogServiceGrpc.LogServiceBlockingStub blockingStub;

    public CreateEnterLogResponse createEnterLog(
            String tenantId,
            Long areaId,
            Long memberId,
            String memberName,
            Long passId,
            VisitCategory visitCategory) {
        try {
            CreateEnterLogRequest request =
                    CreateEnterLogRequest.newBuilder()
                            .setTenantId(tenantId)
                            .setAreaId(areaId)
                            .setMemberId(memberId)
                            .setMemberName(memberName)
                            .setPassId(passId)
                            .setVisitCategory(visitCategory.name())
                            .build();

            return blockingStub.createEnterLog(request);
        } catch (StatusRuntimeException e) {
            log.error("gRPC createEnterLog error: {}", e.getMessage());
            throw GrpcExceptionUtil.fromStatusRuntimeException(e);
        }
    }

    public CreateBuildingEnterLogResponse createBuildingEnterLog(
            String tenantId,
            Long buildingId,
            Long memberId,
            String memberName,
            Long passId,
            Direction direction,
            VisitCategory visitCategory) {
        try {
            CreateBuildingEnterLogRequest request =
                    CreateBuildingEnterLogRequest.newBuilder()
                            .setTenantId(tenantId)
                            .setBuildingId(buildingId)
                            .setMemberId(memberId)
                            .setMemberName(memberName)
                            .setPassId(passId)
                            .setDirection(direction.name())
                            .setVisitCategory(visitCategory.name())
                            .build();

            return blockingStub.createBuildingEnterLog(request);
        } catch (StatusRuntimeException e) {
            log.error("gRPC createBuildingEnterLog error: {}", e.getMessage());
            throw GrpcExceptionUtil.fromStatusRuntimeException(e);
        }
    }
}
