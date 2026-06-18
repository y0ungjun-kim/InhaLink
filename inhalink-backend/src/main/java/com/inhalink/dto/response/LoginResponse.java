package com.inhalink.dto.response;

import com.inhalink.domain.User;
import lombok.Getter;

@Getter
public class LoginResponse {
    private final String token;
    private final UserProfileResponse profile;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.profile = new UserProfileResponse(user);
    }
}
