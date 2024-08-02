package com.aeonbank.library.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Borrower extends User {

    private String apiKeyCode;

}
