package com.example.springJWT.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springJWT.entities.Person;
import com.example.springJWT.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public void loginUser () {
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser (@RequestBody Person user) {
        Map<String, String> requestAnswer = userService.registerUser(user);

        if(requestAnswer.containsKey("Error")){
            return ResponseEntity
                    .badRequest()
                    .body(requestAnswer.get("Error"));
        }

        return ResponseEntity.ok().body(requestAnswer.get("Message"));
    }

    @PostMapping("/refresh/token")
    public void refreshToken (HttpServletRequest request,HttpServletResponse response) throws IOException {
        String refreshToken = request.getHeader("RefreshToken");
        if(refreshToken != null) {
            try {
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                Person user = userService.getUserByUsername(username);
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                        .withIssuer(request.getRequestURI().toString())
                        .sign(algorithm);
                Map<String, String> res = new HashMap<>();
                res.put("accessToken", accessToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),res);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error",exception.getMessage());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        } else {
            throw new RuntimeException("Please, resign in!");
        }
    }

    @GetMapping("/error")
    String errorPage(){
        return "error";
    }
}
