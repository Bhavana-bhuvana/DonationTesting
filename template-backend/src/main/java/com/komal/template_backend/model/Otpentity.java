package com.komal.template_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection="Otps")
public class Otpentity {
    @Id
    private String id;
    private String email;
    private String otpHash;
    private String salt;
    private int attempts;
    @Indexed(name = "otp_ttl_idx", expireAfterSeconds = 300)  // ⏳ Mongo will auto-delete after 5 mins
     private Date createdAt;
    public Otpentity(String email, String otpHash, String salt) {
        this.email = email;
        this.salt = salt;
        this.otpHash = otpHash;
        attempts=0;
        this.createdAt= new Date();
    }
    public String getEmail() {
        return email;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public String getSalt() {
        return salt;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttemps(int attemps) {
        this.attempts = attemps;
    }
}
