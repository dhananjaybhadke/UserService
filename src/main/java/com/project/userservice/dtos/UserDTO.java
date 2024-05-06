package com.project.userservice.dtos;

import com.project.userservice.model.Roles;
import com.project.userservice.model.User;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    private String name;
    private String email;
    @ManyToMany
    private List<Roles> roles;
    private boolean isEmailVerified;

    public static UserDTO from(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setEmailVerified(user.isEmailVerified());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
