package com.ptit.news.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ptit.news.dto.PaymentDTO;
import com.ptit.news.dto.PaymentDetailDTO;
import com.ptit.news.dto.TransactionDTO;
import com.ptit.news.entity.Payment;
import com.ptit.news.entity.User;
import com.ptit.news.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/admin/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Các endpoint khác không thay đổi...
    @GetMapping("/authors")
    public ResponseEntity<List<User>> getAllAuthors() {
        List<User> authors = paymentService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/royalty-details/{authorId}")
    public ResponseEntity<List<PaymentDetailDTO>> getRoyaltyDetails(@PathVariable Long authorId) {
        List<PaymentDetailDTO> details = paymentService.exportRoyaltyDetails(authorId);
        return ResponseEntity.ok(details);
    }

    @PostMapping("/add-transaction/{authorId}")
    public ResponseEntity<Payment> addTransaction(
            @PathVariable Long authorId,
            @RequestParam("content") String content,
            @RequestParam("amount") Double amount,
            @RequestParam("viewCurrent") Long viewCurrent,
            @RequestParam("accountNumber") String accountNumber,
            @RequestParam("transactionDate") String transactionDate,
            @RequestParam("articleIds") List<Long> articleIds,
            @RequestParam("image") String imageUrl // Nhận URL ảnh dưới dạng String
    ) {
        try {
            // Tạo đối tượng TransactionDTO từ các tham số
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setContent(content);
            transactionDTO.setAmount(amount);
            transactionDTO.setViewCurrent(viewCurrent);
            transactionDTO.setAccountNumber(accountNumber);
            transactionDTO.setTransactionDate(transactionDate);
            transactionDTO.setArticleIds(articleIds);
            transactionDTO.setImage(imageUrl);

            Payment payment = paymentService.addTransaction(authorId, transactionDTO);
            return ResponseEntity.ok(payment);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            // Log lỗi chi tiết để dễ dàng gỡ lỗi
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<PaymentDTO>> getTransactionHistory() {
        List<PaymentDTO> history = paymentService.getTransactionHistory();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/calculate")
    public ResponseEntity<List<Map<String, Object>>> getCalculateSalary() {

        return ResponseEntity.ok(paymentService.getCalculateSalary());
    }
}
