package com.jaeygun.board.user.repository;

import com.jaeygun.board.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(String id);

    User findUserByIdAndPassword(String id, String password);
}
