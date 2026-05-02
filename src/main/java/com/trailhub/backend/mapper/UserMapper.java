package com.trailhub.backend.mapper;

import com.trailhub.backend.dto.user.UserSummaryDto;
import com.trailhub.backend.model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserSummaryDto toSummaryDto(AppUser user) {
        UserSummaryDto dto = new UserSummaryDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
