package com.example.springdemo.batch.writer;

import com.example.springdemo.batch.model.User;
import com.example.springdemo.dao.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据写入器
 */
public class UserItemWriter implements ItemWriter<User> {

    private static final Logger log = LoggerFactory.getLogger(UserItemWriter.class);
    private final UserRepository userRepository;

    public UserItemWriter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void write(List<? extends User> items) throws Exception {
        List<com.example.springdemo.dao.entity.User> entities = new ArrayList<>();
        
        for (User item : items) {
            com.example.springdemo.dao.entity.User entity = new com.example.springdemo.dao.entity.User();
            // 复制属性
            entity.setUsername(item.getUsername());
            entity.setPassword(item.getPassword());
            entity.setEmail(item.getEmail());
            entity.setPhone(item.getPhone());
            entity.setRealName(item.getRealName());
            entity.setAge(item.getAge());
            entity.setGender(item.getGender());
            entity.setAddress(item.getAddress());
            entity.setRole(item.getRole());
            entity.setEnabled(item.getEnabled());
            
            entities.add(entity);
        }
        
        log.info("保存 {} 条用户数据", entities.size());
        userRepository.saveAll(entities);
    }

    public static UserItemWriter createWriter(UserRepository userRepository) {
        log.info("创建用户数据写入器");
        return new UserItemWriter(userRepository);
    }
} 