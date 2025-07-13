package com.doubleo.entranceservice.grpc.client;

import com.doubleo.didagent.grpc.server.AcapyServiceGrpc;
import com.doubleo.didagent.grpc.server.VerifyCredentialRequest;
import com.doubleo.didagent.grpc.server.VerifyCredentialResponse;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AcapyClient {

    @GrpcClient("acapy-service")
    private AcapyServiceGrpc.AcapyServiceBlockingStub blockingStub;

    public boolean verifyCredential(String tenantId, long passId, String connectionId) {
        VerifyCredentialRequest request =
                VerifyCredentialRequest.newBuilder()
                        .setTenantId(tenantId)
                        .setPassId(passId)
                        .setConnectionId(connectionId)
                        .build();

        VerifyCredentialResponse response = blockingStub.verifyCredential(request);
        return response.getIsVerified();
    }
}
