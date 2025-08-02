package com.XLR8.email_helper.Controller;

import com.XLR8.email_helper.Entity.Email;
import com.XLR8.email_helper.Service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
public class EmailController {
    private final EmailService emailService;
    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody Email email){
        String response=emailService.generateEmailReply(email);
        return ResponseEntity.ok(response);
    }
}
