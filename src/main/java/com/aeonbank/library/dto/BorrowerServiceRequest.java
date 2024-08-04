package com.aeonbank.library.dto;

import com.aeonbank.library.model.Borrower;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BorrowerServiceRequest extends Borrower {
}
