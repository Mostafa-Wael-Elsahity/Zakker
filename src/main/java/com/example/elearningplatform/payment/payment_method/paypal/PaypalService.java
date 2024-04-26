package com.example.elearningplatform.payment.payment_method.paypal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.payment.coupon.Coupon;
import com.example.elearningplatform.payment.coupon.CouponRepository;
import com.example.elearningplatform.payment.coupon.CouponService;
import com.example.elearningplatform.payment.coupon.dto.ApplyCouponRequest;
import com.example.elearningplatform.payment.payment_method.paypal.dto.TempTransaction;
import com.example.elearningplatform.payment.payment_method.paypal.dto.TempTransactionRepository;
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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaypalService {

      private final APIContext apiContext;
      private final CouponService couponService;
      private final HttpServletRequest request;
      private final TempTransactionRepository tempTransactionRepository;
      private final CouponRepository couponRepository;
      private final TokenUtil tokenUtil;

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

            String payerId = payer.getPayerInfo().getPayerId();
            String paymentId = payment.getId();
            TempTransaction tempTransaction = new TempTransaction();
            tempTransaction.setPayerId(payerId);
            tempTransaction.setPaymentId(paymentId);
            tempTransaction.setCourseId(applyCouponRequest.getCourseId());
            tempTransaction.setUserId(tokenUtil.getUserId());
            Optional<Coupon> coupon = couponRepository.findByCodeAndCourseId(applyCouponRequest.getCouponCode(),applyCouponRequest.getCourseId());
            if (coupon.isPresent()) {
                  tempTransaction.setCouponId(coupon.get().getId());
            }
            tempTransaction.setPrice(((int) (price * 100)));
            
            tempTransactionRepository.save(tempTransaction);

            return payment.create(apiContext);
      }

      public Payment executePayment(
                  String paymentId,
                  String payerId) throws PayPalRESTException {
            Payment payment = new Payment();

            payment.setId(paymentId);

            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);

            return payment.execute(apiContext, paymentExecution);
      }
}
