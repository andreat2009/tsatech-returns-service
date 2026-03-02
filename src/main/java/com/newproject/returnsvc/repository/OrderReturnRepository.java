package com.newproject.returnsvc.repository;

import com.newproject.returnsvc.domain.OrderReturn;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderReturnRepository extends JpaRepository<OrderReturn, Long> {
    List<OrderReturn> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<OrderReturn> findByOrderIdOrderByCreatedAtDesc(Long orderId);
}
