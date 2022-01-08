package com.programming.techie.orderservice.dto;

import com.programming.techie.orderservice.model.OrderLineItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private List<OrderLineItems> orderLineItemsList;
}
