package kr.supporti.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.map.repository.config.EnableMapRepositories;

@Configuration
@EnableMapRepositories(basePackages = { "kr.supporti.common.keyvalue" })
public class KeyValueConfig {

}