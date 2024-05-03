package com.example.elearningplatform.payment.paypal;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.example.elearningplatform.payment.coupon.dto.ApplyCouponRequest;
import com.example.elearningplatform.payment.paypal.payin.PaypalService;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUser;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUserRepository;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Controller
@Data
@Slf4j
public class PaypalController {

	@Autowired
	private PaypalService paypalService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TempTransactionUserRepository tempTransactionUserRepository;
	
	private final String prefixHttp = "http://";
	@GetMapping("/paypal")
	public String home() {
		return "paypal";
	}

	@PostMapping("/payment/create")
	public RedirectView createPayment(@RequestBody ApplyCouponRequest applyCouponRequest) {
		try {
			Payment payment = paypalService.createPayment(applyCouponRequest);
			for (Links links : payment.getLinks()) {
				if (links.getRel().equals("approval_url")) {
					return new RedirectView(links.getHref());
				}
			}
		} catch (PayPalRESTException e) {
			log.error("Error occurred:: ", e);
		}
		return new RedirectView("/payment/error");
	}

	@GetMapping("/payment/success")
	public String paymentSuccess(
			@RequestParam("paymentId") String paymentId,
			@RequestParam("PayerID") String payerId) {
		TempTransactionUser tempTransactionUser = tempTransactionUserRepository
				.findByPaymentIdAndPayerId(paymentId, payerId)
				.orElseThrow(() -> new RuntimeException("Transaction not found"));
		String url = prefixHttp + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/course/retrieve-course/{id}";
		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {
				tempTransactionUser.setConfirmed(true);
				tempTransactionUser.setConfirmDate(LocalDateTime.now());
				return url;
			}
		} catch (PayPalRESTException e) {
			log.error("Error occurred:: ", e);
		}
		return url;
	}

	@GetMapping("/payment/cancel")
	public String paymentCancel() {
		return prefixHttp + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/course/retrieve-course/{id}";
	}

	@GetMapping("/payment/error")
	public String paymentError() {
		return prefixHttp + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/course/retrieve-course/{id}";
	}
}