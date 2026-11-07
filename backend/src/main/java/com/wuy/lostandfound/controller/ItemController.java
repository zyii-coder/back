package com.wuy.lostandfound.controller;

import com.wuy.lostandfound.dto.ItemRequest;
import com.wuy.lostandfound.dto.ItemResponse;
import com.wuy.lostandfound.entity.Category;
import com.wuy.lostandfound.entity.ItemStatus;
import com.wuy.lostandfound.entity.ItemType;
import com.wuy.lostandfound.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<Page<ItemResponse>> search(
            @RequestParam(required = false) ItemType type,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) ItemStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        PageRequest pageable = PageRequest.of(page, size, parseSort(sort));
        return ResponseEntity.ok(itemService.search(type, category, status, keyword, pageable));
    }

    @GetMapping("/mine")
    public ResponseEntity<Page<ItemResponse>> mine(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(itemService.getMine(principal.getUsername(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ItemResponse> create(
            @Valid @RequestBody ItemRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(itemService.create(request, principal.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ItemRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(itemService.update(id, request, principal.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        itemService.delete(id, principal.getUsername());
        return ResponseEntity.noContent().build();
    }

    private Sort parseSort(String sort) {
        try {
            String[] parts = sort.split(",");
            String property = parts[0];
            Sort.Direction direction = (parts.length > 1 && parts[1].equalsIgnoreCase("asc"))
                    ? Sort.Direction.ASC : Sort.Direction.DESC;
            return Sort.by(direction, property);
        } catch (Exception e) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
    }
}
