package com.ptit.news.controller;

import com.ptit.news.command.dto.CreatePaymentTransactionCommand;
import com.ptit.news.command.dto.UpdatePaymentTransactionCommand;
import com.ptit.news.common.Response;
import com.ptit.news.common.enums.PaymentsStatus;
import com.ptit.news.entity.PaymentsTransaction;
import com.ptit.news.query.dto.GetAllPaymentTransactionQuery;
import com.ptit.news.query.dto.GetTransactionByStatusQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/transactions")
public class PaymentTransactionController extends  AdvancedBaseController {

    @PostMapping
    public ResponseEntity<Response<PaymentsTransaction>> createTransaction(@RequestBody CreatePaymentTransactionCommand command) {
        return executeCommandWithCustomStatus(command, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response<PaymentsTransaction>> updateStatus(@PathVariable Long id, @RequestParam PaymentsStatus status) {
        UpdatePaymentTransactionCommand command = UpdatePaymentTransactionCommand.builder()
                .id(id)
                .status(status)
                .build();

        return executeCommandWithCustomStatus(command, HttpStatus.OK);
    }

    @GetMapping
    public Response<List<PaymentsTransaction>> getAllTransaction() {
        List<PaymentsTransaction> paymentsTransactions = queryGateway.query(new GetAllPaymentTransactionQuery(), ResponseTypes.multipleInstancesOf(PaymentsTransaction.class)).join();
        return Response.Success(paymentsTransactions, "Lấy dữ liệu thành công");
    }

    @GetMapping("{status}")
    public Response<List<PaymentsTransaction>> getAllTransactionByStatus(@PathVariable PaymentsStatus status) {
        GetTransactionByStatusQuery query = GetTransactionByStatusQuery.builder().status(status).build();
        List<PaymentsTransaction> paymentsTransactions = queryGateway.query(query, ResponseTypes.multipleInstancesOf(PaymentsTransaction.class)).join();
        return Response.Success(paymentsTransactions, "Lấy dữ liệu thành công");

    }

}
