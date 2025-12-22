package com.ptit.news.service;


import com.ptit.news.dto.ViewTariffRequest;
import com.ptit.news.dto.ViewTariffResponse;
import com.ptit.news.entity.ViewTariff;
import com.ptit.news.exception.ResourceNotFoundException;
import com.ptit.news.repository.ViewTariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViewTariffService {

    private final ViewTariffRepository viewTariffRepository;

    @Transactional(readOnly = true)
    public List<ViewTariffResponse> getAllTariffs() {
        return viewTariffRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ViewTariffResponse getTariffById(Long id) {
        ViewTariff tariff = viewTariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ViewTariff not found with id: " + id));
        return convertToResponse(tariff);
    }

    @Transactional
    public ViewTariffResponse createTariff(ViewTariffRequest request) {
        validateTariffRange(request);
        ViewTariff tariff = ViewTariff.builder()
                .minView(request.getMinView())
                .maxView(request.getMaxView())
                .pricePerView(request.getPricePerView())
                .description(request.getDescription())
                .build();

        ViewTariff savedTariff = viewTariffRepository.save(tariff);
        return convertToResponse(savedTariff);
    }

    @Transactional
    public ViewTariffResponse updateTariff(Long id, ViewTariffRequest request) {
        ViewTariff tariff = viewTariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ViewTariff not found with id: " + id));

        validateTariffRange(request);

        tariff.setMinView(request.getMinView());
        tariff.setMaxView(request.getMaxView());
        tariff.setPricePerView(request.getPricePerView());
        tariff.setDescription(request.getDescription());

        ViewTariff updatedTariff = viewTariffRepository.save(tariff);
        return convertToResponse(updatedTariff);
    }

    @Transactional
    public void deleteTariff(Long id) {
        if (!viewTariffRepository.existsById(id)) {
            throw new ResourceNotFoundException("ViewTariff not found with id: " + id);
        }
        viewTariffRepository.deleteById(id);
    }

    private void validateTariffRange(ViewTariffRequest request) {
        if(request.getMaxView() == null) return;
        if (request.getMaxView() < request.getMinView()) {
            throw new IllegalArgumentException("Maximum view must be greater than or equal to minimum view");
        }
    }

    private ViewTariffResponse convertToResponse(ViewTariff tariff) {
        return ViewTariffResponse.builder()
                .id(tariff.getId())
                .minView(tariff.getMinView())
                .maxView(tariff.getMaxView())
                .pricePerView(tariff.getPricePerView())
                .description(tariff.getDescription())
                .build();
    }
}
