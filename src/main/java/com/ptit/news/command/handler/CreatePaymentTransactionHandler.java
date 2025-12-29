package com.ptit.news.command.handler;

import com.ptit.news.command.dto.CreatePaymentTransactionCommand;
import com.ptit.news.common.Response;
import com.ptit.news.common.enums.PaymentsStatus;
import com.ptit.news.entity.PaymentsTransaction;
import com.ptit.news.repository.PaymentTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CreatePaymentTransactionHandler {

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @CommandHandler
    public Response<PaymentsTransaction> handle(CreatePaymentTransactionCommand command) {
        List<PaymentsTransaction> paymentsTransactions = paymentTransactionRepository.findAll().stream().filter(p->p.getStatus() == PaymentsStatus.PENDING).toList();
        // if (!paymentsTransactions.isEmpty()) {
        //     return Response.Error("Vẫn còn hóa đơn chưa hoàn thành");
        // }
        PaymentsTransaction paymentsTransaction =
                PaymentsTransaction.builder()
                        .jsonData(command.getJsonData())
                        .status(PaymentsStatus.PENDING)
                        .build();
        paymentTransactionRepository.save(paymentsTransaction);
        return Response.Success(paymentsTransaction,"Tạo thành công");
    }
}
