package com.wuy.lostandfound.dto;

import com.wuy.lostandfound.entity.Category;
import com.wuy.lostandfound.entity.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload for creating/updating an item posting.
 */
public class ItemRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 120)
    private String title;

    @Size(max = 2000)
    private String description;

    @NotNull(message = "请选择分类")
    private Category category;

    @NotNull(message = "请选择类型(失物/招领)")
    private ItemType type;

    @Size(max = 200)
    private String location;

    @Size(max = 500)
    private String imageUrl;

    @Size(max = 200)
    private String contactInfo;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public ItemType getType() { return type; }
    public void setType(ItemType type) { this.type = type; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
}
