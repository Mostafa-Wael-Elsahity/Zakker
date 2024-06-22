package com.example.elearningplatform.payment.paypal;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.course.CourseService;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.payment.coupon.CouponService;
import com.example.elearningplatform.payment.coupon.dto.ApplyCouponRequest;
import com.example.elearningplatform.payment.paypal.payin.PaypalService;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUser;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUserRepository;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@Data
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Transactional
public class PaypalController {

	@Autowired
	private PaypalService paypalService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TokenUtil tokenUtil;
	@Autowired
	private TempTransactionUserRepository tempTransactionUserRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private CourseService courseService;
	@Autowired
	private CouponService couponService;

	private final String prefixHttp = "http://";

	/************************************
	 * CREATE PAYMENT ****************************************
	 */

	@GetMapping("/paypal")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("paypal");
		modelAndView.addObject("applyCouponRequest", new ApplyCouponRequest());
		return modelAndView;
	}

	@PostMapping("/payment/create")
	public RedirectView createPayment(@RequestBody ApplyCouponRequest applyCouponRequest) {
		try {
			User user = userRepository.findById(tokenUtil.getUserId()).orElse(null);
			if(user==null || user.getPaypalEmail()==null ) {
				return new RedirectView("/payment/error");
			}
			// if(courseService.ckeckCourseSubscribe(applyCouponRequest.getCourseId())) {
			// return new RedirectView("/course/public/get-course/" +
			// applyCouponRequest.getCourseId());
			// }
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

	/*********************************************
	 * SUCCESS
	 * *
	 * 
	 * @throws Exception
	 *                   **********************************************************************
	 */

	@GetMapping("/payment/success")
	public void paymentSuccess(HttpServletResponse response,
			@RequestParam("paymentId") String paymentId,
			@RequestParam("PayerID") String payerId) throws Exception {
		try {

			TempTransactionUser tempTransactionUser = tempTransactionUserRepository
					.findByPaymentIdAndUserId(paymentId, tokenUtil.getUserId())
					.orElseThrow(() -> new CustomException("Transaction not found", HttpStatus.NOT_FOUND));

			Payment payment = paypalService.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {

				tempTransactionUser.setConfirmed(true);

				tempTransactionUser.setConfirmDate(LocalDateTime.now());
				if (tempTransactionUser.getCouponId() != null) {

					couponService.decrementCoupon(tempTransactionUser.getCouponId());
				}

				Course course = courseRepository.findById(tempTransactionUser.getCourseId())
						.orElseThrow(() -> new CustomException(
								"Course not found", HttpStatus.NOT_FOUND));

				courseRepository.enrollCourse(tempTransactionUser.getUserId(), tempTransactionUser.getCourseId());
				course.incrementNumberOfEnrollments();
				tempTransactionUserRepository.save(tempTransactionUser);

				response.sendRedirect("/course/public/get-course/" + tempTransactionUser.getCourseId());
				return;
			}
		} catch (CustomException e) {
			log.error("Error occurred:: ", e);
		} catch (PayPalRESTException e) {
			log.error("Error occurred:: ", e);
		}
		response.sendRedirect("/payment/error");

	}

	/******************************
	 * CANCEL PAYMENT ****************************************
	 */
	@GetMapping("/payment/cancel")
	public Response paymentCancel() throws IOException {
		return new Response(HttpStatus.BAD_REQUEST, "Payment cancelled", null);

	}

	/***************************************
	 * ERROR ***********************************************************************
	 */

	@GetMapping("/payment/error")
	public Response paymentError() {
		return new Response(HttpStatus.BAD_REQUEST, "Payment error", null);
	}
}