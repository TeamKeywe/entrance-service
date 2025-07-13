package com.doubleo.entranceservice.grpc.client;

import com.doubleo.passservice.grpc.server.GetConnectionIdByPassIdRequest;
import com.doubleo.passservice.grpc.server.GetConnectionIdByPassIdResponse;
import com.doubleo.passservice.grpc.server.PassServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PassClient {
    @GrpcClient("pass-service")
    private PassServiceGrpc.PassServiceBlockingStub blockingStub;

    public String getConnectionIdByPassId(String tenantId, long passId) {
        GetConnectionIdByPassIdRequest request =
                GetConnectionIdByPassIdRequest.newBuilder()
                        .setTenantId(tenantId)
                        .setPassId(passId)
                        .build();

        GetConnectionIdByPassIdResponse response = blockingStub.getConnectionIdByPassId(request);
        return response.getConnectionId();
    }
}
