package kr.supporti.common.keyvalue.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@KeySpace(value = "jwt")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtKeyValueEntity {

    @Id
    private String token;

    private String username;

    private Long userTypeCode;

}