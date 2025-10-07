package com.ptit.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"categories", "comments"})
@ToString(exclude = {"categories", "comments", "author"})
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

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<NewsCategory> categories = new HashSet<>();

    @OneToMany(mappedBy = "news")
    private List<Comment> comments;

    public void addCategory(NewsCategory newsCategory) {
        this.categories.add(newsCategory);
        newsCategory.setNews(this);
    }


}