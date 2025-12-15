package com.ptit.news.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "view_tariff")
public class ViewTariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_view")
    private Long minView;

    @Column(name = "max_view")
    private Long maxView;

    @Column(name = "price_per_view")
    private Double pricePerView;

    private String description;
}
