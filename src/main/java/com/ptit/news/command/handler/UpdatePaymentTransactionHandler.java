package com.ptit.news.command.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptit.news.command.dto.UpdatePaymentTransactionCommand;
import com.ptit.news.common.Response;
import com.ptit.news.common.enums.PaymentsStatus;
import com.ptit.news.dto.PaymentResponse;
import com.ptit.news.entity.Payment;
import com.ptit.news.entity.PaymentsTransaction;
import com.ptit.news.entity.User;
import com.ptit.news.exception.InvalidRequestException;
import com.ptit.news.repository.PaymentRepository;
import com.ptit.news.repository.PaymentTransactionRepository;
import com.ptit.news.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UpdatePaymentTransactionHandler {

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @CommandHandler
    public Response<PaymentsTransaction> handle(UpdatePaymentTransactionCommand command) throws JsonProcessingException {
        PaymentsTransaction paymentsTransaction = paymentTransactionRepository.findById(command.getId()).orElse(null);
        if (paymentsTransaction  == null || paymentsTransaction.getStatus() != PaymentsStatus.PENDING) {
            return Response.Error("Invalid Transaction");
        }
        if (command.getStatus() == PaymentsStatus.COMPLETE) {
            ObjectMapper mapper = new ObjectMapper();
            List<PaymentResponse> paymentResponse = mapper.readValue(paymentsTransaction.getJsonData(), new TypeReference<List<PaymentResponse>>() {
            });
            paymentResponse.forEach(p->{
                User author = userRepository.findById(p.getUserId()).orElse(null);
                if (author == null) {
                    throw new InvalidRequestException("Invalid User");
                }
                Payment payment = Payment.builder()
                        .author(author)
                        .amount(p.getAmount())
                        .viewCurrent(p.getViewCurrent())
                        .build();
                paymentRepository.save(payment);
            });
        }
        paymentsTransaction.setStatus(command.getStatus());
        paymentTransactionRepository.save(paymentsTransaction);

        return Response.Success(paymentsTransaction, "Thay đổi thành công");

    }
}
