package com.ptit.news.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptit.news.dto.PaymentDetailDTO;
import com.ptit.news.dto.TransactionDTO;
import com.ptit.news.entity.News;
import com.ptit.news.entity.Payment;
import com.ptit.news.entity.User;
import com.ptit.news.repository.NewsRepository;
import com.ptit.news.repository.PaymentRepository;
import com.ptit.news.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PaymentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private static final double ROYALTY_RATE_PER_VIEW = 5000.0;

    public List<User> getAllAuthors() {
        return userRepository.findByRoles_Name("author");
    }

    public List<News> getArticlesByAuthor(Long authorId) {
        Optional<User> authorOptional = userRepository.findById(authorId);
        if (authorOptional.isPresent()) {
            return newsRepository.findByAuthor(authorOptional.get());
        }
        return List.of();
    }

    public List<PaymentDetailDTO> exportRoyaltyDetails(Long authorId) {
        Optional<User> authorOptional = userRepository.findById(authorId);
        if (authorOptional.isEmpty()) {
            return List.of();
        }
        User author = authorOptional.get();
        List<News> articles = newsRepository.findByAuthor(author);

        Integer previousViewCurrent = paymentRepository.findFirstByAuthorOrderByCreatedAtDesc(author)
                .map(Payment::getViewCurrent)
                .orElse(0);

        Integer totalCurrentViews = articles.stream().mapToInt(News::getViews).sum();
        Integer viewsThisMonth = totalCurrentViews - previousViewCurrent;
        Double royaltyAmount = viewsThisMonth * ROYALTY_RATE_PER_VIEW;

        return articles.stream().map(article -> {
            return new PaymentDetailDTO(
                    article.getTitle(),
                    article.getViews(),
                    previousViewCurrent,
                    viewsThisMonth,
                    royaltyAmount,
                    author.getPaymentNumber(),
                    new Date()
            );
        }).collect(Collectors.toList());
    }

    //-------------------------------------------------------------
    // ĐÃ SỬA LỖI TRONG PHƯƠNG THỨC NÀY
    //-------------------------------------------------------------
    public Payment addTransaction(Long authorId, TransactionDTO transactionDTO) throws JsonProcessingException {
        Optional<User> authorOptional = userRepository.findById(authorId);
        if (authorOptional.isPresent()) {
            User author = authorOptional.get();
            Payment payment = new Payment();
            payment.setAuthor(author);

            // Sửa lỗi: Đặt nội dung giao dịch vào trường contentPm
            payment.setContentPm(transactionDTO.getContent());

            // Sửa lỗi: Gọi phương thức getImage()
            payment.setImagePm(transactionDTO.getImage());

            payment.setAmount(transactionDTO.getAmount());

            // Sửa lỗi: Chuyển đổi kiểu dữ liệu của viewCurrent từ Long sang Integer
            if (transactionDTO.getViewCurrent() != null) {
                payment.setViewCurrent(transactionDTO.getViewCurrent().intValue());
            } else {
                payment.setViewCurrent(0); // Đặt giá trị mặc định nếu null
            }


            return paymentRepository.save(payment);
        }
        throw new IllegalArgumentException("Không tìm thấy tác giả với ID: " + authorId);
    }

    public List<Payment> getTransactionHistory(Long authorId) {
        Optional<User> authorOptional = userRepository.findById(authorId);
        if (authorOptional.isPresent()) {
            return paymentRepository.findByAuthor(authorOptional.get());
        }
        return List.of();
    }
}
