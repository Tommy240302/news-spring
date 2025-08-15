package com.ptit.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailDTO {
    // Tiêu đề của bài viết
    private String articleTitle;
    // Tổng lượt xem hiện tại
    private Integer totalViews;
    // Tổng lượt xem đã ghi nhận ở đợt thanh toán trước
    private Integer viewCurrent;
    // Lượt xem tháng này (totalViews - viewCurrent)
    private Integer viewsThisMonth;
    // Tổng số tiền nhuận bút
    private Double royaltyAmount;
    // Số tài khoản của tác giả
    private String bankAccountNumber;
    // Ngày giao dịch
    private Date transactionDate;
}
