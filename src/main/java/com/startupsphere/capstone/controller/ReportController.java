package com.startupsphere.capstone.controller;

import com.startupsphere.capstone.dtos.ReportDto;
import com.startupsphere.capstone.entity.Report;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.service.ReportService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/")
    public ResponseEntity<Report> createReport(@RequestBody ReportDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        Report report = reportService.createReport(dto, currentUser);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/")
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Report> updateReport(@PathVariable Integer id, @RequestBody ReportDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        Report updated = reportService.updateReport(id, dto, currentUser);
        return ResponseEntity.ok(updated);
    }

}
