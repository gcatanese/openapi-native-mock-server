package com.tweesky.cloudtools.codegen.samples;

import io.swagger.v3.oas.models.media.JsonSchema;
import io.swagger.v3.oas.models.media.StringSchema;

public class StringSample implements SampleValue {

    private StringSchema schema;
    private JsonSchema jsonSchema;

    public StringSample() {
    }

    public StringSample(StringSchema schema) {
        this.schema = schema;
    }

    public StringSample(JsonSchema schema) {
        this.jsonSchema = schema;
    }

    @Override
    public String getValue(String key) {

        String value = "";

        // check enum (when underlying object is a Schema)
        if (schema != null &&schema.getEnum() != null && !schema.getEnum().isEmpty()) {
            return schema.getEnum().get(0);
        }

        // check enum (when underlying object is a JsonSchema)
        if (jsonSchema != null &&jsonSchema.getEnum() != null && !jsonSchema.getEnum().isEmpty()) {
            return (String) jsonSchema.getEnum().get(0);
        }
        // generate a 'reasonable' value for the given field
        if (key.equalsIgnoreCase("currency")) {
            value = "EUR";
        } else if (key.equalsIgnoreCase("type")) {
            value = "type_a";
        } else if (key.equalsIgnoreCase("country")) {
            value = "NL";
        } else if (key.equalsIgnoreCase("countryCode")) {
            value = "NL";
        } else if (key.equalsIgnoreCase("postalCode")) {
            value = "11AA";
        } else if (key.equalsIgnoreCase("city")) {
            value = "Amsterdam";
        } else if (key.equalsIgnoreCase("street")) {
            value = "Park Av.";
        } else if (key.equalsIgnoreCase("nationality")) {
            value = "NL";
        } else if (key.equalsIgnoreCase("url")) {
            value = "https://www.example.com";
        } else if (key.equalsIgnoreCase("email")) {
            value = "user@example.com";
        } else if (key.endsWith("email") || key.endsWith("Email")) {
            value = "user@example.com";
        } else if (key.equalsIgnoreCase("firstname")) {
            value = "Alice";
        } else if (key.equalsIgnoreCase("lastname")) {
            value = "Cooper";
        } else if (key.equalsIgnoreCase("iban")) {
            value = "NL13TEST0123456789";
        } else if (key.equalsIgnoreCase("telephonenumber")) {
            value = "09 1234567890";
        } else if (key.equalsIgnoreCase("os")) {
            value = "n/a";
        } else if (key.equalsIgnoreCase("locale")) {
            value = "en";
        } else if (key.endsWith("version") || key.endsWith("Version")) {
            value = "1.0";
        } else if (key.endsWith("reference") || key.endsWith("Reference")) {
            value = "ref001";
        }
        return value;
    }
}
