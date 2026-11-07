package com.wuy.lostandfound.service;

import com.wuy.lostandfound.dto.ItemRequest;
import com.wuy.lostandfound.dto.ItemResponse;
import com.wuy.lostandfound.entity.Category;
import com.wuy.lostandfound.entity.Item;
import com.wuy.lostandfound.entity.ItemStatus;
import com.wuy.lostandfound.entity.ItemType;
import com.wuy.lostandfound.entity.User;
import com.wuy.lostandfound.exception.BadRequestException;
import com.wuy.lostandfound.exception.ResourceNotFoundException;
import com.wuy.lostandfound.repository.ItemRepository;
import com.wuy.lostandfound.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Core domain service for lost/found postings: search with optional filters,
 * CRUD restricted to the owner, and entity<->DTO mapping.
 */
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public Page<ItemResponse> search(ItemType type, Category category, ItemStatus status,
                                     String keyword, Pageable pageable) {
        Specification<Item> spec = buildSpec(type, category, status, keyword);
        return itemRepository.findAll(spec, pageable).map(this::toResponse);
    }

    public Page<ItemResponse> getMine(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        return itemRepository.findByPublisherId(user.getId(), pageable).map(this::toResponse);
    }

    public ItemResponse getById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("物品记录不存在: " + id));
        return toResponse(item);
    }

    @Transactional
    public ItemResponse create(ItemRequest req, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        Item item = new Item();
        apply(req, item);
        item.setPublisherId(user.getId());
        item.setStatus(ItemStatus.OPEN);
        item = itemRepository.save(item);
        return toResponse(item);
    }

    @Transactional
    public ItemResponse update(Long id, ItemRequest req, String username) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("物品记录不存在: " + id));
        if (!item.getPublisherId().equals(currentUserId(username))) {
            throw new BadRequestException("只能修改自己发布的记录");
        }
        apply(req, item);
        item = itemRepository.save(item);
        return toResponse(item);
    }

    @Transactional
    public void delete(Long id, String username) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("物品记录不存在: " + id));
        if (!item.getPublisherId().equals(currentUserId(username))) {
            throw new BadRequestException("只能删除自己发布的记录");
        }
        itemRepository.delete(item);
    }

    private Long currentUserId(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在")).getId();
    }

    private void apply(ItemRequest req, Item item) {
        item.setTitle(req.getTitle());
        item.setDescription(req.getDescription());
        item.setCategory(req.getCategory());
        item.setType(req.getType());
        item.setLocation(req.getLocation());
        item.setImageUrl(req.getImageUrl());
        item.setContactInfo(req.getContactInfo());
    }

    private ItemResponse toResponse(Item item) {
        ItemResponse r = new ItemResponse();
        r.setId(item.getId());
        r.setTitle(item.getTitle());
        r.setDescription(item.getDescription());
        r.setCategory(item.getCategory());
        r.setType(item.getType());
        r.setStatus(item.getStatus());
        r.setLocation(item.getLocation());
        r.setImageUrl(item.getImageUrl());
        r.setContactInfo(item.getContactInfo());
        r.setCreatedAt(item.getCreatedAt());
        r.setUpdatedAt(item.getUpdatedAt());
        if (item.getPublisherId() != null) {
            userRepository.findById(item.getPublisherId())
                    .ifPresent(u -> r.setPublisherUsername(u.getUsername()));
        }
        return r;
    }

    private Specification<Item> buildSpec(ItemType type, Category category, ItemStatus status, String keyword) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (type != null) predicates.add(cb.equal(root.get("type"), type));
            if (category != null) predicates.add(cb.equal(root.get("category"), category));
            if (status != null) predicates.add(cb.equal(root.get("status"), status));
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("description")), like),
                        cb.like(cb.lower(root.get("location")), like)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
