package com.grocery.handlers;

import com.grocery.dtos.GenerateJWTTokenDTO;
import com.grocery.dtos.JwtTokenResponseDTO;
import com.grocery.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/token")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping(value = "/generate-token")
    public ResponseEntity<JwtTokenResponseDTO> generateJwtToken(@RequestBody GenerateJWTTokenDTO tokenDto) {
        try {
            log.debug("Generating token for:{}", tokenDto.getUserName());
            final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(tokenDto.getUserName(),
                    tokenDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = tokenProvider.genrateToken(authentication);
            return ResponseEntity.ok(JwtTokenResponseDTO.builder().jwtToken(jwtToken).build());
        } catch (Exception e) {
            log.error("Error in generating token: {}", e.getMessage(), e);
        }
        //TODO: Need to handle exceptions
        return null;
    }
}

