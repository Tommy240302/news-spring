package com.ptit.news.repository;

import com.ptit.news.common.enums.RequestAuthorStatus;
import com.ptit.news.entity.RequestAuthor;
import com.ptit.news.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestAuthorRepository extends JpaRepository<RequestAuthor, Long> {
    Optional<RequestAuthor> findByUserAndStatusIn(User user, List<RequestAuthorStatus> statuses);
}
