package com.example.elearningplatform.payment.paypal.payin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.payment.coupon.Coupon;
import com.example.elearningplatform.payment.coupon.CouponRepository;
import com.example.elearningplatform.payment.coupon.CouponService;
import com.example.elearningplatform.payment.coupon.dto.ApplyCouponRequest;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUser;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUserRepository;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Service
@Data
public class PaypalService {

      @Autowired
      private APIContext apiContext;
      @Autowired
      private CouponService couponService;
      @Autowired
      private HttpServletRequest request;
      @Autowired
      private TempTransactionUserRepository tempTransactionUserRepository;
      @Autowired
      private CouponRepository couponRepository;
      @Autowired
      private TokenUtil tokenUtil;


      /****************************************************************************************/
      public Payment createPayment(ApplyCouponRequest applyCouponRequest) throws PayPalRESTException {
         

            String successUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
                        + request.getContextPath()
                        + "/payment/success";
            String cancelUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
                        + request.getContextPath()
                        + "/payment/cancel";

            Response response = couponService.applyCoupon(applyCouponRequest);
            if (response.getStatus() != HttpStatus.OK) {
                  throw new PayPalRESTException((String) response.getData());
            }
            Double price = (Double) couponService.applyCoupon(applyCouponRequest).getData();

            Amount amount = new Amount();
            amount.setCurrency("USD");
            amount.setTotal(String.format(Locale.forLanguageTag("en_US"), "%.2f", price));

            Transaction transaction = new Transaction();
            transaction.setDescription(null);
            transaction.setAmount(amount);

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");
        

            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl(cancelUrl);
            redirectUrls.setReturnUrl(successUrl);

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);
            payment.setRedirectUrls(redirectUrls);

            Coupon coupon = couponRepository
                        .findByCodeAndCourseId(applyCouponRequest.getCouponCode(), applyCouponRequest.getCourseId())
                        .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));

            payment = payment.create(apiContext);

            TempTransactionUser tempTransactionUser = new TempTransactionUser();
            tempTransactionUser.setCourseId(applyCouponRequest.getCourseId());
            tempTransactionUser.setUserId(tokenUtil.getUserId());
            // tempTransactionUser.setUserId(tokenUtil.getUserId());
            tempTransactionUser.setCouponId(coupon.getId());
            tempTransactionUser.setPrice(((int) (price * 100)));
            tempTransactionUser.setConfirmed(false);
      
            tempTransactionUser.setPaymentId(payment.getId());
            tempTransactionUser.setCurrency("USD");
            tempTransactionUser.setPaymentMethod("paypal");
            tempTransactionUserRepository.save(tempTransactionUser);
            // System.out.println("Created Payment ID: " + payment.toString());
            return payment;
      }
/********************************************** PayPal Payment Execution ************************************************/

      public Payment executePayment(
                  String paymentId,
                  String payerId) throws PayPalRESTException {
            Payment payment = new Payment();

            payment.setId(paymentId);

            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);
            // System.out.println(payment.toString());
            // System.out.println(paymentExecution.toString());

            return payment.execute(apiContext, paymentExecution);
      }
}