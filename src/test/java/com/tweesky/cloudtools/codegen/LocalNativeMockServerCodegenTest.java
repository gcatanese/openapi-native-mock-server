package com.tweesky.cloudtools.codegen;

import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.config.CodegenConfigurator;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LocalNativeMockServerCodegenTest {

    @Test
    public void generate() throws IOException {
        File output = Files.createTempDirectory("test").toFile();
        output.deleteOnExit();

        final CodegenConfigurator configurator = new CodegenConfigurator()
                .setGeneratorName("native-mock-server")
//                .setInputSpec("openapi/tmp/LegalEntityService-v3.json")
                .setInputSpec("openapi/tmp/users-openapi.yaml")
//                .setInputSpec("openapi/tmp/CheckoutService-v70.yaml")
//                .setOutputDir(output.getAbsolutePath().replace("\\", "/"));
                .setOutputDir("openapi/tmp/go-server");

        DefaultGenerator generator = new DefaultGenerator();
        List<File> files = generator.opts(configurator.toClientOptInput()).generate();
        //files.forEach(File::deleteOnExit);

        //log(output + "/api/api_basic.go");

//        TestUtils.assertFileContains(Paths.get(output + "/main.go"),
//                "package main");

    }

    private void log(String filename) throws IOException {
        System.out.println(new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8));
    }


}
