package com.XLR8.email_helper.Controller;

import com.XLR8.email_helper.Entity.Email;
import com.XLR8.email_helper.Entity.Email;  // Make sure to import Email class
import com.XLR8.email_helper.Service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/email")
@AllArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/generate")
    public Mono<ResponseEntity<String>> generateEmail(@RequestBody Email email) {
        return emailService.generateEmailReply(email)
                .map(ResponseEntity::ok);
    }
}

