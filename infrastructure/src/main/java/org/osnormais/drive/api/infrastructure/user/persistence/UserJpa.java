package org.osnormais.drive.api.infrastructure.user.persistence;

import java.util.Optional;
import java.util.UUID;

import org.osnormais.drive.api.domain.entitlement.grant.GrantId;
import org.osnormais.drive.api.domain.entitlement.plan.PlanId;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;

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
    private UUID planId;

    private UUID activeGrantId;

    private UserJpa(
            final UUID id,
            final UUID planId,
            final UUID activeGrantId) {
        this.id = id;
        this.planId = planId;
        this.activeGrantId = activeGrantId;
    }

    public User toDomain() {
        return User.with(
                UserId.of(getId()),
                PlanId.of(getPlanId()),
                Optional.ofNullable(getActiveGrantId()).map(GrantId::of).orElse(null),
                null);
    }

    public static UserJpa fromDomain(final User user) {
        return new UserJpa(
                user.getId().getValue(),
                user.getPlan().getValue(),
                user.getActiveGrant().map(GrantId::getValue).orElse(null));
    }

    public UserJpa() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPlanId() {
        return planId;
    }

    public void setPlanId(UUID planId) {
        this.planId = planId;
    }

    public UUID getActiveGrantId() {
        return activeGrantId;
    }

    public void setActiveGrantId(UUID activeGrantId) {
        this.activeGrantId = activeGrantId;
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
