package com.ptit.news.repository;

import com.ptit.news.common.enums.PaymentsStatus;
import com.ptit.news.entity.PaymentsTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentTransactionRepository  extends JpaRepository<PaymentsTransaction,Long> {
}
