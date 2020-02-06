package com.bmworks.rabbit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SalesOrderEndpoints {
    private Cart cart;
    private RestTemplate restTemplate;
    private Receiver receiver;

    public SalesOrderEndpoints(Cart cart, Receiver receiver) {
        this.cart = cart;
        this.restTemplate = new RestTemplate();
        this.receiver = receiver;
    }

    @RequestMapping(path = "/addToCart", method = RequestMethod.POST)
    public AddToCartResponse addToCart(@RequestBody AddToCartRequest addToCartRequest) {
        String url = "http://" + receiver.getStockManagementHost() + "/getStock?" + "productId=" + addToCartRequest.productId;
        Integer stock = restTemplate.getForObject(url, Integer.class);
        AddToCartResponse addToCartResponse = new AddToCartResponse();
        addToCartResponse.hasEnoughInStock = true;
        return addToCartResponse;
    }
}
