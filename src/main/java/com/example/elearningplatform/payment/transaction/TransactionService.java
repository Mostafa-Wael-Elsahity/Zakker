package com.example.elearningplatform.payment.transaction;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.payment.transaction.dto.CreateTransactionRequest;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;

import lombok.Data;

@Service
@Data
public class TransactionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private TransactionRepository transactionRepository;

  

    /*********************************************************************** */

    public Response saveTransaction(CreateTransactionRequest request) {
        try {
            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new Exception("User not found"));
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new Exception("Course not found"));
            
            Transaction transaction = new Transaction();
            transaction.setCourse(course);
            transaction.setUser(user);

            transaction.setPaymentMethod(request.getPaymentMethod());
            transaction.setPrice(request.getPrice());
            transaction.setPaymentDate(LocalDateTime.now());
            transactionRepository.save(transaction);
            return new Response(HttpStatus.OK, "Transaction created successfully", transaction);

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());

        }

    }

    /*********************************************************************** */
    public Response getTransactions() {
        try {
            return new Response(HttpStatus.OK, "Transactions retrieved successfully",
                    transactionRepository.findByUserId(tokenUtil.getUserId()));
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }
    /*********************************************************************** */

}
