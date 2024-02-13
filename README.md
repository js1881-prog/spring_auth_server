# spring_boot_auth_server 

SpringBoot boilerplate for creating REST API server 👍

made by [js1881-prog](https://github.com/js1881-prog)

## Quick Overview 🚀

```
1. jwt(RS256) 기반의 인증
2. 멱등성 유지를 위한 idempotency-key Filter 
3. Deprecated Router 처리를 위한 URL 포워딩 interceptor 
4. MDC Logging Filter 제공, OnePerRequest에 대한 uuid 로깅(logback.xml 기반)
5. Redis HA Strategy에 대한 동적인 configuration 제공, env파일 기반 sentinel, cluster HA 다수의 nodes 등록 기능
6. Centralized Error Handler, RunTimeException에 대한 Response Builder
7. Filter, controller에 대한 tdd, endpoint 테스팅
```


## Dependencies

[ Java 17, SpringBoot 3.2.2 ]

```
- [spring-boot-starter-security:3.1.2]
- [lettuce]
- [jjwt:0.12.5]
- [guava:r05]
- [spring-boot-aop]
- [log4j:2.2.0.0]
- [lombok]
```

## Functions

### 1. Security 기반의 Custom Filters



<img width="854" alt="20240213_155356" src="https://github.com/js1881-prog/spring_auth_server/assets/98295182/22819cb2-d64e-492c-91f6-d908bdb2c981">





1. [MDCLogging Filter](https://github.com/js1881-prog/spring_auth_server/blob/main/src/main/java/cpg/back/auth/config/security/log/MDCLoggingFilter.java)로 하나의 request instance 특정(uuid) 지급, 
   [aop기반 후처리 logging으로](https://github.com/js1881-prog/spring_auth_server/blob/main/src/main/java/cpg/back/auth/aop/RequestLoggingAspect.java) 어플리케이션이 비지니스 로직 처리후 response를 보낼때까지의 period측정

2. csrf 체크 ([env 파일기반 optional](https://github.com/js1881-prog/spring_auth_server/blob/main/src/main/resources/application.properties.sample))

3. [멱등성 체크(POST, PATCH, CONNECT)](https://github.com/js1881-prog/spring_auth_server/blob/main/src/main/java/cpg/back/auth/config/security/idempotency/IdempotencyFilter.java)

          ! 사용 조건

           3.1 클라이언트 사이드에서 멱등키를 관리하고 같은 엔드포인트-HTTP메소드 요청시 멱등키는 unique해야한다.

           3.2 서버는 기존의 멱등키가 존재하면 요청을 reject, 멱등키가 없으면 resolve한다.






### 2. URL 포워딩 interceptor 

<img width="1241" alt="20240213_162931" src="https://github.com/js1881-prog/spring_auth_server/assets/98295182/08b8a943-31d3-4899-98fc-919db1622976">


1. [모든 엔드포인트 ENUM화 관리](https://github.com/js1881-prog/spring_auth_server/blob/main/src/main/java/cpg/back/auth/constant/Endpoint.java)
2. [상위 버전의 엔드포인트가 존재할시, 상위 버전으로 redirect call](https://github.com/js1881-prog/spring_auth_server/blob/main/src/main/java/cpg/back/auth/interceptor/ApiVersionRedirectInterceptor.java)
3. [하위 버전의 controller route는 Depecated처리](https://github.com/js1881-prog/spring_auth_server/blob/main/src/main/java/cpg/back/auth/core/controller/CoreController.java)
   
 ```
"/api/v1/route" 엔드포인트는, "/api/v2/route"의 상위 버전이 존재함으로 V2로 리다이렉트 됩니다. 실 개발시 FE팀의 편의 제공을 목적으로,
미처 처리하지 못한 엔드포인트 라우팅 처리를 동적으로 처리함에 목적이 있습니다.
```



### 3. Centralized Error Handler

  <img width="1227" alt="20240213_165655" src="https://github.com/js1881-prog/spring_auth_server/assets/98295182/f6f20a90-ee3f-4a96-8cd9-c3bfd14c465e">



1. [RunTimeError들에 대한 중앙 집중화](https://github.com/js1881-prog/spring_auth_server/blob/main/src/main/java/cpg/back/auth/exception/GlobalHandler.java), 가벼운 에러 로깅 기능, try-catch Brace없는 깔끔한 코드 컨벤션 처리가 목적
2. Spring이 제공하는 Default Exception 외에, 상황에 따른 CustomException도 확장성 높게 추가 가능



### 4. Test Code

1. [security](https://github.com/js1881-prog/spring_auth_server/blob/main/src/test/java/cpg/back/auth/config/security/SecurityConfigTest.java), [filter에 대한 엔드포인트 테스트](https://github.com/js1881-prog/spring_auth_server/blob/main/src/test/java/cpg/back/auth/config/security/filter/IdempotencyTest.java)
2. [URL포워딩 interceptor, 엔드포인트 테스트](https://github.com/js1881-prog/spring_auth_server/blob/main/src/test/java/cpg/back/auth/interceptor/ApiVersionRedirectInterceptorTest.java)
3. [CoreService, tdd](https://github.com/js1881-prog/spring_auth_server/blob/main/src/test/java/cpg/back/auth/core/service/CoreServiceTest.java)
