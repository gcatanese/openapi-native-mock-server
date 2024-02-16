package com.tweesky.cloudtools.codegen.util;

import com.tweesky.cloudtools.codegen.samples.DateSample;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;

public class RequestHelperTest {

    RequestHelper requestHelper = new RequestHelper();

    @Test
    public void getCurl() {

        final String URL = "http://localhost:8080/users/usr0001";
        final String VERB = "get";

        assertEquals("curl -X GET 'http://localhost:8080/users/usr0001'",
                requestHelper.getCurl(URL, VERB));
    }

    @Test
    public void getCurlWithPost() {

        final String URL = "http://localhost:8080/users/usr0001";
        final String VERB = "post";
        final String BODY = "{\n  \"id\" : 1,\n  \"city\" : \"Amsterdam\"\n}";

        final String EXPECTED = "curl -X POST 'http://localhost:8080/users/usr0001' -d '{\n" +
                "  \"id\" : 1,\n" +
                "  \"city\" : \"Amsterdam\"\n" +
                "}'";

        assertEquals(EXPECTED, requestHelper.getCurl(URL, VERB, BODY));
    }
}
