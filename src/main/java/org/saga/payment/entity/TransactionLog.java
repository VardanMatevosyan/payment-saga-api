package org.saga.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.saga.common.enums.PaymentStatus;

@Entity
@Table(name = "transaction_log", schema = "perches")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "order_id", nullable = false, unique = true)
  Long orderId;

  @Column(name = "event_id", nullable = false, unique = true)
  String eventId;

  @Column(name = "customer_email", nullable = false)
  String customerEmail;

  @Column(name = "price", nullable = false)
  Integer price;

  @Column(name = "payment_date", nullable = false)
  LocalDateTime paymentDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method", nullable = false)
  PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_status", nullable = false)
  PaymentStatus paymentStatus;

}
