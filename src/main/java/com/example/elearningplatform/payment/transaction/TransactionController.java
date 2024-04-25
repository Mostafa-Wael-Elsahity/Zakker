package com.example.elearningplatform.payment.transaction;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.payment.transaction.Dto.CreateTransactionRequest;
import com.example.elearningplatform.response.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    /************************************************************************************************* */
    @PostMapping("/create-transaction")
    public Response createTransaction(@RequestBody CreateTransactionRequest request) {

        return transactionService.saveTransaction(request);
    }

    /************************************************************************************************* */
    @GetMapping("/get-transactions")
    public Response getTransactions() {

        return transactionService.getTransactions();
    }
}
