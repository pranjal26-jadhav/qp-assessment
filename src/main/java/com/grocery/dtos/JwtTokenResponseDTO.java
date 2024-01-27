package com.grocery.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtTokenResponseDTO {
    private String jwtToken;
}
