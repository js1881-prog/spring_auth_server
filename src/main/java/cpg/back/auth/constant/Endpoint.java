package cpg.back.auth.constant;

import lombok.Getter;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Endpoint {
    public static final String V1_AUTHS_ANONYMOUS = "/api/v1/auths/anonymous";
    public static final String V2_AUTHS_ANONYMOUS = "/api/v2/auths/anonymous";
    public static final String V1_PUBLIC_KEY = "/api/v1/auths/public-key";

    public static List<String> getEndpointsAsList() {
        return Arrays.stream(Endpoint.class.getFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()) &&
                        Modifier.isFinal(field.getModifiers()) &&
                        field.getType().equals(String.class))
                .map(field -> {
                    try {
                        return (String) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
