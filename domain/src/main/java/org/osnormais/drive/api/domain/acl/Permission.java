package org.osnormais.drive.api.domain.acl;

public enum Permission {

    OWNER(0),
    MANAGE(1),
    WRITE(2),
    READ(3);

    private final Integer level;

    Permission(Integer level) {
        this.level = level;
    }

    public boolean includes(final Permission requiredPermission) {
        return this.level <= requiredPermission.level;
    }

    public Integer getLevel() {
        return level;
    }

}
