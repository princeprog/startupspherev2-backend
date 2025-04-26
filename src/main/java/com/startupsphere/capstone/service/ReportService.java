package com.startupsphere.capstone.service;

import com.startupsphere.capstone.dtos.ReportDto;
import com.startupsphere.capstone.entity.Report;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report createReport(ReportDto dto, User user) {
        Report report = new Report();
        report.setName(dto.getName());
        report.setContent(dto.getContent());
        report.setTimestamp(dto.getTimestamp());
        report.setUserId(user);

        return reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        List<Report> reports = new ArrayList<>();
        reportRepository.findAll().forEach(reports::add);
        return reports;
    }

    public Report updateReport(Integer reportId, ReportDto dto, User currentUser) {
        Report existing = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    
        // Make sure the report belongs to the current user
        if (!existing.getUserId().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized to update this report");
        }
    
        existing.setName(dto.getName());
        existing.setContent(dto.getContent());
        existing.setTimestamp(dto.getTimestamp());
    
        return reportRepository.save(existing);
    }
    
}
