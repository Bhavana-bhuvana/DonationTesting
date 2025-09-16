package com.komal.template_backend.service;

import com.komal.template_backend.Util.AESUtil;
import com.komal.template_backend.model.Donourentity;
import com.komal.template_backend.repo.DonationRepo;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

import static com.komal.template_backend.Util.AESUtil.decrypt;

@Service
public class DonationService {
    private final DonationRepo donationRepo;

    public DonationService(DonationRepo donationRepo) {
        this.donationRepo = donationRepo;
    }

    //  Encrypt before saving
    public Donourentity saveDonation(Donourentity donor) throws Exception {
        donor.setEmail(AESUtil.encrypt(donor.getEmail()));
        donor.setMobile(AESUtil.encrypt(donor.getMobile()));
        donor.setUniqueId(AESUtil.encrypt(donor.getUniqueId()));
        donor.setBankName(AESUtil.encrypt(donor.getBankName()));
        donor.setIfsc(AESUtil.encrypt(donor.getIfsc()));
        donor.setAccountNumber(AESUtil.encrypt(donor.getAccountNumber()));
        return donationRepo.save(donor);
    }

    // ✅ Helper: check if string looks like Base64
    private boolean isBase64(String value) {
        if (value == null) return false;
        try {
            Base64.getDecoder().decode(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // ✅ Decrypt only encrypted values
    public List<Donourentity> getAllDonors() {
        List<Donourentity> donors = donationRepo.findAll();

        donors.forEach(d -> {
            try {
                if (isBase64(d.getEmail())) d.setEmail(decrypt(d.getEmail()));
                if (isBase64(d.getMobile())) d.setMobile(decrypt(d.getMobile()));
                if (isBase64(d.getUniqueId())) d.setUniqueId(decrypt(d.getUniqueId()));
                if (isBase64(d.getBankName())) d.setBankName(decrypt(d.getBankName()));
                if (isBase64(d.getIfsc())) d.setIfsc(decrypt(d.getIfsc()));
                if (isBase64(d.getAccountNumber())) d.setAccountNumber(decrypt(d.getAccountNumber()));
            } catch (Exception e) {
                System.err.println("Skipping decryption for record: " + e.getMessage());
            }
        });

        return donors;
    }
}
