package ua.khai.yarovyi.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BaseConfiguration {

    public static Map<String, String> init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File from = new File(BaseConfiguration.class.getClassLoader().getResource("properties.json").getFile());
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {{
        }};
        return mapper.readValue(from, typeRef);
    }
}