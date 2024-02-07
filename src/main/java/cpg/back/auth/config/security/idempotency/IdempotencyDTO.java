package cpg.back.auth.config.security.idempotency;


import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class IdempotencyDTO {
    private final String requestId;
    private final String ip;
    private final String agent;
    private final String method;
    private final Date createdAt;
}
