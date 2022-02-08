package kr.supporti.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.LecturerDataEntity;

@Repository
public interface LecturerDataRepository extends JpaRepository<LecturerDataEntity, Long> {
}