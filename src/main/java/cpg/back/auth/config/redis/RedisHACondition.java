package cpg.back.auth.config.redis;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

@Component
public class RedisHACondition {

    public static class OnStandaloneCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String strategy = context.getEnvironment().getProperty("redis.ha.strategy");
            return "STANDALONE".equalsIgnoreCase(strategy);
        }
    }

    public static class OnSentinelCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String strategy = context.getEnvironment().getProperty("redis.ha.strategy");
            return "SENTINEL".equalsIgnoreCase(strategy);
        }
    }

    public static class OnClusterCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String strategy = context.getEnvironment().getProperty("redis.ha.strategy");
            return "CLUSTER".equalsIgnoreCase(strategy);
        }
    }
}
