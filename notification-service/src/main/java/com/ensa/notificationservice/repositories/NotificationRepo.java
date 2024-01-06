package com.ensa.notificationservice.repositories;

import com.ensa.notificationservice.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepo extends JpaRepository<Notification, UUID> {
}
