CREATE TABLE return_request (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    order_item_id BIGINT,
    customer_id BIGINT NOT NULL,
    reason VARCHAR(255) NOT NULL,
    comment TEXT,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_return_request_order ON return_request(order_id);
CREATE INDEX idx_return_request_customer ON return_request(customer_id);
