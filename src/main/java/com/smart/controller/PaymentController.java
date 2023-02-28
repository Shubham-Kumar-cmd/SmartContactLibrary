package com.smart.controller;

import java.security.Principal;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.smart.model.MyOrder;
import com.smart.repository.OrderRepository;
import com.smart.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class PaymentController {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	Order orderCreated=null;
	
	//create order for payment handler
    @PostMapping("/create-order")
    public String paymentGateway(@RequestBody Map<String, Object> data,Principal principal) {
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
    	System.out.println(amount);
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
			MyOrder order = new MyOrder();
			order.setOrderId(orderCreated.get("id"));
			order.setAmount(orderCreated.get("amount")+"");
			order.setReceipt(orderCreated.get("receipt"));
			order.setStatus("created");
			order.setUser(this.userRepository.getUserByUserNameUser(principal.getName()));
			order.setPaymentId(null);
			
			this.orderRepository.save(order);
			
		} catch (RazorpayException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
		}
    	
    	return orderCreated.toString();
    }
    
    @PostMapping("/update-order")
    public ResponseEntity<?> saveToDatabase(@RequestBody Map<String, Object> data) {
    	
    	System.out.println(data);
    	MyOrder myOrder = this.orderRepository.findByOrderId(data.get("order_id").toString());
    	myOrder.setStatus(data.get("status").toString());
    	myOrder.setPaymentId(data.get("payment_id").toString());
    	this.orderRepository.save(myOrder);
    	return ResponseEntity.ok(Map.of("msg","updated"));
    }
    
}
