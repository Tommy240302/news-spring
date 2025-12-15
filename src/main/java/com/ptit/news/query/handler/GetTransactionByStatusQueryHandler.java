package com.ptit.news.query.handler;

import com.ptit.news.common.Response;
import com.ptit.news.common.enums.PaymentsStatus;
import com.ptit.news.entity.PaymentsTransaction;
import com.ptit.news.exception.InvalidRequestException;
import com.ptit.news.query.dto.GetTransactionByStatusQuery;
import com.ptit.news.repository.PaymentTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GetTransactionByStatusQueryHandler {

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @QueryHandler
    public List<PaymentsTransaction> handler(GetTransactionByStatusQuery query) {


        return paymentTransactionRepository.findAll()
                .stream()
                .filter(p->p.getStatus() == query.getStatus())
                .toList();
    }
}
