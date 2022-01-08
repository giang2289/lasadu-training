package com.programming.techie.feignclient.inventory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="inventory")
public interface InventoryServiceProxy {
    @GetMapping(value= "/api/inventory/{skuCode}")
    Boolean checkStock(@PathVariable String skuCode);

    @PostMapping( value= "/api/inventory")
    String testFeign();


}
