package com.SpringSecurityRoles.enumeration;

import com.SpringSecurityRoles.constant.Autority;

public enum Role {    
    ROLE_USER(Autority.USER_AUTHORITIES),
    ROLE_HR(Autority.HR_AUTHORITIES),
    ROLE_MANAGER(Autority.MANAGER_AUTHORITIES),
    ROLE_ADMIN(Autority.ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(Autority.SUPER_ADMIN_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return this.authorities;
    }
}
