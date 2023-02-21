console.log("this is js file");

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
  
  
  