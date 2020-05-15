package com.zhang.demo.common.utils.security;

import com.zhang.demo.entity.ZUserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * SpringSecurity需要的用户信息
 */
public class AdminUserDetails implements UserDetails {

    private ZUserEntity zUserEntity;

    public AdminUserDetails(ZUserEntity zUserEntity) {
        this.zUserEntity = zUserEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return zUserEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return zUserEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
