package com.ensa.notificationservice.services;


import com.ensa.notificationservice.dto.NotificationRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @RabbitListener(queues = "${notification.queues.queue1}")
    public void msgReceiveMessage(NotificationRequest request) {
        // Process the notification request
        // Example: Send SMS or email
        System.out.println(request);
    }

    @RabbitListener(queues = "${notification.queues.queue2}")
    public void otpReceiveMessage(NotificationRequest request) {
        // Process the notification request
        // Example: Send SMS or email
        System.out.println(request);
    }
}
