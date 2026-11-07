package com.wuy.lostandfound.config;

import com.wuy.lostandfound.entity.Category;
import com.wuy.lostandfound.entity.Item;
import com.wuy.lostandfound.entity.ItemStatus;
import com.wuy.lostandfound.entity.ItemType;
import com.wuy.lostandfound.entity.Role;
import com.wuy.lostandfound.entity.User;
import com.wuy.lostandfound.repository.ItemRepository;
import com.wuy.lostandfound.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Dev-only convenience: on a fresh (empty) database it creates a demo user, an admin,
 * and a few sample postings so the UI isn't empty on first run.
 * It is a no-op once data already exists.
 *
 * Demo accounts:  demo / demo123   (USER)
 *                admin / admin123 (ADMIN)
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           ItemRepository itemRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // already seeded
        }

        User demo = new User();
        demo.setUsername("demo");
        demo.setEmail("demo@wuy.edu");
        demo.setPassword(passwordEncoder.encode("demo123"));
        demo.setRole(Role.USER);
        demo = userRepository.save(demo);

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@wuy.edu");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        seedItem(demo.getId(), "图书馆丢失黑色雨伞", "在图书馆三楼自习区遗失，黑色长柄雨伞。",
                Category.OTHERS, ItemType.LOST, "图书馆三楼");
        seedItem(demo.getId(), "食堂捡到学生证", "中心食堂二楼捡到一张学生证，请失主联系。",
                Category.DOCUMENTS, ItemType.FOUND, "中心食堂");
        seedItem(demo.getId(), "操场捡到蓝牙耳机", "操场边捡到一只白色蓝牙耳机，已暂存失物中心。",
                Category.ELECTRONICS, ItemType.FOUND, "操场");
    }

    private void seedItem(Long publisherId, String title, String desc,
                          Category category, ItemType type, String location) {
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(desc);
        item.setCategory(category);
        item.setType(type);
        item.setLocation(location);
        item.setContactInfo("请联系 demo 用户");
        item.setPublisherId(publisherId);
        item.setStatus(ItemStatus.OPEN);
        itemRepository.save(item);
    }
}
