package com.doubleo.entranceservice.grpc.client;


import com.doubleo.hospitalservice.domain.policy.grpc.server.MaximumGuardianNumRequest;
import com.doubleo.hospitalservice.domain.policy.grpc.server.MaximumGuardianNumResponse;
import com.doubleo.hospitalservice.domain.policy.grpc.server.MaximumGuardianNumServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HospitalClient {

    @GrpcClient("hospital-service")
    private MaximumGuardianNumServiceGrpc.MaximumGuardianNumServiceBlockingStub blockingStub;

    public long getMaximumGuardianNum(String tenantId) {
        MaximumGuardianNumRequest request = MaximumGuardianNumRequest.newBuilder()
                .setTenantId(tenantId)
                .build();

        MaximumGuardianNumResponse response = blockingStub.getMaximumGuardianNumById(request);
        return response.getMaximumGuardianNum();
    }
}
