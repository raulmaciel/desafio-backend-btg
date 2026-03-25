package dev.raul.btgpactual.orderms.service;

import dev.raul.btgpactual.orderms.controller.dto.OrderResponse;
import dev.raul.btgpactual.orderms.entity.OrderEntity;
import dev.raul.btgpactual.orderms.entity.OrderItem;
import dev.raul.btgpactual.orderms.listener.dto.OrderCreatedEvent;
import dev.raul.btgpactual.orderms.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(OrderCreatedEvent event){
        var entity = new OrderEntity();
        entity.setOrderId(event.codigoPedido());
        entity.setCustomerId(event.codigoCliente());
        entity.setItems(getOrderItems(event));
        entity.setTotal(getTotal(event));

        orderRepository.save(entity);
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest){
        var orders = orderRepository.findAllByCustomerId(customerId, pageRequest);

        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal getTotalAmountByCustomerId(Long customerId) {
        var aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("customerId").is(customerId)),
                Aggregation.group("customerId").sum("total").as("totalAmount")
        );

        var response = mongoTemplate.aggregate(aggregation, "orders", Map.class);

        return response.getUniqueMappedResult() == null ?
                BigDecimal.ZERO :
                new BigDecimal(response.getUniqueMappedResult().get("totalAmount").toString());
    }

    public Long countByCustomerId(Long customerId) {
        return orderRepository.countByCustomerId(customerId);
    }

    private static List<OrderItem> getOrderItems(OrderCreatedEvent event) {
        return event.itens().stream()
                .map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco()))
                .toList();
    }

    private BigDecimal getTotal(OrderCreatedEvent event){
        return event.itens().stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
