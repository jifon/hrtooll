package com.example.hrtool.controller;


import com.example.hrtool.dto.AuthRequest;
import com.example.hrtool.dto.ExternalAuthResponse;
import com.example.hrtool.dto.InternalAuthResponse;
import com.example.hrtool.dto.RegisterRequest;
import com.example.hrtool.payload.BaseResponse;
import com.example.hrtool.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${hrtool.refreshToken.maxAge}")
    private int maxAge;

    @PostMapping("/register")
    public ResponseEntity<ExternalAuthResponse> register(@RequestBody RegisterRequest request) {

//        System.out.println("auth/register called");

        try{
            InternalAuthResponse authresponse = authService.register(request);

            ExternalAuthResponse externalAuthResponse = new ExternalAuthResponse("", "", "");
            externalAuthResponse.setFullName(authresponse.getFullName());
            externalAuthResponse.setRole(authresponse.getRole());
            externalAuthResponse.setAccessToken(authresponse.getAccessToken());

            ResponseCookie cookie = ResponseCookie
                    .from("refreshToken", authresponse.getRefreshToken())
                    .path("/auth/refresh")
                    .maxAge(maxAge)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(externalAuthResponse);
        }catch (Exception e){
            e.printStackTrace();
            ExternalAuthResponse res = new ExternalAuthResponse("", "", "");
            return ResponseEntity.badRequest().body(res);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ExternalAuthResponse> login(@RequestBody AuthRequest request) {

//        System.out.println("auth/login called");
        try{
            InternalAuthResponse authresponse = authService.login(request);

            ExternalAuthResponse externalAuthResponse = new ExternalAuthResponse("", "", "");
            externalAuthResponse.setFullName(authresponse.getFullName());
            externalAuthResponse.setRole(authresponse.getRole());
            externalAuthResponse.setAccessToken(authresponse.getAccessToken());

            ResponseCookie cookie = ResponseCookie
                    .from("refreshToken", authresponse.getRefreshToken())
                    .path("/hr/auth")
                    .maxAge(maxAge)
                    .httpOnly(true)
                    .secure(true) //should be true when deploying
                    .sameSite("Strict")
                    .build();

            ResponseCookie testCookie = ResponseCookie.from("testCookie", "testValue123").build();
            ResponseCookie testCookie2 = ResponseCookie.from("testCookie2", "CookieWithPathAuth").path("/hr/auth").build();
            ResponseCookie testCookie3 = ResponseCookie.from("testCookie3", "CookieWithPathAuthRefresh").path("/hr/auth/refresh").build();
            ResponseCookie testCookie4 = ResponseCookie.from("testCookie4", "HTTPOnly").httpOnly(true).build();
            ResponseCookie testCookie5 = ResponseCookie.from("testCookie5", "SameSite").sameSite("Strict").build();
            ResponseCookie testCookie6 = ResponseCookie.from("testCookie6", "Secure").secure(true).build();
            ResponseCookie testCookie7 = ResponseCookie.from("testCookie7", "HTTPOnlySecure").secure(true).httpOnly(true).build();
            ResponseCookie testCookie8 = ResponseCookie.from("testCookie8", "SameSiteSecure").secure(true).sameSite("Strict").build();
            ResponseCookie testCookie9 = ResponseCookie.from("testCookie9", "SecureSameSiteHttpOny").secure(true).sameSite("Strict").httpOnly(true).build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            headers.add(HttpHeaders.SET_COOKIE, testCookie.toString());
            headers.add(HttpHeaders.SET_COOKIE, testCookie2.toString());
            headers.add(HttpHeaders.SET_COOKIE, testCookie3.toString());
            headers.add(HttpHeaders.SET_COOKIE, testCookie4.toString());
            headers.add(HttpHeaders.SET_COOKIE, testCookie5.toString());
            headers.add(HttpHeaders.SET_COOKIE, testCookie6.toString());
            headers.add(HttpHeaders.SET_COOKIE, testCookie7.toString());
            headers.add(HttpHeaders.SET_COOKIE, testCookie8.toString());
            headers.add(HttpHeaders.SET_COOKIE, testCookie9.toString());

            return ResponseEntity.ok().headers(headers).body(externalAuthResponse);
        }catch (Exception e){
            e.printStackTrace();
            ExternalAuthResponse res = new ExternalAuthResponse("", "", "");
            return ResponseEntity.badRequest().body(res);
        }
    }

    @GetMapping("/refresh")
    public BaseResponse<String> refresh(HttpServletRequest request) {

//        System.out.println("refresh called in authController");

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
//            System.out.println("Cookies: ");
            for (Cookie cookie : cookies) {
//                System.out.println("Name: " + cookie.getName() + ", Wert: " + cookie.getValue());
            }
        } else {
//            System.out.println("Keine Cookies vorhanden.");
        }

        if(cookies == null){
            return new BaseResponse<>(false, "no refreshToken", null);
        }
        Cookie refreshToken = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("refreshToken")).findFirst().orElse(null);
        if(refreshToken == null || refreshToken.getValue() == null){
//            System.out.println(cookies);
            return new BaseResponse<>(false, "no refreshToken", null);
        }else{
//            System.out.println("refreshToken war nicht null, jetzt wird ein neues AccessToken generiert");
        }
        return new BaseResponse<>(true, "new AccessToken", authService.refresh(refreshToken.getValue()));
    }
}

