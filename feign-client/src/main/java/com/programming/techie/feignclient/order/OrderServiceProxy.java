package com.programming.techie.feignclient.order;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="order")
public interface OrderServiceProxy {
}
