package dev.raul.btgpactual.orderms.controller;

import dev.raul.btgpactual.orderms.controller.dto.ApiResponse;
import dev.raul.btgpactual.orderms.controller.dto.OrderResponse;
import dev.raul.btgpactual.orderms.controller.dto.PaginationResponse;
import dev.raul.btgpactual.orderms.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable Long customerId,
                                                    @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){

        Page<OrderResponse> pageResponseOrders = orderService.findAllByCustomerId(customerId, PageRequest.of(page, pageSize));

        return ResponseEntity.ok(new ApiResponse<>(
                pageResponseOrders.getContent(),
                PaginationResponse.fromPage(pageResponseOrders)
        ));
    }

}
