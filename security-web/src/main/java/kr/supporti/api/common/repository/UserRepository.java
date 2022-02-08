package kr.supporti.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "SELECT * FROM tb_user WHERE username = :username", nativeQuery = true)
    public UserEntity selectByUsername(@Param("username") String username);
}