package ua.khai.yarovyi.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BaseConfiguration {

    /**
     * Loads base configuration from properties.json file.
     *
     * @return Initialized key-value dictionary with sting properties
     * @throws IOException in case if JSON is invalid or file was no found
     */
    public static Map<String, String> init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File from = new File(BaseConfiguration.class.getClassLoader().getResource("properties.json").getFile());
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {{
        }};
        return mapper.readValue(from, typeRef);
    }
}