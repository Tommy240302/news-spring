package com.ptit.news.controller;

import com.ptit.news.dto.ViewTariffRequest;
import com.ptit.news.dto.ViewTariffResponse;
import com.ptit.news.service.ViewTariffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/view-tariffs")
@RequiredArgsConstructor
public class ViewTariffController {

    private final ViewTariffService viewTariffService;

    @GetMapping
    public ResponseEntity<List<ViewTariffResponse>> getAllTariffs() {
        List<ViewTariffResponse> tariffs = viewTariffService.getAllTariffs();
        return ResponseEntity.ok(tariffs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViewTariffResponse> getTariffById(@PathVariable Long id) {
        ViewTariffResponse tariff = viewTariffService.getTariffById(id);
        return ResponseEntity.ok(tariff);
    }

    @PostMapping
    public ResponseEntity<ViewTariffResponse> createTariff( @RequestBody ViewTariffRequest request) {
        ViewTariffResponse createdTariff = viewTariffService.createTariff(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTariff);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViewTariffResponse> updateTariff(
            @PathVariable Long id,
            @Valid @RequestBody ViewTariffRequest request) {
        ViewTariffResponse updatedTariff = viewTariffService.updateTariff(id, request);
        return ResponseEntity.ok(updatedTariff);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTariff(@PathVariable Long id) {
        viewTariffService.deleteTariff(id);
        return ResponseEntity.noContent().build();
    }
}
