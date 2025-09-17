package com.komal.template_backend.controller;

import com.komal.template_backend.model.Donourentity;
import com.komal.template_backend.service.DonationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
//@CrossOrigin(origins = "http://localhost:5173")
public class DonationController {
    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }
    @PostMapping("/save")
    public Donourentity saveDonation(@RequestBody Donourentity donor) throws Exception {
        return donationService.saveDonation(donor);
    }
    @GetMapping("/all")
    public List<Donourentity> getAllDonors() {
        return donationService.getAllDonors();
    }
}

