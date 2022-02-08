package kr.supporti.common.keyvalue.repository;

import java.util.Optional;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

import kr.supporti.common.keyvalue.entity.JwtKeyValueEntity;

@Repository
public interface JwtKeyValueRepository extends KeyValueRepository<JwtKeyValueEntity, String> {

    public Optional<JwtKeyValueEntity> findByUsername(String username);

}