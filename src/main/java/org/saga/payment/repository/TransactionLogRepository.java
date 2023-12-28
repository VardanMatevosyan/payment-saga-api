package org.saga.payment.repository;

import java.util.Optional;
import org.saga.payment.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {

  Optional<TransactionLog> findTransactionLogByOrderId(Long orderId);

}

