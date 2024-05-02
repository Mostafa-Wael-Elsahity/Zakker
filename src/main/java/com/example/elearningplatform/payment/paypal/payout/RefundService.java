package com.example.elearningplatform.payment.paypal.payout;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.payment.paypal.payout.dto.RefundRequest;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUser;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUserRepository;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RefundService {
      @Autowired
      private final TempTransactionUserRepository tempTransactionUserRepository;
      private final UserRepository userRepository;
      private final CourseRepository courseRepository;
      @Value("${paypal.access-token-url}")
      private String paypalAccessTokenUrl;
      @Value("${paypal.client-id}")
      private String paypalClientId;
      @Value("${paypal.client-secret}")
      private String paypalClientSecret;
      @Value("${paypal.payout-url}")
      private String paypalPayoutUrl;
      @Value("${paypal.email}")
      private String emailPaypal;

      public Boolean check(RefundRequest refundRequest) {
            TempTransactionUser tempTransactionUser = tempTransactionUserRepository
                        .findByUserIdAndCourseId(refundRequest.getUserId(), refundRequest.getCourseId());
            if (tempTransactionUser.getConfirmDate()
                        .plus(Duration.ofDays(30)).isAfter(LocalDateTime.now())) {
                  // save tempTrans to Transactions
                  tempTransactionUserRepository.delete(tempTransactionUser);
                  return false;
            } else {

                  return true;
            }

      }

      public String createPayout(TempTransactionUser tempTransactionUser,
                  String recipientType,
                  String senderItemId)
                  throws JsonProcessingException {
            RestTemplate restTemplate = new RestTemplate();
            Course course = courseRepository.findById(tempTransactionUser.getCourseId()).orElse(null);
            String emailSubject = String.format(
                        """
                                    Refund for course %s
                                    """, course.getTitle());
            String emailMessage = String.format(
                        """
                                    The Refund have been approved for course %s and Now you can receive the payment for the course. Please check your PayPal account.
                                    """,
                        course.getTitle());
            String senderBatchId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            Optional<User> user = userRepository.findById(tempTransactionUser.getUserId());
            String receiver = user.get().getEmail();
            String requestJson = String.format(
                        """
                                    { "sender_batch_header": { "sender_batch_id": "%s",
                                    "email_subject": "%s", "email_message": "%s" },
                                    "items": [ { "recipient_type": "%s", "amount": { "value": "%s", "currency": "%s" }, "note": "%s",
                                    "sender_item_id": "%s", "receiver": "%s", "recipient_wallet": "%s" } ] }
                                    """,
                        senderBatchId, emailSubject, emailMessage, recipientType, tempTransactionUser.getPrice()/100.0, "USD",
                        "NOTE", emailPaypal, receiver, "PAYPAL");

            HttpHeaders headers = new HttpHeaders();
            String token = getAccessToken();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization",
                        "Bearer " + token);

            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(paypalPayoutUrl, entity, String.class);

            return response.getBody();
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
