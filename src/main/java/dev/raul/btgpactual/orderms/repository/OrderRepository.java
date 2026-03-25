package dev.raul.btgpactual.orderms.repository;

import dev.raul.btgpactual.orderms.controller.dto.OrderResponse;
import dev.raul.btgpactual.orderms.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {
    Page<OrderEntity> findAllByCustomerId(Long customerId, PageRequest pageRequest);

    Long countByCustomerId(Long customerId);

}
