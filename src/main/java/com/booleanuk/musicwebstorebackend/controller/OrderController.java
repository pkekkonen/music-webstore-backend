package com.booleanuk.musicwebstorebackend.controller;

import com.booleanuk.musicwebstorebackend.model.*;
import com.booleanuk.musicwebstorebackend.repository.*;
import com.booleanuk.musicwebstorebackend.payload.response.ErrorResponse;
import com.booleanuk.musicwebstorebackend.payload.response.Response;
import com.booleanuk.musicwebstorebackend.payload.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderLineRepository orderLineRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{id}/orders")
    public ResponseEntity<Response<?>> getAllOrders(@PathVariable int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No user with this ID exists"));
        }
        List<Order> allOrders = this.orderRepository.findAllByUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(allOrders));
    }

    @GetMapping("/{id}/currentOrder")
    public ResponseEntity<Response<?>> getCurrentOrder(@PathVariable int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No user with this ID exists"));
        }
        Order order = orderRepository.findByUserAndDate(user, null).orElse(null);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User has no open order"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(order));
    }

    @PutMapping("/{id}/currentOrder/checkout")
    public ResponseEntity<Response<?>> checkoutCurrentOrder(@PathVariable int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No user with this ID exists"));
        }
        Order order = orderRepository.findByUserAndDate(user, null).orElse(null);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User has no open order"));
        }

        order.setDate(OffsetDateTime.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(orderRepository.save(order)));
    }

    @GetMapping("/{id}/orders/{order_id}")
    public ResponseEntity<Response<?>> getOrder(@PathVariable int id, @PathVariable int order_id) {
        Order order = findOrder(order_id);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(order));
    }

    @PostMapping("/{id}/orders")
    public ResponseEntity<Response<?>> createOrder(@PathVariable int id, @RequestBody Order order) {
        User user = userRepository.findById(id).orElse(null);
        if (order == null || user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }
        order.setUser(user);
        Order createdOrder = orderRepository.save(order);

        Response<Order> response = new SuccessResponse<>(createdOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}/orders/{order_id}")
    public ResponseEntity<Response<?>> deleteOrder(@PathVariable int id, @PathVariable int order_id) {
        Order orderToDelete = findOrder(order_id);

        if (orderToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }
        List<OrderLine> orderLines = orderToDelete.getOrderLine();
        orderLineRepository.deleteAll(orderLines);
        orderRepository.delete(orderToDelete);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(orderToDelete));
    }

    @PutMapping("/{id}/orders/{order_id}")
    public ResponseEntity<Response<?>> updateOrder(@PathVariable int id, @PathVariable int order_id,
            @RequestBody Order order) {
        Order orderToUpdate = findOrder(order_id);
        if (orderToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        if (order.getDate() != null) {
            orderToUpdate.setDate(order.getDate());
        }
        if (order.getUser() != null) {
            orderToUpdate.setUser(order.getUser());
        }
        if (order.getOrderLine() != null) {
            List<OrderLine> list = new ArrayList<>();
            List<OrderLine> oldOrderLines = orderToUpdate.getOrderLine();
            orderLineRepository.deleteAll(oldOrderLines);
            List<OrderLine> newOrderLines = order.getOrderLine();
            for (OrderLine orderLine : newOrderLines) {
                OrderLine newOrderLine = new OrderLine();

                int product_id = orderLine.getProduct().getId();
                System.out.println(product_id);
                Product p = productRepository.findById(product_id).orElse(null);
                System.out.println(p);
                if(p == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));

                }
                newOrderLine.setProduct(p);
                newOrderLine.setOrder(orderToUpdate);
                newOrderLine.setQuantity(orderLine.getQuantity());
                list.add(orderLineRepository.save(newOrderLine));
            }
            orderToUpdate.setOrderLine(list);

        }
        Order savedOrder = orderRepository.save(orderToUpdate);
        Order response = savedOrder;
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(response));

    }

    private Order findOrder(int id) {
        return orderRepository.findById(id).orElse(null);
    }

}
