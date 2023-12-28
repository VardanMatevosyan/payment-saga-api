CREATE SCHEMA IF NOT EXISTS perches;

DO $$
    BEGIN
        IF
            NOT EXISTS(SELECT 1 FROM pg_catalog.pg_type WHERE typname = 'perches_payment_status')
            THEN CREATE TYPE perches.perches_payment_status AS ENUM ('PAYMENT_COMPLETED', 'PAYMENT_REJECTED', 'PAYMENT_PENDING');
        END IF;
        IF
            NOT EXISTS(SELECT 1 FROM pg_catalog.pg_type WHERE typname = 'perches_payment_method')
        THEN CREATE TYPE perches.perches_payment_method AS ENUM ('VISA', 'MASTER_CARD', 'PAY_PAL', 'GOOGLE_PAY');
        END IF;
    END
$$;


CREATE TABLE IF NOT EXISTS perches.transaction_log (
    id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    order_id BIGINT NOT NULL,
    event_id VARCHAR(150) NOT NULL,
    customer_email VARCHAR(150) NOT NULL,
    price INT NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    payment_method perches.perches_payment_method NOT NULL,
    payment_status perches.perches_payment_status NOT NULL DEFAULT 'PAYMENT_PENDING'::perches.perches_payment_status,
    CONSTRAINT transaction_log_id_pk PRIMARY KEY (id),
    CONSTRAINT transaction_log_order_id_uq UNIQUE (order_id),
    CONSTRAINT transaction_log_event_id_uq UNIQUE (event_id)
);

CREATE INDEX IF NOT EXISTS perches_transaction_log_customer_email on perches.transaction_log (customer_email);


