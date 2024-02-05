package cpg.back.auth.core.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@JsonSerialize
@Getter
public class PublicKeyResponseDto {
    private String publicKey; // 실제로는 사용되지 않는 형태로 변환된 공개키 문자열
    private String RSAExponent; // 공개키 지수의 16진수 문자열 표현
    private String RSAModulus; // 공개키 모듈러스의 16진수 문자열 표현
}