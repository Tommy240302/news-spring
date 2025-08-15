package com.ptit.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    // Nội dung giao dịch
    private String content;

    // URL của ảnh giao dịch
    private String image; // Đổi tên từ transactionImageUrl để khớp với controller

    // Số tiền đã thanh toán
    private Double amount;

    // Tổng lượt xem được ghi nhận cho đợt thanh toán này
    private Long viewCurrent; // Đổi kiểu từ Integer sang Long để khớp với controller

    // Số tài khoản
    private String accountNumber; // Thêm trường bị thiếu

    // Ngày giao dịch
    private String transactionDate; // Thêm trường bị thiếu

    // Danh sách ID của các bài viết đã được thanh toán
    private List<Long> articleIds;
}
