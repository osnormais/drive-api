package org.osnormais.drive.api.domain.entitlement.grant;

import static java.util.Objects.isNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.osnormais.drive.api.domain.AggregateRoot;
import org.osnormais.drive.api.domain.entitlement.quota.Amount;
import org.osnormais.drive.api.domain.entitlement.quota.BandwidthQuota;
import org.osnormais.drive.api.domain.entitlement.quota.BytesQuota;
import org.osnormais.drive.api.domain.entitlement.quota.ParallelOperationsQuota;
import org.osnormais.drive.api.domain.entitlement.quota.Quota;
import org.osnormais.drive.api.domain.entitlement.quota.TotalCountQuota;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public class UserEntitlementGrant extends AggregateRoot<GrantId> {

    private final UserId userId;
    private final Set<QuotaGrant<BytesQuota>> storageQuotaGrants;
    private final Set<QuotaGrant<BandwidthQuota>> bandwidthQuotaGrants;
    private final Set<QuotaGrant<ParallelOperationsQuota>> parallelOperationsQuotaGrants;
    private final Set<QuotaGrant<TotalCountQuota>> maxFilesQuotaGrants;

    private UserEntitlementGrant(
            final GrantId id,
            final UserId userId,
            final Set<QuotaGrant<BytesQuota>> storageQuotaGrants,
            final Set<QuotaGrant<BandwidthQuota>> bandwidthQuotaGrants,
            final Set<QuotaGrant<ParallelOperationsQuota>> parallelOperationsQuotaGrants,
            final Set<QuotaGrant<TotalCountQuota>> maxFilesQuotaGrants) {
        super(id);
        this.userId = userId;
        this.storageQuotaGrants = isNull(storageQuotaGrants) ? new HashSet<>()
                : new HashSet<>(storageQuotaGrants);
        this.bandwidthQuotaGrants = isNull(bandwidthQuotaGrants) ? new HashSet<>()
                : new HashSet<>(bandwidthQuotaGrants);
        this.parallelOperationsQuotaGrants = isNull(parallelOperationsQuotaGrants) ? new HashSet<>()
                : new HashSet<>(parallelOperationsQuotaGrants);
        this.maxFilesQuotaGrants = isNull(maxFilesQuotaGrants) ? new HashSet<>()
                : new HashSet<>(maxFilesQuotaGrants);
    }

    @Override
    public void validate(ValidationHandler handler) {

        if (isNull(userId))
            handler.append(new ValidationError("User ID cannot be null"));

    }

    public BytesQuota totalActiveStorageQuota() {
        return BytesQuota.of(sumActiveQuotaAmounts(storageQuotaGrants));
    }

    private static <Q extends Quota> Amount sumActiveQuotaAmounts(final Set<QuotaGrant<Q>> grants) {
        return grants
                .stream()
                .filter(QuotaGrant::isActive)
                .map(QuotaGrant::quota)
                .map(Quota::amount)
                .flatMap(Optional::stream)
                .reduce(Amount.zero(), Amount::add);
    }

    public UserId getUserId() {
        return userId;
    }

    public Set<QuotaGrant<BytesQuota>> getStorageQuotaGrants() {
        return Set.copyOf(storageQuotaGrants);
    }

    public Set<QuotaGrant<BandwidthQuota>> getBandwidthQuotaGrants() {
        return Set.copyOf(bandwidthQuotaGrants);
    }

    public Set<QuotaGrant<ParallelOperationsQuota>> getParallelOperationsQuotaGrants() {
        return Set.copyOf(parallelOperationsQuotaGrants);
    }

    public Set<QuotaGrant<TotalCountQuota>> getMaxFilesQuotaGrants() {
        return Set.copyOf(maxFilesQuotaGrants);
    }

}
