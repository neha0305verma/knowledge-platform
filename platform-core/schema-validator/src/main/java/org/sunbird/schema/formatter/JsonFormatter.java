package org.sunbird.schema.formatter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.leadpony.justify.api.InstanceType;
import org.leadpony.justify.spi.FormatAttribute;

import javax.json.JsonString;
import javax.json.JsonValue;
import java.util.List;
import java.util.Map;

public class JsonFormatter implements FormatAttribute {

    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public String name() {
        return "json";
    }

    @Override
    public InstanceType valueType() {
       return InstanceType.STRING;
    }

    @Override
    public boolean test(JsonValue value) {
        Object data = null;
        String str = null;
        try {
            str = ((JsonString) value).getString();
            data = mapper.readValue(str, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            try {
                data = mapper.readValue(str, new TypeReference<List<Object>>() {
                });
            } catch (Exception ex) {
                return false;
            }
        }
        return null != data ? true : false;
    }
}
