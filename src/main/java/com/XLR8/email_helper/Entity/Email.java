package com.XLR8.email_helper.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    @JsonProperty("emailContent")
    private String emailContent;

    @JsonProperty("tone")
    private String tone;
}
