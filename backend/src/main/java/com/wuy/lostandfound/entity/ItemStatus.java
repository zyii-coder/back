package com.wuy.lostandfound.entity;

/**
 * Lifecycle of a lost/found posting.
 * OPEN    -> still unmatched
 * CLAIMED -> matched with an owner/claimer
 * CLOSED  -> resolved / taken down
 */
public enum ItemStatus {
    OPEN,
    CLAIMED,
    CLOSED
}
