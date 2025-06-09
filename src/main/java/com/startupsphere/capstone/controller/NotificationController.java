package com.startupsphere.capstone.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.startupsphere.capstone.dtos.NotificationRequest;
import com.startupsphere.capstone.entity.Notifications;
import com.startupsphere.capstone.responses.ApiResponse;
import com.startupsphere.capstone.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse> createNotification(@RequestBody NotificationRequest request) {
        try {
            logger.info("Creating new notification");
            Notifications created = notificationService.createNotifications(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Notification created successfully", created));
        } catch (Exception e) {
            logger.error("Error creating notification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to create notification: " + e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllNotifications() {
        try {
            logger.info("Fetching all notifications");
            List<Notifications> notifications = notificationService.getAllNotifications();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Notifications retrieved successfully", notifications));
        } catch (Exception e) {
            logger.error("Error retrieving notifications: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to fetch all notifications", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateNotifications(@RequestBody Notifications notifications,
            @PathVariable int id) {
        try {
            logger.info("Updating notification with id: {}", id);
            Notifications updated = notificationService.updateNotifications(notifications, id);
            return ResponseEntity.ok(new ApiResponse(true, "Notification updated successfully", updated));
        } catch (IllegalArgumentException e) {
            logger.warn("Notification not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error updating notification with id: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to update notification", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteNotification(@PathVariable int id){
        try {
            logger.info("Deleting notification with id: {}", id);
            boolean deleted = notificationService.deleteNotification(id);
            return ResponseEntity.ok(new ApiResponse(deleted, "Notification deleted successfully", null));
        } catch (IllegalArgumentException e) {
            logger.warn("Notification not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse(false, "Notification not found with id: {}", id));
        } catch (Exception e) {
            logger.error("Error deleting notifcation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse(false, "Error deleting notification", null));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse> getNotificationsCount(){
        try {
            logger.info("Fetching notifications count");
            long count = notificationService.getNotificationsCount();
            return ResponseEntity.ok()
            .body(new ApiResponse(true, "Notifications count fetched successfully", count));
        } catch (Exception e) {
            logger.error("Error retrieving notifications count: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse(false, "Failed to get notifications count", null));
        }
    }

    @GetMapping("/unviewed/count")
    public ResponseEntity<ApiResponse> getUnviewedCount(){
        try {
            long count = notificationService.getUnviewedCount();
            return ResponseEntity.ok()
            .body(new ApiResponse(true, "Unviewed count retrieved", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse(false, "Error getting unviewed count", null));
        }
    }

    @GetMapping("/new")
    public ResponseEntity<ApiResponse> getUnviewedNotifications(){
        try {
            List<Notifications> notifications = notificationService.getUnviewedNotifications();
            return ResponseEntity.ok()
            .body(new ApiResponse(true, "Unviewed Notifications retrieved successfully", notifications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse(false, "Error retrieving unviewed notifications", null));
        }
    }

    @PutMapping("/{id}/view")
    public ResponseEntity<ApiResponse> markAsViewed(@PathVariable int id){
        try {
            Notifications viewed = notificationService.markAsViewed(id);
            return ResponseEntity.ok()
            .body(new ApiResponse(true,"Marked as viewed successfully", viewed));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse(false,"Error marking as viewed", null));
        }
    }

}
