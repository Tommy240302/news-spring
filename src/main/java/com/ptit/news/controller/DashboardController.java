package com.ptit.news.controller;

import com.ptit.news.common.Response;
import com.ptit.news.dto.DashboardSummaryDTO;
import com.ptit.news.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard") // Endpoint cho Dashboard, dành cho Admin
@RequiredArgsConstructor
public class DashboardController extends AdvancedBaseController { // Kế thừa AdvancedBaseController

    private final DashboardService dashboardService;

    /**
     * Lấy dữ liệu tổng quan cho Dashboard.
     * Endpoint: GET /api/admin/dashboard/summary
     *
     * @return ResponseEntity chứa DashboardSummaryDTO.
     */
    @GetMapping("/summary")
    public ResponseEntity<Response<DashboardSummaryDTO>> getDashboardSummary() {
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary();
        return success(summary, "Lấy dữ liệu Dashboard tổng quan thành công.");
    }
}