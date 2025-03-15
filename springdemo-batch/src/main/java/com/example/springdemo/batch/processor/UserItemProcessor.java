package com.example.springdemo.batch.processor;

import com.example.springdemo.batch.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 用户数据处理器
 */
public class UserItemProcessor implements ItemProcessor<User, User> {

    private static final Logger log = LoggerFactory.getLogger(UserItemProcessor.class);
    private final PasswordEncoder passwordEncoder;

    public UserItemProcessor(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User process(User user) throws Exception {
        // 对密码进行加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        // 数据验证和转换
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            log.warn("用户名为空，跳过该记录");
            return null; // 返回null表示跳过该记录
        }
        
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.warn("邮箱格式不正确: {}", user.getEmail());
            user.setEmail(null); // 设置为null，后续可以通过其他方式处理
        }
        
        // 设置默认值
        if (user.getEnabled() == null) {
            user.setEnabled(true);
        }
        
        log.info("处理用户: {}", user.getUsername());
        return user;
    }
} 