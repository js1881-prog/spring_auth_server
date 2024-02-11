package cpg.back.auth.core.service;

import cpg.back.auth.core.dto.PublicKeyResponseDto;
import cpg.back.auth.util.KeyGenerator;
import cpg.back.auth.util.RS256Jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.MalformedKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CoreServiceTest {

    @InjectMocks
    CoreServiceImpl coreService;

    @Mock
    RS256Jwt rs256Jwt;

    @Mock
    KeyGenerator keyGenerator;

    @Mock
    Header header;

    @Mock
    Claims claims;

    @Mock
    KeyFactory keyFactory;

    @Mock
    PublicKey publicKey;

    @Mock
    RSAPublicKeySpec rsaPublicKeySpec;

    @Mock
    PublicKeyResponseDto publicKeyResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateJwt_success_case() {
        String expectedToken = "mockedJwtToken";
        when(rs256Jwt.createToken(anyString())).thenReturn(expectedToken);
        String actualToken = coreService.generateJwt();
        assertThat(actualToken).isEqualTo(expectedToken);
        verify(rs256Jwt, times(1)).createToken(anyString());
    }

    @Test
    void generateJwt_fail_case_shouldBe_throw_MalformedKeyException() {
        when(rs256Jwt.createToken(anyString())).thenThrow(new MalformedKeyException("MalformedKeyException"));
        Exception exception = assertThrows(MalformedKeyException.class, () -> coreService.generateJwt());
        assertThat(exception.getMessage()).isEqualTo("MalformedKeyException");
        verify(rs256Jwt, times(1)).createToken(anyString());
    }

    @Test
    void generateJwt_fail_case_shouldBe_throw_ExpiredJwtException() {
        when(rs256Jwt.createToken(anyString())).thenThrow(new ExpiredJwtException(header, claims, "ExpiredJwtException"));
        Exception exception = assertThrows(ExpiredJwtException.class, () -> coreService.generateJwt());
        assertThat(exception.getMessage()).isEqualTo("ExpiredJwtException");
        verify(rs256Jwt, times(1)).createToken(anyString());
    }

    @Test
    void generateJwt_fail_case_shouldBe_throw_UnsupportedJwtException() {
        when(rs256Jwt.createToken(anyString())).thenThrow(new UnsupportedJwtException("UnsupportedJwtException"));
        Exception exception = assertThrows(UnsupportedJwtException.class, () -> coreService.generateJwt());
        assertThat(exception.getMessage()).isEqualTo("UnsupportedJwtException");
        verify(rs256Jwt, times(1)).createToken(anyString());
    }

//    @Test
//    public void testExtractKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
//        PublicKey mockPublicKey = mock(PublicKey.class);
//        when(keyGenerator.getPublicKey()).thenReturn(mockPublicKey);
//
//        PublicKeyResponseDto result = publicKeyExtractor.extractKey();
//
//        assertEquals("expectedPublicKeyString", result.getPublicKey());
//        assertEquals("expectedModulus", result.getRSAExponent());
//        assertEquals("expectedExponent", result.getRSAModulus());
//    }


}
