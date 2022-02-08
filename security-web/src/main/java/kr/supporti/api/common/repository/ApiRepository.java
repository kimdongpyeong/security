package kr.supporti.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.ApiEntity;

@Repository
public interface ApiRepository extends JpaRepository<ApiEntity, Long> {

}