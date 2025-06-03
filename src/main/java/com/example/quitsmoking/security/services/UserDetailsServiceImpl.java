// src/main/java/com/example/quitsmoking/security/services/UserDetailsServiceImpl.java
package com.example.quitsmoking.security.services;

import com.example.quitsmoking.model.User;
import com.example.quitsmoking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Sử dụng .orElseThrow() để lấy User từ Optional và ném ngoại lệ nếu không tìm thấy
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        // Đảm bảo UserDetailsImpl.build() gọi đúng các getter trên đối tượng user
        return UserDetailsImpl.build(user);
    }
}