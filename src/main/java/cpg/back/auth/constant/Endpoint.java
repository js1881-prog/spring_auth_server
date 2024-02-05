package cpg.back.auth.constant;

import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;

@Getter
public class Endpoint {
    public static final String V1_AUTHS_ANONYMOUS = "/api/v1/auths/anonymous";
    public static final String V2_AUTHS_ANONYMOUS = "/api/v2/auths/anonymous";

    public static List<String> getEndpointsAsList() {
        List<String> endpoints = new ArrayList<>();
        Field[] fields = Endpoint.class.getFields();

        for (Field field : fields) {
            if (isStatic(field.getModifiers()) &&
                    isFinal(field.getModifiers()) &&
                    field.getType().equals(String.class)) {
                try {
                    String value = (String) field.get(null);
                    endpoints.add(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return endpoints;
    }
}
