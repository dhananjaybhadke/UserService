package com.project.userservice.security.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.project.userservice.model.Roles;
import org.springframework.security.core.GrantedAuthority;

@JsonDeserialize
public class CustomGrantedAuthority implements GrantedAuthority  {
    //Role <=> GrantedAuthority
    private String authority;

    public CustomGrantedAuthority() {

    }

    public CustomGrantedAuthority(Roles role) {
        this.authority = role.getName();
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
