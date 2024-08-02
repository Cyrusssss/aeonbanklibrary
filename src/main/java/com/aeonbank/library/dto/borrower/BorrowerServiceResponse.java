package com.aeonbank.library.dto.borrower;

import com.aeonbank.library.model.Borrower;
import lombok.Data;

import java.util.List;

@Data
public class BorrowerServiceResponse {

    private Borrower borrower;
    private List<Borrower> borrowerList;

}
