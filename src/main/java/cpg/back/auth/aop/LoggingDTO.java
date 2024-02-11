package cpg.back.auth.aop;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class LoggingDTO {
    private final String requestId;
    private final String requestMethod;
    private final String requestURI;
    private final String requestIp;
    private final String requestAgent;

    private final int responseStatus;
    private Long elapsedTime;
    private Long requestStartTime;
    private Long responseEndTime;
}


