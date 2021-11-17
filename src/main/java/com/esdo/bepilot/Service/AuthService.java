package com.esdo.bepilot.Service;

import com.esdo.bepilot.Model.Request.AuthRequest;
import com.esdo.bepilot.Model.Response.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest authenticationRequest) throws Exception;
}
