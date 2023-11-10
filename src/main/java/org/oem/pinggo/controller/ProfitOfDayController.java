package org.oem.pinggo.controller;

import lombok.RequiredArgsConstructor;
import org.oem.pinggo.service.ProfitOfDayService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/api/profitofday")
@RequiredArgsConstructor
public class ProfitOfDayController {
    private final ProfitOfDayService profitOfDayService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminAccess() {
        return profitOfDayService.findAllProfitRecords();

    }

}
