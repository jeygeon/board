package com.jaeygun.board.user.repository;

import com.jaeygun.board.user.entity.NaverUserKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaverUserKeyRepository extends JpaRepository<NaverUserKey, Long> {

    NaverUserKey findUserKeyBynKey(String key);
}
