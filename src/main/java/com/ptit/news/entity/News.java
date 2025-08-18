package com.ptit.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "news")
public class News extends BaseEntity {
    private String title;
    @Lob
    private String summary;
    private String image;
    @Lob
    private String content;
    private boolean status;
    private Integer views;
    private Date publishedAt;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "news")
    private List<Comment> comments;


}