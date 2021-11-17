package com.esdo.bepilot.Controller;

import com.esdo.bepilot.Model.Entity.Account;
import com.esdo.bepilot.Model.Request.AccountInputDTO;
import com.esdo.bepilot.Model.Request.AuthRequest;
import com.esdo.bepilot.Model.Response.AuthResponse;
import com.esdo.bepilot.Repository.AccountRepository;
import com.esdo.bepilot.Service.AuthService;
import com.esdo.bepilot.Service.Validate.CheckValid;
import com.esdo.bepilot.Service.jwt.CustomUserDetailsService;
import com.esdo.bepilot.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authenticationRequest) throws Exception {
        return ResponseEntity.ok(authService.login(authenticationRequest));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> saveAccount(@RequestBody AccountInputDTO account) throws Exception {
        return ResponseEntity.ok(userDetailsService.save(account));
    }
}

