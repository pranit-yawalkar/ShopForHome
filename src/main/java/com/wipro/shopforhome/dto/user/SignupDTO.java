package com.wipro.shopforhome.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class SignupDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
