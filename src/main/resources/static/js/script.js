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

















  