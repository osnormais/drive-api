package org.osnormais.drive.api.infrastructure.user.persistence;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.domain.user.valueobject.Quota;
import org.osnormais.drive.api.domain.user.valueobject.QuotaRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "User")
@Table(name = "users")
public class UserJpa {

    @Id
    private UUID id;

    @Column(nullable = false)
    private Long quotaBytes;

    private Long requestedQuotaBytes;

    private Instant requestedQuotaAt;

    private UserJpa(
            final UUID id,
            final Long quotaBytes,
            final Long requestedQuotaBytes,
            final Instant requestedQuotaAt) {
        this.id = id;
        this.quotaBytes = quotaBytes;
        this.requestedQuotaBytes = requestedQuotaBytes;
        this.requestedQuotaAt = requestedQuotaAt;
    }

    public User toDomain() {

        return User.with(
                UserId.of(getId()),
                Quota.of(getQuotaBytes()),
                (requestedQuotaBytes != null || requestedQuotaAt != null)
                        ? new QuotaRequest(new Quota(requestedQuotaBytes), requestedQuotaAt)
                        : null,
                null);
    }

    public static UserJpa fromDomain(final User user) {
        return new UserJpa(
                user.getId().getValue(),
                user.getQuota().bytes(),
                user.getQuotaRequest().map(QuotaRequest::requestedQuota).map(Quota::bytes).orElse(null),
                user.getQuotaRequest().map(QuotaRequest::requestedAt).orElse(null));
    }

    public UserJpa() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getQuotaBytes() {
        return quotaBytes;
    }

    public void setQuotaBytes(Long quotaBytes) {
        this.quotaBytes = quotaBytes;
    }

    public Long getRequestedQuotaBytes() {
        return requestedQuotaBytes;
    }

    public void setRequestedQuotaBytes(Long requestedQuotaBytes) {
        this.requestedQuotaBytes = requestedQuotaBytes;
    }

    public Instant getRequestedQuotaAt() {
        return requestedQuotaAt;
    }

    public void setRequestedQuotaAt(Instant requestedQuotaAt) {
        this.requestedQuotaAt = requestedQuotaAt;
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
        UserJpa other = (UserJpa) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
