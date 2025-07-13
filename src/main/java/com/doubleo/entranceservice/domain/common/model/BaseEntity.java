package com.doubleo.entranceservice.domain.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Tenant {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDt;

    @LastModifiedDate private LocalDateTime updatedDt;

    @Column(name = "tenant_id", nullable = false)
    protected String tenantId;

    @Override
    public String getTenantId() {
        return tenantId;
    }
}
