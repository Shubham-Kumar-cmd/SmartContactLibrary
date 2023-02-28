console.log("this is js file");

//used for mobile screen size
const toggleSidebar=()=>{

    if($(".sidebar").is(":visible")){
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","0%");
    }
    else{
        $(".sidebar").css("display","block");
        $(".content").css("margin-left","20%");
    }

};

//used for tiny text editor
tinymce.init({
        selector: '#mytextarea'
      });
      
//used for delete contact      
function deleteContact(contact_id){
		  swal({
	  title: "Are you sure?",
	  text: "You want to delete this contact!",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	  timer: 10000,
	})
	.then((willDelete) => {
	  if (willDelete) {
	    window.location="/user/delete/"+contact_id;	
	  } else {
	    swal("Your contact is safe!");
	  }
	});
  }
  
//used for search contact  
const search=()=>{
	let query=$("#search-input").val();
	if(query==''){
		$(".search-result").hide();
	}
	else{
		//searching
		//console.log(query);
		
		//sending request to server
		let url=`http://localhost:8083/search/${query}`;
		fetch(url)
		  .then((response) => response.json())
		  .then((data) => {console.log(data)
		  
		  let text=`<div class='list-group'>`;
		  
		  data.forEach((contact)=>{
			  text+=`<a href='/user/${contact.contact_id}/contact' class='list-group-item list-group-item-action'>${contact.name}</a>`
		  });
		  
		  text+=`</div>`;
		  
		  $(".search-result").html(text);
		  $(".search-result").show();
		  });
				
		
	}
}; 


//integrating payment method
//first request to server to create order
const paymentStart=()=>{
	console.log("payment started..");
	let amount=$("#payment_field").val();
	console.log(amount);
	if(amount=='' || amount ==null){
		//alert("amount is required!!");
		swal("Failed!", "amount is required!!", "error");
		return;
	}
	
	//1) using ajax to send request to server to create order -jquery
	$.ajax({
		url:'/user/create-order',
		data:JSON.stringify({amount:amount,info:'order_request'}),
		contentType:'application/json',
		type:'POST',
		dataType:'json',
		success:function(response){
			//invoked when success
			console.log(response);
			if(response.status=='created'){


				//2) open payment form
				var options = {
				    key: "rzp_test_WARgFgiz3icdKP", // Enter the Key ID generated from the Dashboard
				    amount: response.amount, // Amount is in currency subunits. Default currency is INR. Hence, 50000 refers to 50000 paise
				    currency: "INR",
				    name: "Smart Contact Library",
				    description: "Donation",
				    image: "https://www.google.com/imgres?imgurl=https%3A%2F%2Fdcassetcdn.com%2Fdesign_img%2F913364%2F202687%2F202687_5387656_913364_image.jpg&imgrefurl=https%3A%2F%2Fwww.designcrowd.com%2Fdesign%2F5387656&tbnid=h5I6cf4geFg6mM&vet=12ahUKEwjQ04q6kLX9AhXU_zgGHTXADmMQMygBegUIARCwAQ..i&docid=84fd_pWdHp-keM&w=1200&h=840&q=shubham%20hardware%20logo&ved=2ahUKEwjQ04q6kLX9AhXU_zgGHTXADmMQMygBegUIARCwAQ",
				    order_id: response.id, //This is a sample Order ID. Pass the `id` obtained in the response of Step 1
				    handler: function (response){
				        console.log(response.razorpay_payment_id);
				        console.log(response.razorpay_order_id);
				        console.log(response.razorpay_signature);
				        console.log('payment successful');
				        //alert('payment successful');
				        
				        updatePaymentOnSuccess(
							response.razorpay_payment_id,
							response.razorpay_order_id,
							'paid'
						);	
				        //swal("Good job!", "congrats !! payment successful!", "success");
				    },
				    prefill: {
				        name: "Gaurav Kumar",
				        email: "gaurav.kumar@example.com",
				        contact: "9000090000"
				    },
				    notes: {
				        address: "Shubham Hardware"
				    },
				    theme: {
				        color: "#3399cc"
				    }
				};
				
				var rzp = new Razorpay(options);
				rzp.on('payment.failed', function (response){
				        console.log(response.error.code);
				        console.log(response.error.description);
				        console.log(response.error.source);
				        console.log(response.error.step);
				        console.log(response.error.reason);
				        console.log(response.error.metadata.order_id);
				        console.log(response.error.metadata.payment_id);
						//alert('Oops payment failed');
						swal("Failed!", "Oops payment failed!", "error");
				});
				//document.getElementById('rzp-button1').onclick = function(e){
				    rzp.open();
				    //e.preventDefault();
				//}
								
								
			}
		},
		error:function(error){
			//invoked when error
			console.log(error);
			alert("something went wrong!!");
		}
	}) 
};


function updatePaymentOnSuccess(payment_id,order_id,status){
	$.ajax({
		url:'/user/update-order',
		data:JSON.stringify({payment_id:payment_id,order_id:order_id,status:status}),
		contentType:'application/json',
		type:'POST',
		dataType:'json',
		success:function(response){
			swal("Good job!", "congrats !! payment successful!", "success");
		},
		error:function(error){
			swal("Failed!", "Your payment is successful, but there is problem with server", "error");
		}
	});
}











  