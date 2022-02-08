package kr.supporti.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.LecturerDataFileEntity;
import kr.supporti.api.common.entity.LecturerUploadFileEntity;

@Repository
public interface LecturerUploadFileRepository extends JpaRepository<LecturerUploadFileEntity, Long> {
}