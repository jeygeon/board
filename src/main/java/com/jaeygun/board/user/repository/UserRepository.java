package com.jaeygun.board.user.repository;

import com.jaeygun.board.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(String id);

    User findUserByIdAndPassword(String id, String password);

    List<User> findUserByNameAndPhoneNumber(String name, String phoneNumber);
}
