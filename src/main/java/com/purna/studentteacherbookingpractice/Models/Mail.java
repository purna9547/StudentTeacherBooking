package com.purna.studentteacherbookingpractice.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Mail {
    private String recipient;
    private String subject;
    private String msgBody;
}
