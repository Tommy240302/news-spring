package com.ptit.news.query.handler;

import com.ptit.news.dto.CategoryResponse;
import com.ptit.news.entity.PaymentsTransaction;
import com.ptit.news.exception.InvalidRequestException;
import com.ptit.news.query.dto.GetAllPaymentTransactionQuery;
import com.ptit.news.repository.PaymentTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllPaymentTransactionQueryHandler {

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @QueryHandler
    public List<PaymentsTransaction> handle(GetAllPaymentTransactionQuery query) {
        try {
            return paymentTransactionRepository.findAll();
        } catch (Exception e) {
            log.error("Lỗi khi lấy Transaction: {}", e.getMessage(), e);
            throw new InvalidRequestException("Không thể lấy danh sách transaction");
        }
    }
}
