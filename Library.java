package phoenixbff;
import org.testng.ITestContext;

import java.io.*;

import core.graphql.utilities.Utilities;
import core.graphql.utilities.APIService;
import static core.rest.SpecBuilder.getRequestSpec;

import org.testng.annotations.*;
import phoenixbff.GraphQL.PayLoadMutation;


import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.testng.annotations.AfterSuite;
import phoenixbff.GraphQL.PayLoadQuery;
import phoenixbff.Rest.APIWrapper;
import phoenixbff.Rest.TestDataConfig;
import phoenixbff.Rest.JsonBuilder;

import org.apache.http.client.methods.CloseableHttpResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import org.apache.http.HttpEntity;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import io.restassured.path.json.JsonPath;

import org.testng.Assert;

import java.util.List;

public class Library {



    @Test(groups = "smoke", description = "legacyArticleGet",priority = 3)
    public void legacyArticleGet(ITestContext context)  {

        String nullString = null;
        JsonBuilder authRequest = APIWrapper.authBuilder(TestDataConfig.authProgram);
        Response response = APIWrapper.post(authRequest, nullString);
        assertThat(response.statusCode(), equalTo(200));

        JsonPath jsonPathEvaluator = response.jsonPath();
        String accesstoken = jsonPathEvaluator.get("access_token");
        System.out.println("access token is: " + accesstoken);
        context.setAttribute("accesstoken", accesstoken);
        String Env = (String) context.getAttribute("Env");
        String token = (String) context.getAttribute("accesstoken");
        String etsessionid = (String) context.getAttribute("etsessionid");
        System.out.println(":etsession id is " + etsessionid);
        String payload = PayLoadQuery.legacyArticleGet();

        Response res = APIService.sendAPIRequest(payload,token);
        assertThat(res.statusCode(), equalTo(200));

        JsonPath js = Utilities.RawToJSON(res);

        String status = js.get("data.aePlaylistCreate.uuid");
        System.out.println("Response Body is: " + status);



    }
    @Test(groups = "smoke", description = "articleGet",priority = 4)
    public void ArticleGet(ITestContext context)  {

        String nullString = null;
        JsonBuilder authRequest = APIWrapper.authBuilder(TestDataConfig.authProgram);
        Response response = APIWrapper.post(authRequest, nullString);
        assertThat(response.statusCode(), equalTo(200));

        JsonPath jsonPathEvaluator = response.jsonPath();
        String accesstoken = jsonPathEvaluator.get("access_token");
        System.out.println("access token is: " + accesstoken);
        context.setAttribute("accesstoken", accesstoken);
        String Env = (String) context.getAttribute("Env");
        String token = (String) context.getAttribute("accesstoken");
        String etsessionid = (String) context.getAttribute("etsessionid");
        System.out.println(":etsession id is " + etsessionid);
        String payload = PayLoadQuery.articleGet();

        Response res = APIService.sendAPIRequest(payload,token);
        assertThat(res.statusCode(), equalTo(200));

        JsonPath js = Utilities.RawToJSON(res);

        String status = js.get("data.aePlaylistCreate.uuid");
        System.out.println("Response Body is: " + status);



    }
    @Test(groups = "smoke", description = "legacyCreateArticleRead",priority = 5)
    public void legacyCreateArticleRead(ITestContext context)  {

        String nullString = null;
        JsonBuilder authRequest = APIWrapper.authBuilder(TestDataConfig.authProgram);
        Response response = APIWrapper.post(authRequest, nullString);
        assertThat(response.statusCode(), equalTo(200));

        JsonPath jsonPathEvaluator = response.jsonPath();
        String accesstoken = jsonPathEvaluator.get("access_token");
        System.out.println("access token is: " + accesstoken);
        context.setAttribute("accesstoken", accesstoken);
        String Env = (String) context.getAttribute("Env");
        String token = (String) context.getAttribute("accesstoken");
        String etsessionid = (String) context.getAttribute("etsessionid");
        System.out.println(":etsession id is " + etsessionid);
        String payload = PayLoadMutation.legacyCreateArticleRead();

        Response res = APIService.sendAPIRequest(payload,token);
        assertThat(res.statusCode(), equalTo(200));

        JsonPath js = Utilities.RawToJSON(res);

        String status = js.get("data.aePlaylistCreate.uuid");
        System.out.println("Response Body is: " + status);



    }


    @AfterSuite(groups = "smoke")
    public void simpletest() throws IOException {

        String TriggerOff = System.getProperty("SlackTriggerOff");
        System.out.println("Env is: " + TriggerOff);
        if (TriggerOff == null) {
            System.out.println("Into Env ");
            try {
                // Define the command to run the Bash script
                String command = "bash  /apiframework/src/test/java/phoenixbff/curlscript.sh ";  // Replace with the actual path

                // Execute the Bash script using Runtime
                Process process = Runtime.getRuntime().exec(command);

                // Wait for the process to complete
                int exitCode = process.waitFor();

                // Print the exit code
                System.out.println("Bash script exit code: " + exitCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
