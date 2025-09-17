package com.komal.template_backend.controller;

import com.komal.template_backend.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
//@CrossOrigin(origins = "http://localhost:5173")
public class OtpMailController {
    private final OtpService otpService;


    public OtpMailController(OtpService otpService) {
        this.otpService = otpService;
    }
    @PostMapping("/request")
    public ResponseEntity<String> requestOtp(@RequestParam String email){
        try{
            otpService.requestOtp(email);
            return ResponseEntity.ok("OTP sent to "+email);
        } catch (Exception e) {
return ResponseEntity.status(500) .body("error"+e.getMessage())  ;     }
    }
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam String email,@RequestParam String otp){
        try {
            boolean success = otpService.verifyOtp(email, otp);
            return success ? ResponseEntity.ok("Otp verified successfully") :
                    ResponseEntity.badRequest().body("invalied otp or expired");
        }catch (Exception e) {
                return ResponseEntity.status(500).body("Error: " + e.getMessage());
            }
        }
    }


