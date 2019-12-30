package com.doctordroid.entity.remote.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterRequest {

    private String name;
    private String email;
    private String password;

}
