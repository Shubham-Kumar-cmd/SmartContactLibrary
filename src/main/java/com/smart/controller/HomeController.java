package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.smart.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.smart.helper.Message;
import com.smart.model.User;

@Controller
public class HomeController {
    
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
    @Autowired
    private UserRepository repo;

    //save operation
    @PostMapping("/save")
    public ResponseEntity<String> saveUser(@RequestBody User user){
        Integer id=repo.save(user).getUser_id();
        System.out.println(user);
        return new ResponseEntity<String>("Employee'"+id+"'saved",HttpStatus.OK);
    }

    //home handler
    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("title", "Home: Smart Contact Library");
        return "home";
    }

    //about handler
    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("title", "About: Smart Contact Library");
        return "about";
    }

    //signup handler
    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title", "Register: Smart Contact Library");
        model.addAttribute("user", new User());
        return "signup";
    }

    //handler for registering user
    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result, @RequestParam(value="agreement", defaultValue="false") boolean agreement,Model model,HttpSession session){
        
        try {

            //client side validation
            if(!agreement){
                System.out.println("you have not agreed terms and conditions");
                throw new Exception("you have not agreed terms and conditions");
            }

            //server side validation
            if(result.hasErrors()){
                System.out.println("Errors "+result.toString());
                model.addAttribute("user", user);
                return "signup";
            }
            
            
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImage("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // User result=repo.save(user);
            repo.save(user);
            System.out.println("agreement "+agreement);
            System.out.println("User "+user);
            model.addAttribute("user", new User());
            session.setAttribute("message",new Message("Successfully Registered","alert-success"));
            return "signup";
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message",new Message("Something Went Wrong!! "+e.getMessage(),"alert-danger"));
            return "signup";
        }
    }
    
    //handler for custom login
    @GetMapping("/signin")
    public String login(Model model){
        model.addAttribute("title", "Login: Smart Contact Library");
        return "login";
    }
    
    //handler for error page
    @GetMapping("/error")
    public String error(Model model){
        model.addAttribute("title", "Error: Smart Contact Library");
        return "error";
    }
}
