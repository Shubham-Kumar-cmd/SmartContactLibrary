package com.smart.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.helper.Message;
import com.smart.model.Contact;
import com.smart.model.User;
import com.smart.repository.ContactRepository;
import com.smart.repository.UserRepository;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;

	//method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model,Principal principal) {
		String userName = principal.getName();
    	System.out.println("username "+userName);//Principal is used to get the unique id(here In this case, it is email)
    	
    	User user = userRepository.getUserByUserNameUser(userName);
    	model.addAttribute("user", user);
    	
    	System.out.println("User "+user);
	}
	
	//dashboard home handler
    @RequestMapping(value="/index", method = RequestMethod.GET)
    public String dashboard(Model model,Principal principal){
    	model.addAttribute("title", "User Dashboard");
        return "normal/user_dashboard";
    }
    
    //open add contact form handler 
    @GetMapping("/addcontact")
    public String openAddContactForm(Model model) {
    	model.addAttribute("title", "Add Contact");
    	model.addAttribute("contact", new Contact());
    	return "normal/add_contact";
    }
    
    //processing add contact form handler
    @PostMapping("/processcontact")
    public String processContact(
    		@ModelAttribute Contact contact,
    		//@RequestParam("image") MultipartFile file,
    		Principal principal,
    		HttpSession session
    		) {
    	try {
    		String name = principal.getName();
        	User user = userRepository.getUserByUserNameUser(name);
        	
        	
        	//processing and file uploading
        	//not working
        	/*if(file.isEmpty()) {
        		//file is empty show some message to the user
        		System.out.println("file is empty");
        		
        		contact.setImage("contact.png");//we have to set manually if user not add the image
        		
        	}else {
        		//upload the file to folder and update the contact with file url in database
        		contact.setImage(file.getOriginalFilename());
        		File saveFile = new ClassPathResource("static/img").getFile();
        		Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
        		long copy = Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        		System.out.println("file upload "+copy);
        	}*/
        	
        	contact.setUser(user);
        	user.getContacts().add(contact);
        	userRepository.save(user);
        	System.out.println("contact data added to database");
        	System.out.println("data "+contact);
        	session.setAttribute("msg", new Message("Contact added successfully","alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new Message("Something went wrong!! please try again","alert-danger"));
		}
    	return "normal/add_contact";
    }
    
    //show all contacts handler
    //per page =5[n]
    //current page =0[page]
    @GetMapping("/showcontacts/{page}")
    public String showContact(@PathVariable("page") Integer page,Model model,Principal principal) {
    	
    	
    	String name = principal.getName();
    	User nameUser = userRepository.getUserByUserNameUser(name);
    	int user_id = nameUser.getUser_id();
  
    	//Pageable contain two information 
    	//1)currentpage page
    	//2)contact per page
    	Pageable pageable = PageRequest.of(page, 3);
    	Page<Contact> findContactsByUserId = this.contactRepository.findContactByUserEmail(user_id, pageable);
    	model.addAttribute("contacts", findContactsByUserId);
    	model.addAttribute("currentPage", page);
    	model.addAttribute("totalPages", findContactsByUserId.getTotalPages());
    	
    	model.addAttribute("title", "Show all Contacts");
    	return "normal/all_contacts";
    }
    
    
    //show particular contact detail handler
    @GetMapping("/{contact_id}/contact")
    public String showDetails(@PathVariable("contact_id") Integer cId,Model model,Principal principal) {
    	model.addAttribute("title", "Show Detail");
    	Optional<Contact> contactId = this.contactRepository.findById(cId);
    	Contact contact = contactId.get();
    	
    	String name = principal.getName();
    	User user = this.userRepository.getUserByUserNameUser(name);
    	if(user.getUser_id()==contact.getUser().getUser_id()) {
    		model.addAttribute("contact", contact);
    	}
    	return "normal/show_detail";
    }
    
    //delete particular contact detail handler
    @GetMapping("/delete/{contact_id}")
    public String deleteContact(@PathVariable("contact_id") Integer cid,Model model,Principal principal,HttpSession session) {
    		Optional<Contact> contactOptional = this.contactRepository.findById(cid);
    		Contact contact = contactOptional.get();
    		System.out.println("contact "+contact);
    		
    		String name = principal.getName();
    		User user = this.userRepository.getUserByUserNameUser(name);
    		
    		if(user.getUser_id()==contact.getUser().getUser_id()) {
    			boolean deleted = user.getContacts().remove(contact);
    			this.userRepository.save(user);
    			System.out.println("DELETED "+deleted);
    			session.setAttribute("message", new Message("contact deleted successfully", "alert-success"));
    		}
    	return "redirect:/user/showcontacts/0";
    }
    
    //update particular contact detail handler
    @PostMapping("/updatecontact/{contact_id}")
    public String updateContact(@PathVariable("contact_id") Integer cid, Model model,HttpSession session) {
    	
    	Optional<Contact> findById = this.contactRepository.findById(cid);
    	Contact contact = findById.get();
    	model.addAttribute("title", "Update Contact Details");
    	model.addAttribute("contact", contact);
    	session.setAttribute("message", new Message("Updated Successfully", "alert-success"));
    	return "normal/update_contact";
    }
    
    //update contact details handler
    @PostMapping("/processupdatecontact")
    public String processContactUpdate(@ModelAttribute Contact contact,
    		//@RequestParam("image") MultipartFile file,
    		Principal principal,
    		HttpSession session) {
    	try {
			System.out.println(contact.getEmail());
			System.out.println(contact.getContact_id());
			System.out.println(contact.getName());
			
			String name = principal.getName();
        	User user = userRepository.getUserByUserNameUser(name);
        	
        	//processing and file uploading
        	//not working
        	/*if(file.isEmpty()) {
        		//file is empty show some message to the user
        		System.out.println("file is empty");
        		
        		contact.setImage("contact.png");//we have to set manually if user not add the image
        		
        	}else {
        		//upload the file to folder and update the contact with file url in database
        		contact.setImage(file.getOriginalFilename());
        		File saveFile = new ClassPathResource("static/img").getFile();
        		Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
        		long copy = Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        		System.out.println("file upload "+copy);
        	}*/
        	
        	contact.setUser(user);
        	contactRepository.save(contact);
        	System.out.println("contact data updated to database");
        	System.out.println("data "+contact);
        	session.setAttribute("message", new Message("Contact Updated successfully","alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong!! please try again","alert-danger"));
		}
    	return "redirect:/user/"+contact.getContact_id()+"/contact";
    }
    
    //user profile handler
    @GetMapping("/profile")
    public String userProfile(Model model,Principal principal) {
    	String name = principal.getName();
    	User user = userRepository.getUserByUserNameUser(name);
    	model.addAttribute("title", "User Profile");
    	model.addAttribute("profile", user);
    	return "normal/user_profile";
    }
    
    //user setting handler
    @GetMapping("/settings")
    public String openSetting(Model model) {
    	model.addAttribute("title", "Change your profile");
    	return "normal/settings";
    }
    
    //processing changepassword handler
    @PostMapping("/changepassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,Principal principal,HttpSession session) {
    	System.out.println("oldPwd "+oldPassword);
    	System.out.println("newPwd "+newPassword);
    	
    	String name = principal.getName();
    	User user = this.userRepository.getUserByUserNameUser(name);
    	
    	String password = user.getPassword();
    	
    	if(this.bCryptPasswordEncoder.matches(oldPassword, password)) {
    		//change the password
    		user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
    		this.userRepository.save(user);
    		session.setAttribute("message", new Message("Password updated successfully", "alert-success"));
    	}
    	else {
    		//throw error
    		session.setAttribute("message", new Message("Invalid old password", "alert-danger"));
    		return "redirect:/user/settings";
    	}
    	
    	return "redirect:/user/index";
    }
    
    
    
}
