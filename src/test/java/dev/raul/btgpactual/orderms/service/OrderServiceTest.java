package dev.raul.btgpactual.orderms.service;

import dev.raul.btgpactual.orderms.entity.OrderEntity;
import dev.raul.btgpactual.orderms.listener.dto.OrderCreatedEvent;
import dev.raul.btgpactual.orderms.listener.dto.OrderItemEvent;
import dev.raul.btgpactual.orderms.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldSaveOrderWithCalculatedTotal() {
        // Given
        var item1 = new OrderItemEvent("prod1", 2, BigDecimal.valueOf(50.0));
        var item2 = new OrderItemEvent("prod2", 1, BigDecimal.valueOf(100.0));
        var event = new OrderCreatedEvent(1L, 1L, List.of(item1, item2));

        // When
        orderService.save(event);

        // Then
        var captor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderRepository).save(captor.capture());
        var saved = captor.getValue();

        assertEquals(0, BigDecimal.valueOf(200.0).compareTo(saved.getTotal()));
    }

    @Test
    void shouldFindById() {
        // Given
        var order = new OrderEntity();
        order.setOrderId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // When
        var result = orderService.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().orderId());
    }

    @Test
    void shouldCountByCustomerId() {
        // Given
        when(orderRepository.countByCustomerId(1L)).thenReturn(5L);

        // When
        var result = orderService.countByCustomerId(1L);

        // Then
        assertEquals(5L, result);
    }
}
