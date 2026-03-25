package dev.raul.btgpactual.orderms.controller;

import dev.raul.btgpactual.orderms.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void shouldReturnOrders() throws Exception {
        when(orderService.findAllByCustomerId(anyLong(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/customers/1/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnSummary() throws Exception {
        when(orderService.getTotalAmountByCustomerId(anyLong())).thenReturn(BigDecimal.TEN);
        when(orderService.countByCustomerId(anyLong())).thenReturn(1L);

        mockMvc.perform(get("/customers/1/summary")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOrderById() throws Exception {
        when(orderService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
