package com.tweesky.cloudtools.codegen;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.EmailSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.config.CodegenConfigurator;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NativeMockServerCodegenTest {


    @Test
    public void verifyRouters() throws IOException {
        File output = Files.createTempDirectory("test").toFile();
        output.deleteOnExit();

        final CodegenConfigurator configurator = new CodegenConfigurator()
                .setGeneratorName("native-mock-server")
                .setSkipOperationExample(true)
                .setInputSpec("src/test/resources/specs/specByContractId.yaml")
                .setOutputDir(output.getAbsolutePath().replace("\\", "/"));

        DefaultGenerator generator = new DefaultGenerator();
        List<File> files = generator.opts(configurator.toClientOptInput()).generate();
        files.forEach(File::deleteOnExit);

        // check file exists
        TestUtils.assertFileExists(Paths.get(output + "/api/routers.go"));
        // check / route
        TestUtils.assertFileContains(Paths.get(output + "/api/routers.go"),
                "{\n" +
                        " \"Home\",\n " +
                        " http.MethodGet,\n " +
                        " \"/\",\n " +
                        " Index,\n " +
                        "}");
        // check /index route
        TestUtils.assertFileContains(Paths.get(output + "/api/routers.go"),
                "{\n" +
                        " \"Index\",\n " +
                        " http.MethodGet,\n " +
                        " \"/index/\",\n " +
                        " Index,\n " +
                        "}");
        // check /openapi route
        TestUtils.assertFileContains(Paths.get(output + "/api/routers.go"),
                "{\n" +
                        " \"OpenApi\",\n " +
                        " http.MethodGet,\n " +
                        " \"/openapi/\",\n " +
                        " OpenApi,\n " +
                        "}");
        // check /users/:userId route
        TestUtils.assertFileContains(Paths.get(output + "/api/routers.go"),
                "{\n" +
                        " \"GetUsersUserId\",\n " +
                        " http.MethodGet,\n " +
                        " \"/users/:userId\",\n " +
                        " GetUsersUserId,\n " +
                        "}");

    }

    @Test
    public void generateByContractId() throws IOException {
        File output = Files.createTempDirectory("test").toFile();
        output.deleteOnExit();

        final CodegenConfigurator configurator = new CodegenConfigurator()
                .setGeneratorName("native-mock-server")
                .setSkipOperationExample(true)
                .setInputSpec("src/test/resources/specs/specByContractId.yaml")
                .setOutputDir(output.getAbsolutePath().replace("\\", "/"));

        DefaultGenerator generator = new DefaultGenerator();
        List<File> files = generator.opts(configurator.toClientOptInput()).generate();
        files.forEach(File::deleteOnExit);

        TestUtils.assertFileContains(Paths.get(output + "/main.go"),
                "package main");

        TestUtils.assertFileExists(Paths.get(output + "/api/api_basic.go"));
        TestUtils.assertFileContains(Paths.get(output + "/api/api_basic.go"),
                "request: post-user-request-200 response: post-user-response-200");

    }

    @Test
    public void generateByRef() throws IOException {
        File output = Files.createTempDirectory("test").toFile();
        output.deleteOnExit();

        final CodegenConfigurator configurator = new CodegenConfigurator()
                .setGeneratorName("native-mock-server")
                .setSkipOperationExample(true)
                .setInputSpec("src/test/resources/specs/specByRef.yaml")
                .setOutputDir(output.getAbsolutePath().replace("\\", "/"));

        DefaultGenerator generator = new DefaultGenerator();
        List<File> files = generator.opts(configurator.toClientOptInput()).generate();
        files.forEach(File::deleteOnExit);
        files.forEach(System.out::println);

        TestUtils.assertFileContains(Paths.get(output + "/main.go"),
                "package main");

        TestUtils.assertFileExists(Paths.get(output + "/api/api_basic.go"));
        TestUtils.assertFileContains(Paths.get(output + "/api/api_basic.go"),
                "request: post-user response: post-user-200");

    }

    @Test
    public void generateFromSchema() throws IOException {
        File output = Files.createTempDirectory("test").toFile();
        output.deleteOnExit();

        final CodegenConfigurator configurator = new CodegenConfigurator()
                .setGeneratorName("native-mock-server")
                .setSkipOperationExample(true)
                .setInputSpec("src/test/resources/specs/specNoExamples.yaml")
                .setOutputDir(output.getAbsolutePath().replace("\\", "/"));

        DefaultGenerator generator = new DefaultGenerator();
        List<File> files = generator.opts(configurator.toClientOptInput()).generate();
        files.forEach(File::deleteOnExit);

        TestUtils.assertFileContains(Paths.get(output + "/main.go"),
                "package main");

        log(output + "/api/api_basic.go");

        TestUtils.assertFileExists(Paths.get(output + "/api/api_basic.go"));
        // check generated email address
        TestUtils.assertFileContains(Paths.get(output + "/api/api_basic.go"),
                "\"user@example.com\"");

    }

    @Test
    public void generateFromSchemaWithEnum() throws IOException {
        File output = Files.createTempDirectory("test").toFile();
        output.deleteOnExit();

        final CodegenConfigurator configurator = new CodegenConfigurator()
                .setGeneratorName("native-mock-server")
                .setSkipOperationExample(true)
                .setInputSpec("src/test/resources/specs/specWithEnum.yaml")
                .setOutputDir(output.getAbsolutePath().replace("\\", "/"));

        DefaultGenerator generator = new DefaultGenerator();
        List<File> files = generator.opts(configurator.toClientOptInput()).generate();
        files.forEach(File::deleteOnExit);

        TestUtils.assertFileContains(Paths.get(output + "/main.go"),
                "package main");

        log(output + "/api/api_basic.go");

        TestUtils.assertFileExists(Paths.get(output + "/api/api_basic.go"));
        // check country value from enum
        TestUtils.assertFileContains(Paths.get(output + "/api/api_basic.go"),
                "\"country\" : \"UK\"");

    }

    @Test
    public void getOperation() throws IOException {
        File output = Files.createTempDirectory("test").toFile();
        output.deleteOnExit();

        final CodegenConfigurator configurator = new CodegenConfigurator()
                .setGeneratorName("native-mock-server")
                .setSkipOperationExample(true)
                .setInputSpec("src/test/resources/specs/specs-no-examples.yaml")
                .setOutputDir(output.getAbsolutePath().replace("\\", "/"));

        DefaultGenerator generator = new DefaultGenerator();
        List<File> files = generator.opts(configurator.toClientOptInput()).generate();
        files.forEach(File::deleteOnExit);

        TestUtils.assertFileContains(Paths.get(output + "/main.go"),
                "package main");

        log(output + "/api/api_basic.go");

        TestUtils.assertFileExists(Paths.get(output + "/api/api_basic.go"));
        // check generated email address
        TestUtils.assertFileContains(Paths.get(output + "/api/api_basic.go"),
                "\"user@example.com\"");

    }

    @Test
    public void generateByName() throws IOException {
        File output = Files.createTempDirectory("test").toFile();
        output.deleteOnExit();

        final CodegenConfigurator configurator = new CodegenConfigurator()
                .setGeneratorName("native-mock-server")
                .setSkipOperationExample(true)
                .setInputSpec("src/test/resources/specs/specByName.yaml")
                .setOutputDir(output.getAbsolutePath().replace("\\", "/"));

        DefaultGenerator generator = new DefaultGenerator();
        List<File> files = generator.opts(configurator.toClientOptInput()).generate();
        files.forEach(File::deleteOnExit);

        TestUtils.assertFileContains(Paths.get(output + "/main.go"),
                "package main");

        log(output + "/api/api_basic.go");

        TestUtils.assertFileExists(Paths.get(output + "/api/api_basic.go"));
        TestUtils.assertFileContains(Paths.get(output + "/api/api_basic.go"),
                "// request: get-user-valid response: get-user-valid");

        TestUtils.assertFileContains(Paths.get(output + "/api/api_basic.go"),
                "// request: new-user response: basic");
    }

    @Test
    public void generateGetExample() throws IOException {
        File output = Files.createTempDirectory("test").toFile();
        output.deleteOnExit();

        final CodegenConfigurator configurator = new CodegenConfigurator()
                .setGeneratorName("native-mock-server")
                .setSkipOperationExample(true)
                .setInputSpec("src/test/resources/specs/get-example.yaml")
                .setOutputDir(output.getAbsolutePath().replace("\\", "/"));

        DefaultGenerator generator = new DefaultGenerator();
        List<File> files = generator.opts(configurator.toClientOptInput()).generate();
        files.forEach(File::deleteOnExit);

        log(output + "/api/api_basic.go");

        TestUtils.assertFileContains(Paths.get(output + "/main.go"),
                "package main");

        TestUtils.assertFileExists(Paths.get(output + "/api/api_basic.go"));
        TestUtils.assertFileContains(Paths.get(output + "/api/api_basic.go"),
                "request: valid response: get-user-id-0001");

    }


    @Test
    public void extractExampleByName() {
        String str = "#/components/examples/get-user-basic";

        assertEquals("get-user-basic", new NativeMockServerCodegen().extractNameFromRef(str));
    }

    @Test
    public void formatJson() {

        final String EXPECTED = "{\n  \"id\" : 1,\n  \"city\" : \"Amsterdam\"\n}";
        final String JSON = "{\"id\":1,\"city\":\"Amsterdam\"}";

        assertEquals(EXPECTED, new NativeMockServerCodegen().formatJson(JSON));

    }

    @Test
    public void convertObjectNodeToJson() {

        final String EXPECTED = "{\n  \"id\" : 1,\n  \"city\" : \"Amsterdam\"\n}";

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode city = mapper.createObjectNode();

        city.put("id", 1);
        city.put("city", "Amsterdam");

        assertEquals(EXPECTED, new NativeMockServerCodegen().convertToJson(city));

    }

    @Test
    public void convertLinkedHashMapToJson() {
        String EXPECTED = "{\n" +
                "  \"country\" : \"NL\",\n" +
                "  \"key\" : \"\",\n" +
                "  \"key2\" : 0,\n" +
                "  \"key3\" : \"user@example.com\",\n" +
                "  \"key4\" : [ ]\n" +
                "}";

        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>(
                ImmutableMap.of(
                        "country", new StringSchema(),
                        "key", new StringSchema(),
                        "key2", new IntegerSchema(),
                        "key3", new EmailSchema(),
                        "key4", new ArraySchema()
                        ));

        String json = new NativeMockServerCodegen().convertToJson(linkedHashMap);
        assertEquals(EXPECTED, json);
    }

    @Test
    public void getInputSpecFilename() {

        String INPUT_SPEC = "/dir/subdir/myopenapi.yaml";
        String EXPECTED = "myopenapi.yaml";
        assertEquals(EXPECTED, new NativeMockServerCodegen().getInputSpecFilename(INPUT_SPEC));
    }

    private void log(String filename) throws IOException {
        System.out.println(new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8));
    }

}
