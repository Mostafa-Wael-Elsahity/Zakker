package com.example.elearningplatform.verficationtoken;

import com.example.elearningplatform.user.User;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class VerficationTokenService {

    @Autowired private  VerficationTokenRepository verficationTokenRepository;

    /*******************************************************************************************************************/
    public String create(User user) {
        VerificationToken token = verficationTokenRepository.findByUser(user);
        if (token != null) {
            verficationTokenRepository.delete(token);
        }
        token = new VerificationToken(user);
        verficationTokenRepository.save(token);
        return token.getToken();
    }

    /*******************************************************************************************************************/

    /*******************************************************************************************************************/

    /*******************************************************************************************************************/
    public boolean checkTokenValidation(VerificationToken token) throws SQLException, IOException {

        if (LocalDateTime.now().isBefore(token.getExpiryDate().plus(20, ChronoUnit.MINUTES))) {
            return true;
        }

        return false;
    }
}
