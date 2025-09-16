package com.komal.template_backend.service;

import com.komal.template_backend.Util.OtpUtil;
import com.komal.template_backend.model.Otpentity;
import com.komal.template_backend.repo.OtpRepo;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class OtpService {
    private final OtpRepo otprepo;
    private final MailService mailservice;


    public OtpService(OtpRepo otprepo, MailService mailservice) {
        this.otprepo = otprepo;
        this.mailservice = mailservice;
    }
    public void requestOtp(String email) throws Exception{
        String otp=generateOtp();
        String salt = OtpUtil.genSalt();
        String hash = OtpUtil.hashOtp(otp, salt);
        otprepo.deleteByEmail(email);
        Otpentity otpdoc=new Otpentity(email,hash,salt);
        otprepo.save(otpdoc);
        mailservice.sendOtpMail(email, "Your OTP for Donation", "Your OTP is: " + otp);

    }
    public boolean verifyOtp(String email, String inputOtp) throws Exception {
        Optional<Otpentity> otpOpt = otprepo.findByEmail(email);
        if(otpOpt.isEmpty()) return  false;
        Otpentity otpDoc = otpOpt.get();
        if (otpDoc.getAttempts() >= 5) return false;
        String hash=OtpUtil.hashOtp(inputOtp,otpDoc.getSalt());
        if(hash.equals(otpDoc.getOtpHash())){
            otprepo.delete(otpDoc);
            return true;
        }
        else {
            otpDoc.setAttemps((otpDoc.getAttempts()+1));
            otprepo.save(otpDoc);
            return false;
        }
    }
    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(900000) + 100000;
        return String.valueOf(num);
    }
}
