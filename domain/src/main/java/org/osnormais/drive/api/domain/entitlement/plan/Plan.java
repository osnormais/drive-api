package org.osnormais.drive.api.domain.entitlement.plan;

import static java.util.Objects.isNull;

import org.osnormais.drive.api.domain.AggregateRoot;
import org.osnormais.drive.api.domain.entitlement.quota.BandwidthQuota;
import org.osnormais.drive.api.domain.entitlement.quota.BytesQuota;
import org.osnormais.drive.api.domain.entitlement.quota.TotalCountQuota;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public class Plan extends AggregateRoot<PlanId> {

    private PlanName name;
    private BytesQuota storageQuota;
    private BandwidthQuota bandwidthQuota;
    private TotalCountQuota maxFilesQuota;

    private Plan(
            final PlanId id,
            final PlanName name,
            final BytesQuota storageQuota,
            final BandwidthQuota bandwidthQuota,
            final TotalCountQuota maxFilesQuota) {
        super(id);
        this.name = name;
        this.storageQuota = storageQuota;
        this.bandwidthQuota = bandwidthQuota;
        this.maxFilesQuota = maxFilesQuota;
    }

    public static Plan with(
            final PlanId id,
            final PlanName name,
            final BytesQuota storageQuota,
            final BandwidthQuota bandwidthQuota,
            final TotalCountQuota maxFilesQuota) {
        return new Plan(
                id,
                name,
                storageQuota,
                bandwidthQuota,
                maxFilesQuota);
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(name))
            handler.append(new ValidationError("'Plan.name' cannot be null."));
        else
            name.validate(handler);

        if (isNull(storageQuota))
            handler.append(new ValidationError("'Plan.storageQuota' cannot be null."));
        else
            storageQuota.validate(handler);

        if (isNull(bandwidthQuota))
            handler.append(new ValidationError("'Plan.bandwidthQuota' cannot be null."));
        else
            bandwidthQuota.validate(handler);

        if (isNull(maxFilesQuota))
            handler.append(new ValidationError("'Plan.maxFilesQuota' cannot be null."));
        else
            maxFilesQuota.validate(handler);

    }

    public PlanName getName() {
        return name;
    }

    public BytesQuota getStorageQuota() {
        return storageQuota;
    }

    public BandwidthQuota getBandwidthQuota() {
        return bandwidthQuota;
    }

    public TotalCountQuota getMaxFilesQuota() {
        return maxFilesQuota;
    }

}
