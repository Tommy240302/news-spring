package com.ptit.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor // Lombok sẽ tạo constructor này
@Builder
public class AuthorRevenueDTO {
    private Long authorId;
    private String authorEmail;
    private String authorFirstName;
    private String authorLastName;
    private Double totalRevenue;

    // Xóa constructor thủ công này:
    // public AuthorRevenueDTO(Long authorId, String authorEmail, String authorFirstName, String authorLastName, Double totalRevenue) {
    //     this.authorId = authorId;
    //     this.authorEmail = authorEmail;
    //     this.authorFirstName = authorFirstName;
    //     this.authorLastName = authorLastName;
    //     this.totalRevenue = totalRevenue;
    // }
}