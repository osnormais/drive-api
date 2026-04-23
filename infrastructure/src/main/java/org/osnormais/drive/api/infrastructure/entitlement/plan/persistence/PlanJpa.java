package org.osnormais.drive.api.infrastructure.entitlement.plan.persistence;

import java.util.Optional;
import java.util.UUID;

import org.osnormais.drive.api.domain.entitlement.plan.Plan;
import org.osnormais.drive.api.domain.entitlement.plan.PlanId;
import org.osnormais.drive.api.domain.entitlement.plan.PlanName;
import org.osnormais.drive.api.domain.entitlement.quota.Amount;
import org.osnormais.drive.api.domain.entitlement.quota.BandwidthQuota;
import org.osnormais.drive.api.domain.entitlement.quota.BytesQuota;
import org.osnormais.drive.api.domain.entitlement.quota.ParallelOperationsQuota;
import org.osnormais.drive.api.domain.entitlement.quota.TotalCountQuota;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Plan")
@Table(name = "plans")
public class PlanJpa {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    private Long storageQuotaBytes;

    @Column(nullable = false)
    private Boolean storageQuotaIsUnlimited;

    private Long bandwidthQuotaBytes;

    @Column(nullable = false)
    private Boolean bandwidthQuotaIsUnlimited;

    private Long parallelOperationsQuotaCount;

    @Column(nullable = false)
    private Boolean parallelOperationsQuotaIsUnlimited;

    private Long maxFilesQuotaCount;

    @Column(nullable = false)
    private Boolean maxFilesQuotaIsUnlimited;

    private PlanJpa(
            final UUID id,
            final String name,
            final Long storageQuotaBytes,
            final Boolean storageQuotaIsUnlimited,
            final Long bandwidthQuotaBytes,
            final Boolean bandwidthQuotaIsUnlimited,
            final Long parallelOperationsQuotaCount,
            final Boolean parallelOperationsQuotaIsUnlimited,
            final Long maxFilesQuotaCount,
            final Boolean maxFilesQuotaIsUnlimited) {
        this.id = id;
        this.name = name;
        this.storageQuotaBytes = storageQuotaBytes;
        this.storageQuotaIsUnlimited = storageQuotaIsUnlimited;
        this.bandwidthQuotaBytes = bandwidthQuotaBytes;
        this.bandwidthQuotaIsUnlimited = bandwidthQuotaIsUnlimited;
        this.parallelOperationsQuotaCount = parallelOperationsQuotaCount;
        this.parallelOperationsQuotaIsUnlimited = parallelOperationsQuotaIsUnlimited;
        this.maxFilesQuotaCount = maxFilesQuotaCount;
        this.maxFilesQuotaIsUnlimited = maxFilesQuotaIsUnlimited;
    }

    public PlanJpa() {

    }

    public Plan toDomain() {
        return Plan.with(
                PlanId.of(getId()),
                new PlanName(getName()),
                BytesQuota.with(
                        Optional.ofNullable(getStorageQuotaBytes()).map(Amount::of),
                        getStorageQuotaIsUnlimited()),
                BandwidthQuota.with(
                        Optional.ofNullable(getBandwidthQuotaBytes()).map(Amount::of),
                        getBandwidthQuotaIsUnlimited()),
                ParallelOperationsQuota.with(
                        Optional.ofNullable(getParallelOperationsQuotaCount()).map(Amount::of),
                        getParallelOperationsQuotaIsUnlimited()),
                TotalCountQuota.with(
                        Optional.ofNullable(getMaxFilesQuotaCount()).map(Amount::of),
                        getMaxFilesQuotaIsUnlimited()));
    }

    public static PlanJpa fromDomain(final Plan plan) {
        return new PlanJpa(
                plan.getId().getValue(),
                plan.getName().value(),
                plan.getStorageQuota().amount().map(Amount::value).orElse(null),
                plan.getStorageQuota().isUnlimited(),
                plan.getBandwidthQuota().amount().map(Amount::value).orElse(null),
                plan.getBandwidthQuota().isUnlimited(),
                plan.getParallelOperationsQuota().amount().map(Amount::value).orElse(null),
                plan.getParallelOperationsQuota().isUnlimited(),
                plan.getMaxFilesQuota().amount().map(Amount::value).orElse(null),
                plan.getMaxFilesQuota().isUnlimited());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStorageQuotaBytes() {
        return storageQuotaBytes;
    }

    public void setStorageQuotaBytes(Long storageQuotaBytes) {
        this.storageQuotaBytes = storageQuotaBytes;
    }

    public Boolean getStorageQuotaIsUnlimited() {
        return storageQuotaIsUnlimited;
    }

    public void setStorageQuotaIsUnlimited(Boolean storageQuotaIsUnlimited) {
        this.storageQuotaIsUnlimited = storageQuotaIsUnlimited;
    }

    public Long getBandwidthQuotaBytes() {
        return bandwidthQuotaBytes;
    }

    public void setBandwidthQuotaBytes(Long bandwidthQuotaBytes) {
        this.bandwidthQuotaBytes = bandwidthQuotaBytes;
    }

    public Boolean getBandwidthQuotaIsUnlimited() {
        return bandwidthQuotaIsUnlimited;
    }

    public void setBandwidthQuotaIsUnlimited(Boolean bandwidthQuotaIsUnlimited) {
        this.bandwidthQuotaIsUnlimited = bandwidthQuotaIsUnlimited;
    }

    public Long getParallelOperationsQuotaCount() {
        return parallelOperationsQuotaCount;
    }

    public void setParallelOperationsQuotaCount(Long parallelOperationsQuotaCount) {
        this.parallelOperationsQuotaCount = parallelOperationsQuotaCount;
    }

    public Boolean getParallelOperationsQuotaIsUnlimited() {
        return parallelOperationsQuotaIsUnlimited;
    }

    public void setParallelOperationsQuotaIsUnlimited(Boolean parallelOperationsQuotaIsUnlimited) {
        this.parallelOperationsQuotaIsUnlimited = parallelOperationsQuotaIsUnlimited;
    }

    public Long getMaxFilesQuotaCount() {
        return maxFilesQuotaCount;
    }

    public void setMaxFilesQuotaCount(Long maxFilesQuotaCount) {
        this.maxFilesQuotaCount = maxFilesQuotaCount;
    }

    public Boolean getMaxFilesQuotaIsUnlimited() {
        return maxFilesQuotaIsUnlimited;
    }

    public void setMaxFilesQuotaIsUnlimited(Boolean maxFilesQuotaIsUnlimited) {
        this.maxFilesQuotaIsUnlimited = maxFilesQuotaIsUnlimited;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlanJpa other = (PlanJpa) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
