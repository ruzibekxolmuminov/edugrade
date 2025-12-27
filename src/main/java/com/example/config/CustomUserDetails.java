package com.example.config;

import com.example.enums.ProfileRoleEnum;
import com.example.enums.ProfileStatus;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    @Getter
    private String id;
    private String passport_number;
    private String password;
    private ProfileStatus status;
    private List<SimpleGrantedAuthority> roles;

    public CustomUserDetails(String id, String username, String password, ProfileStatus status,
                             List<ProfileRoleEnum> roleList) {
        this.id = id;
        this.passport_number = username;
        this.password = password;
        this.status = status;

        List<SimpleGrantedAuthority> roles = new LinkedList<>();
        roleList.forEach(role -> {
            roles.add(new SimpleGrantedAuthority(role.name()));
        });
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return passport_number;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status.equals(ProfileStatus.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
