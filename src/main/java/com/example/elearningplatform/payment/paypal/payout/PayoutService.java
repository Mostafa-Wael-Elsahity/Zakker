package com.example.elearningplatform.payment.paypal.payout;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUser;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUserRepository;
import com.example.elearningplatform.payment.transaction.Transaction;
import com.example.elearningplatform.payment.transaction.TransactionRepository;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Service
@Data
public class PayoutService {
      @Autowired
      private TempTransactionUserRepository tempTransactionUserRepository;

      @Autowired
      private TransactionRepository transactionRepository;

      @Autowired
      private UserRepository userRepository;

      @Autowired
      private CourseRepository courseRepository;

      @Value("${paypal.access-token-url}")
      private String paypalAccessTokenUrl;
      @Value("${paypal.client-id}")
      private String paypalClientId;
      @Value("${paypal.client-secret}")
      private String paypalClientSecret;
      @Value("${paypal.payout-url}")
      private String paypalPayoutUrl;

      @Scheduled(cron = "${schedulingProcessTempTransaction}")
      private void check() throws JsonProcessingException {
            List<TempTransactionUser> tempTransactionUser = tempTransactionUserRepository.findAll();
            for (TempTransactionUser temp : tempTransactionUser) {
                  if (temp.getConfirmDate().plus(Duration.ofDays(30)).isAfter(LocalDateTime.now())) {
                        // TODO: process payment transaction to instructures if refund is expired
                        // transfer money to instructors
                        if (temp.getPrice() > 0) {
                              createPayout(temp);
                        }
                        // save transaction to Transactions
                        Transaction transaction = new Transaction();
                        transaction.setCourse(courseRepository.findById(temp.getCourseId()).get());
                        transaction.setUser(userRepository.findById(temp.getUserId()).get());
                        transaction.setPaymentDate(temp.getConfirmDate());
                        transaction.setPaymentMethod(temp.getPaymentMethod());
                        transaction.setPrice(temp.getPrice());
                        transactionRepository.save(transaction);
                        tempTransactionUserRepository.delete(temp);
                  }
            }
      }

      public String createPayout(TempTransactionUser tempTransactionUser)
                  throws JsonProcessingException {
            RestTemplate restTemplate = new RestTemplate();
            /*
             * String senderBatchId, String emailSubject, String emailMessage,
             * String recipientType, String value, String currency, String note,
             * String senderItemId, String receiver, String recipientWallet
             */
            String code = tempTransactionUser.getUserId() + tempTransactionUser.getPaymentId();
            String senderBatchId = code + generate();
            String emailSubject = String.format(
                        "Payment for course %s", tempTransactionUser.getCourseId());
            String emailMessage = String.format(
                        "The user %s have register for you course and now you can receive the payment for the course. Please check your PayPal account.",
                        tempTransactionUser.getUserId());
            String price = calculatePrice(tempTransactionUser.getPrice());
            String senderItemId = code + generate();
            Course course = courseRepository.findById(tempTransactionUser.getCourseId()).orElse(null);
            User ownerCourse = course.getOwner();
            String receiver = ownerCourse.getPaypalEmail();
            String requestJson = String.format(
                        """
                                    { "sender_batch_header": { "sender_batch_id": "%s",
                                    "email_subject": "%s", "email_message": "%s" },
                                    "items": [ { "recipient_type": "%s", "amount": { "value": "%s", "currency": "%s" }, "note": "%s",
                                    "sender_item_id": "%s", "receiver": "%s", "recipient_wallet": "%s" } ] }
                                    """,
                        senderBatchId, emailSubject, emailMessage, "EMAIL", price, "USD", "NOTE", senderItemId,
                        receiver, "PAYPAL");

            HttpHeaders headers = new HttpHeaders();
            String token = getAccessToken();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization",
                        "Bearer " + token);

            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(paypalPayoutUrl, entity, String.class);

            return response.getBody();
      }

      public String generate() {
            Random random = new Random();
            StringBuilder builder = new StringBuilder(10);
            for (int i = 0; i < 10; i++) {
                  int digit = random.nextInt(10); // generates a random digit from 0 to 9
                  builder.append(digit);
            }
            return builder.toString();
      }

      private String calculatePrice(Integer price) {
            Double percentage = 0.3;
            Double newPrice = price - (price * percentage);
            return String.format("%.2f", newPrice / 100.0);
      }

      public String getAccessToken() throws JsonProcessingException {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(paypalClientId, paypalClientSecret);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "client_credentials");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(paypalAccessTokenUrl, entity, String.class);

            // Parse the JSON response and return the access token
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("access_token").asText();
      }
}
