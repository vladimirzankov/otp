package com.secureauth.verification.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender mailSender;
    private final TelegramBotService telegramBotService;
    private final Dotenv dotenv = Dotenv.load();

    private final String twilioAccountSid = dotenv.get("TWILIO_ACCOUNT_SID");
    private final String twilioAuthToken = dotenv.get("TWILIO_AUTH_TOKEN");
    private final String twilioPhoneNumber = dotenv.get("TWILIO_PHONE_NUMBER");

    @PostConstruct
    public void init() {
        Twilio.init(twilioAccountSid, twilioAuthToken);
    }

    public void sendEmailNotification(String to, String tokenValue, String sessionId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Verification Token");
        message.setText("Verification token for session " + sessionId + ": " + tokenValue);
        mailSender.send(message);
    }

    public void sendSmsNotification(String toPhoneNumber, String tokenValue, String sessionId) {
        String messageText = "Verification token for session " + sessionId + ": " + tokenValue;

        Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                messageText
        ).create();
    }

    public void sendTelegramNotification(String chatId, String tokenValue, String sessionId) {
        String message = "Verification token for session " + sessionId + ": " + tokenValue;
        telegramBotService.sendMessage(chatId, message);
    }
} 