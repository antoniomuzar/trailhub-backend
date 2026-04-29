package com.trailhub.backend.dto.user;

import lombok.Data;

@Data
public class UserSummaryDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;


}
