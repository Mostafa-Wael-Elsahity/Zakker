package com.example.elearningplatform.payment.paypal.payout;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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

import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUser;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Service
@Data
public class PayoutService {
      @Autowired
      private TempTransactionUserRepository tempTransactionUserRepository;

      @Value("${paypal.access-token-url}")
      private String paypalAccessTokenUrl;
      @Value("${paypal.client-id}")
      private String paypalClientId;
      @Value("${paypal.client-secret}")
      private String paypalClientSecret;
      @Value("${paypal.payout-url}")
      private String paypalPayoutUrl;

      @Scheduled(cron = "${schedulingProcessTempTransaction}")
      private void check() {
            List<TempTransactionUser> tempTransactionUser = tempTransactionUserRepository.findAll();
            for (TempTransactionUser temp : tempTransactionUser) {
                  if (temp.getConfirmDate().plus(Duration.ofDays(30)).isAfter(LocalDateTime.now())) {
                        // TODO: process payment transaction to instructures if refund is expired

                        tempTransactionUserRepository.delete(temp);
                  }
            }
      }

      public String createPayout(
                  String senderBatchId, String emailSubject, String emailMessage,
                  String recipientType, String value, String currency, String note,
                  String senderItemId, String receiver, String recipientWallet)
                  throws JsonProcessingException {
            RestTemplate restTemplate = new RestTemplate();

            String requestJson = String.format(
                        """
                                    { "sender_batch_header": { "sender_batch_id": "%s",
                                    "email_subject": "%s", "email_message": "%s" },
                                    "items": [ { "recipient_type": "%s", "amount": { "value": "%s", "currency": "%s" }, "note": "%s",
                                    "sender_item_id": "%s", "receiver": "%s", "recipient_wallet": "%s" } ] }
                                    """,
                        senderBatchId, emailSubject, emailMessage, recipientType, value, currency, note, senderItemId,
                        receiver, recipientWallet);

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
