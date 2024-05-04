package com.example.elearningplatform.payment.paypal.payout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.payment.paypal.payout.dto.RefundRequest;
import com.example.elearningplatform.response.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Data;

@RestController
@Data
@SecurityRequirement(name = "bearerAuth")
public class RefundController {

      @Autowired
      private RefundService refundService;
      
      /*******
       * 
       * @param refundRequest
       * @return
       * @throws JsonMappingException
       * @throws JsonProcessingException
       */

      @PostMapping("/refund")
      public Response createRefund(@RequestBody RefundRequest refundRequest)
                  throws JsonMappingException, JsonProcessingException {
            // String response = payoutService.createPayout(
            //             "123457789010002010", "Email subject",
            //             "Email message", "EMAIL", "100.00",
            //             "USD", "Note", "item_mohamedredazs00olimaahw",
            //             "sb-c1zgn29967664@business.example.com", "PAYPAL");
            return refundService.createRefund(refundRequest);
      }
}