package com.example.DigitalCard.controller;


import com.example.DigitalCard.dto.PasswordResetDto;
import com.example.DigitalCard.dto.UserDto;
import com.example.DigitalCard.entity.User;
import com.example.DigitalCard.repository.FormRepository;
import com.example.DigitalCard.repository.UserRepository;
import com.example.DigitalCard.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Controller
public class AuthController {

    private UserService userService;
    private FormRepository formRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, FormRepository formRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.formRepository = formRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home() {

        return "LandingPage";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        model.addAttribute("passwordResetUrl", "/password-reset");
        return "log";
    }



    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "reg";
    }



    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model) {
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "reg";
        }
        userService.saveUser(user);
        return "log";
    }

    @GetMapping("/password-reset")
    public String showPasswordResetForm(Model model) {
        PasswordResetDto passwordResetDto = new PasswordResetDto();
        model.addAttribute("passwordResetDto", passwordResetDto);
        return "password-reset";
    }

    @PostMapping("/password-reset")
    public String resetPassword(@Valid @ModelAttribute("passwordResetDto") PasswordResetDto passwordResetDto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "password-reset";
        }
        User user = userService.findByEmail(passwordResetDto.getEmail());
        if (user == null) {
            result.rejectValue("email", null, "No user found with that email");
            return "password-reset";
        }
        if (!passwordResetDto.getNewPassword().equals(passwordResetDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Passwords do not match");
            return "password-reset";
        }
        resetPassword(user, passwordResetDto.getNewPassword());
        return "redirect:/login";
    }

    public void resetPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

  /*
    @PostMapping("/login")
    public String login(@ModelAttribute("user") UserDto user, BindingResult result, Model model) {
        String email = user.getEmail();
        String password = user.getPassword();
        if (result.hasErrors()) {
            return "login";
        }
        User loggedInUser = userService.findByEmailAndPassword(email, password);
        if (loggedInUser == null) {
            result.rejectValue("email", null, "Invalid email or password");
            return "login";
        }
        userService.loginUser(user);
        return "users";
    }*/
}
