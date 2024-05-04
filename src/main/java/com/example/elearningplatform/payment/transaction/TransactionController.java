// package com.example.elearningplatform.payment.transaction;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;

// import com.example.elearningplatform.payment.transaction.dto.CreateTransactionRequest;
// import com.example.elearningplatform.response.Response;

// import io.swagger.v3.oas.annotations.security.SecurityRequirement;
// import lombok.Data;
// import lombok.RequiredArgsConstructor;

// @RestController
// @Data
// @SecurityRequirement(name = "bearerAuth")
// public class TransactionController {
//     @Autowired private  TransactionService transactionService;

//     /************************************************************************************************* */
//     // @PostMapping("/create-transaction")
//     // public Response createTransaction(@RequestBody CreateTransactionRequest request) {

//     //     return transactionService.saveTransaction(request);
//     // }

//     // /************************************************************************************************* */
//     // @GetMapping("/get-transactions")
//     // public Response getTransactions() {

//     //     return transactionService.getTransactions();
//     // }
// }