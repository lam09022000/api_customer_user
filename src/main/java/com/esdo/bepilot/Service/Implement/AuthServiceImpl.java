package com.esdo.bepilot.Service.Implement;

import com.esdo.bepilot.Model.Entity.Account;
import com.esdo.bepilot.Model.Request.AuthRequest;
import com.esdo.bepilot.Model.Response.AuthResponse;
import com.esdo.bepilot.Repository.AccountRepository;
import com.esdo.bepilot.Service.AuthService;
import com.esdo.bepilot.Service.Validate.CheckValid;
import com.esdo.bepilot.Service.jwt.CustomUserDetailsService;
import com.esdo.bepilot.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public AuthResponse login(AuthRequest authenticationRequest) throws Exception {

        CheckValid.checkAuthRequest(authenticationRequest, accountRepository);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        }
        catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        UserDetails userdetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        String token = jwtUtil.generateToken(userdetails);
        Account account = accountRepository.findByEmail(authenticationRequest.getEmail());
        if (account.getEmployee() == null) {
            System.out.println("Không có thông tin nhân viên");
        }
        String name = account.getEmployee().getName();
        String avatar = account.getEmployee().getAvatar();
        String email = account.getEmail();
        String role = account.getRole();
        return new AuthResponse(name, avatar, email, role, token);
    }
}
