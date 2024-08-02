package com.aeonbank.library.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends Audit {

    private String name;
    private String email;
    private String password;

}
