package com.example.quitsmoking.repository;

import com.example.quitsmoking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Cần import Optional
import java.util.UUID; // Cần import UUID

@Repository
public interface UserRepository extends JpaRepository<User, UUID> { // <--- THAY ĐỔI TỪ Long SANG UUID

    // Tìm một User theo username
    // Sử dụng Optional để xử lý trường hợp không tìm thấy user, tốt hơn là trả về thẳng User
    Optional<User> findByUsername(String username); // <--- Nên trả về Optional<User>

    // Kiểm tra xem một username có tồn tại không
    boolean existsByUsername(String username);

    // Kiểm tra xem một email có tồn tại không
    boolean existsByEmail(String email);

    // Lưu ý: Các phương thức findById, existsById, deleteById từ JpaRepository
    // sẽ tự động nhận UUID làm kiểu ID sau khi bạn thay đổi JpaRepository<User, UUID>
}