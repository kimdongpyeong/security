package kr.supporti.common.keyvalue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.supporti.common.keyvalue.entity.JwtKeyValueEntity;
import kr.supporti.common.keyvalue.repository.JwtKeyValueRepository;

@Service
public class JwtKeyValueService {

    @Autowired
    private JwtKeyValueRepository jwtKeyValueRepository;

    @Transactional(readOnly = true)
    public JwtKeyValueEntity getJwtKeyValue(String token) {
        return jwtKeyValueRepository.findById(token).orElse(null);
    }

    @Transactional
    public JwtKeyValueEntity createJwtKeyValue(JwtKeyValueEntity jwtKeyValueEntity) {
        return jwtKeyValueRepository.save(jwtKeyValueEntity);
    }

    @Transactional
    public JwtKeyValueEntity modifyJwtKeyValue(JwtKeyValueEntity jwtKeyValueEntity) {
        return jwtKeyValueRepository.save(jwtKeyValueEntity);
    }

    @Transactional
    public void removeJwtKeyValue(String token) {
        jwtKeyValueRepository.deleteById(token);
    }

    @Transactional(readOnly = true)
    public JwtKeyValueEntity getJwtKeyValueByUsername(String username) {
        return jwtKeyValueRepository.findByUsername(username).orElse(null);
    }

}