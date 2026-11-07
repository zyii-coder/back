package com.wuy.lostandfound.repository;

import com.wuy.lostandfound.entity.Item;
import com.wuy.lostandfound.entity.ItemStatus;
import com.wuy.lostandfound.entity.ItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * JpaSpecificationExecutor lets the service build dynamic WHERE clauses
 * (optional filters: type / category / status / keyword) without writing N query methods.
 */
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    Page<Item> findByType(ItemType type, Pageable pageable);

    Page<Item> findByStatus(ItemStatus status, Pageable pageable);

    Page<Item> findByPublisherId(Long publisherId, Pageable pageable);
}
