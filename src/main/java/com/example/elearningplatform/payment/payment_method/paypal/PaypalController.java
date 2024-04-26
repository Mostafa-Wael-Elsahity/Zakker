package com.example.elearningplatform.payment.payment_method.paypal;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.course.CourseRepository;
import com.example.elearningplatform.payment.coupon.dto.ApplyCouponRequest;
import com.example.elearningplatform.payment.payment_method.paypal.dto.TempTransaction;
import com.example.elearningplatform.payment.payment_method.paypal.dto.TempTransactionRepository;
import com.example.elearningplatform.payment.transaction.Transaction;
import com.example.elearningplatform.payment.transaction.TransactionRepository;
import com.example.elearningplatform.user.User;
import com.example.elearningplatform.user.UserRepository;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaypalController {

	private final PaypalService paypalService;
	private final HttpServletRequest request;
	private final String prefixHttp = "http://";
	private final TempTransactionRepository tempTransactionRepository;
	private final TransactionRepository transactionRepository;
	private final UserRepository userRepository;
	private final CourseRepository courseRepository;

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
		TempTransaction tempTransaction = tempTransactionRepository.findByPaymentIdAndPayerId(paymentId, payerId);
		String url = prefixHttp + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/course/retrieve-course/{id}";
		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {
				Transaction transaction = new Transaction();
				// how to set course id and user id to transaction
				User user = userRepository.findById(tempTransaction.getUserId()).orElse(null);
				Course course = courseRepository.findById(tempTransaction.getCourseId()).orElse(null);
				transaction.setCourse(course);
				transaction.setUser(user);
				transaction.setPaymentDate(LocalDateTime.now());
				transaction.setPaymentMethod("Paypal");
				transactionRepository.save(transaction);
				tempTransactionRepository.deleteByPaymentIdAndPayerId(paymentId, payerId);
				return url;
			}
		} catch (PayPalRESTException e) {
			log.error("Error occurred:: ", e);
		}
		tempTransactionRepository.deleteByPaymentIdAndPayerId(paymentId, payerId);
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