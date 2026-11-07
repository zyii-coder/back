package com.wuy.lostandfound.dto;

import com.wuy.lostandfound.entity.Category;
import com.wuy.lostandfound.entity.ItemStatus;
import com.wuy.lostandfound.entity.ItemType;

import java.time.LocalDateTime;

/**
 * Read model returned to clients. Never exposes the publisher's id/password,
 * only the display username.
 */
public class ItemResponse {

    private Long id;
    private String title;
    private String description;
    private Category category;
    private ItemType type;
    private ItemStatus status;
    private String location;
    private String imageUrl;
    private String contactInfo;
    private String publisherUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ItemResponse() { }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public ItemType getType() { return type; }
    public void setType(ItemType type) { this.type = type; }

    public ItemStatus getStatus() { return status; }
    public void setStatus(ItemStatus status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getPublisherUsername() { return publisherUsername; }
    public void setPublisherUsername(String publisherUsername) { this.publisherUsername = publisherUsername; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
