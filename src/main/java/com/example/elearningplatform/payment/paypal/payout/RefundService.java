package com.example.elearningplatform.payment.paypal.payout;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.comment.CommentRepository;
import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.course.lesson.LessonRepository;
import com.example.elearningplatform.course.lesson.note.NoteRepository;
import com.example.elearningplatform.course.reply.Reply;
import com.example.elearningplatform.course.reply.ReplyRepository;
import com.example.elearningplatform.course.review.ReviewRepository;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.payment.paypal.payout.dto.RefundRequest;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUser;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUserRepository;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.Data;

@Service
@Data
@Transactional
public class RefundService {
      @Autowired
      private TempTransactionUserRepository tempTransactionUserRepository;
      @Autowired
      private UserRepository userRepository;
      @Autowired
      private CourseRepository courseRepository;
      @Autowired
      private TokenUtil tokenUtil;
      @Autowired
      private ReplyRepository replyRepository;
      @Autowired
      private ReviewRepository reviewRepository;
      @Autowired
      private CommentRepository commentRepository;
      @Autowired
      private LessonRepository lessonRepository;
      @Autowired
      private NoteRepository noteRepository;

      @Value("${paypal.access-token-url}")
      private String paypalAccessTokenUrl;
      @Value("${paypal.client-id}")
      private String paypalClientId;
      @Value("${paypal.client-secret}")
      private String paypalClientSecret;
      @Value("${paypal.payout-url}")
      private String paypalPayoutUrl;

      public void check(RefundRequest refundRequest) throws JsonProcessingException {
            try {
                  TempTransactionUser tempTransactionUser = tempTransactionUserRepository
                              .findByUserIdAndCourseId(tokenUtil.getUserId(), refundRequest.getCourseId())
                              .orElseThrow(() -> new CustomException("Refund ===> Rejected", HttpStatus.FORBIDDEN));
                  if (tempTransactionUser.getPrice() > 0) {
                        createPayout(tempTransactionUser);
                  }
                  Course course = courseRepository.findById(tempTransactionUser.getCourseId())
                              .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));

                  List<Lesson> lessons = lessonRepository.findLessonsByCourseId(tempTransactionUser.getCourseId());
                  lessons.forEach(
                              lesson -> {
                                    noteRepository.deleteNoteByLessonIdAndUserId(course.getId(), tempTransactionUser.getUserId());
                                    List<Comment> comments = commentRepository.findByLesson(lesson.getId());
                                    comments.forEach(
                                                comment -> {
                                                      if (comment.getUser().getId() == tempTransactionUser
                                                                  .getUserId()) {
                                                            commentRepository.delete(comment);
                                                            lesson.decrementNumberOfComments();
                                                      } else {
                                                            List<Reply> replies = replyRepository
                                                                        .findByComment(comment.getId());
                                                            replies.forEach(
                                                                        reply -> {
                                                                              if (reply.getUser()
                                                                                          .getId() == tempTransactionUser
                                                                                                      .getUserId()) {
                                                                                    replyRepository.delete(reply);
                                                                                    comment.decrementNumberOfReplies();
                                                                              } else if (replyRepository
                                                                                          .findLikedRepliesByUserId(
                                                                                                      tempTransactionUser
                                                                                                                  .getUserId(),
                                                                                                      reply.getId())
                                                                                          .isPresent()) {
                                                                                    reply.decrementNumberOfLikes();
                                                                                    replyRepository.save(reply);

                                                                              }

                                                                        });
                                                            if (commentRepository.findLikedCommentsByUserIdAndCommentId(
                                                                        tempTransactionUser.getUserId(),
                                                                        comment.getId()).isPresent()) {
                                                                  comment.decrementNumberOfLikes();

                                                            }
                                                            commentRepository.save(comment);
                                                      }

                                                });
                                    lessonRepository.save(lesson);
                              });

                  // User user = userRepository.findById(tempTransactionUser.getUserId())
                  // .orElseThrow(() -> new CustomException(
                  // "User not found", HttpStatus.NOT_FOUND));

                  // reviewRepository.deleteReviewsByCourseIdAndUserId(course.getId(),
                  // tempTransactionUser.getUserId());

                 

                  course.decrementNumberOfEnrollments();
                  courseRepository.unEnrollCourse(tempTransactionUser.getUserId(), tempTransactionUser.getCourseId());
                  courseRepository.save(course);

                  tempTransactionUserRepository.delete(tempTransactionUser);
            } catch (CustomException e) {
                  TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                  throw e;
            } catch (Exception e) {
                  TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                  throw e;
            }
      }

      public Response createRefund(RefundRequest refundRequest) throws JsonProcessingException {
            try {
                  User user = userRepository.findById(tokenUtil.getUserId())
                              .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
                              if(user.getPaypalEmail() == null) {
                                    throw new CustomException("Enter your paypal email", HttpStatus.BAD_REQUEST);
                              }
                  check(refundRequest);
                  // throw new CustomException("Refund Rejected", HttpStatus.FORBIDDEN);

                  return new Response(HttpStatus.OK, "Refund successful", null);

            } catch (CustomException e) {
                  return new Response(e.getStatus(), e.getMessage(), null);
            } catch (Exception e) {
                  return new Response(HttpStatus.BAD_REQUEST, e.getMessage(), null);
            }

      }

      public String createPayout(TempTransactionUser tempTransactionUser)
                  throws JsonProcessingException {
            try {
                  RestTemplate restTemplate = new RestTemplate();
                  String code = tempTransactionUser.getUserId() + tempTransactionUser.getPaymentId();
                  String senderBatchId = code + generate();
                  Course course = courseRepository.findById(tempTransactionUser.getCourseId()).orElse(null);
                  String emailSubject = String.format(
                              "Refund for course %s ", course.getTitle());
                  String emailMessage = String.format(
                              "The Refund have been approved for course %s and Now you can " +
                                          " receive the payment for the course." +
                                          "Please check your PayPal account.",
                              course.getTitle());
                  String price = calculatePrice(tempTransactionUser.getPrice());
                  User user = userRepository.findById(tempTransactionUser.getUserId())
                              .orElseThrow(() -> new CustomException(
                                          "User not found", HttpStatus.NOT_FOUND));
                  String receiver = user.getPaypalEmail();
                  // String receiver = "sb-iqzrw29889663@business.example.com";
                  String senderItemId = code + generate();
                  String requestJson = String.format(
                              """
                                          { "sender_batch_header": { "sender_batch_id": "%s",
                                          "email_subject": "%s", "email_message": "%s" },
                                          "items": [ { "recipient_type": "%s", "amount": { "value": "%s", "currency": "%s" }, "note": "%s",
                                          "sender_item_id": "%s", "receiver": "%s", "recipient_wallet": "%s" } ] }
                                          """,
                              senderBatchId, emailSubject, emailMessage, "EMAIL", price, "USD",
                              "NOTE", senderItemId, receiver, "PAYPAL");

                  HttpHeaders headers = new HttpHeaders();
                  String token = getAccessToken();
                  headers.setContentType(MediaType.APPLICATION_JSON);
                  headers.set("Authorization",
                              "Bearer " + token);

                  HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

                  ResponseEntity<String> response = restTemplate.postForEntity(paypalPayoutUrl, entity, String.class);
                  // System.out.println(response);
                  // System.out.println(requestJson);
                  return response.getBody();
            } catch (Exception e) {
                  throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
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
            return String.format("%.2f", price / 100.0);
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
