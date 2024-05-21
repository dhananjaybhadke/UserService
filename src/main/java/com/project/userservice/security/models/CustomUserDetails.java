package com.project.userservice.security.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.project.userservice.model.Roles;
import com.project.userservice.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JsonDeserialize
public class CustomUserDetails implements UserDetails  {
    private String password; // password
    private String username; // email
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private List<CustomGrantedAuthority> authorities; // List<Roles>
    private Long userId;

    public CustomUserDetails() {

    }

    public CustomUserDetails(User user) {
        this.username = user.getEmail();
        this.password = user.getHashedPassword();
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.accountNonLocked = true;
        this.enabled = true;

        //User object has List<Role>
        //We need to convert List<Role> into List<CustomGrantedAuthority>
        this.authorities = new ArrayList<>();

        for (Roles role : user.getRoles()) {
            authorities.add(new CustomGrantedAuthority(role));
        }
        this.userId = user.getId();
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
