package com.smart.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Controller
@RequestMapping("/user")
public class PaymentController {
	
	Order orderCreated=null;
	
	//create order for payment handler
    @PostMapping("/create-order")
    @ResponseBody
    public String paymentGateway(@RequestBody Map<String, Object> data) {
    	/*try {
    		  JSONObject orderRequest = new JSONObject();
    		  orderRequest.put("amount", 50000); // amount in the smallest currency unit
    		  orderRequest.put("currency", "INR");
    		  orderRequest.put("receipt", "order_rcptid_11");

    		  Order order = orderRequest.Orders.create(orderRequest);
    		} catch (RazorpayException e) {
    		  // Handle Exception
    		  System.out.println(e.getMessage());
    		}*/
    	System.out.println("data "+data);
    	System.out.println("order function executed");
    	int amount=Integer.parseInt(data.get("amount").toString());
    	try {
    		//RazorpayClient razorpayClient = new RazorpayClient("rzp_test_haDRsjIQo9vFPJ", "owKJJes2fwE6YD6DToishFuH");
			var razorpayClient=new RazorpayClient("rzp_test_WARgFgiz3icdKP", "nsgRiBWTYe79e3dHcFmBN2uh");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("amount", amount*100);// amount in the smallest currency unit(here, amount is in paisa not rupee)
			jsonObject.put("currency", "INR");
			jsonObject.put("receipt", "order_rcptid_11");
			
			//creating new order
			orderCreated = razorpayClient.orders.create(jsonObject);
			System.out.println(orderCreated);
			
			//here we can perform save operation to save the detail of order  
		} catch (RazorpayException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
		}
    	
    	return orderCreated.toString();
    }
    
}
