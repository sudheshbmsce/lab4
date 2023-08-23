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

public class apitest  {

    @Test(groups = "smoke",description = "VerifyAuthentication",priority = 1)
    public void verifyAuthentication(ITestContext context)  {

           String nullString = null;
           JsonBuilder authRequest = APIWrapper.authBuilder(TestDataConfig.authProgram);
           Response response = APIWrapper.post(authRequest, nullString);
           assertThat(response.statusCode(), equalTo(200));

           JsonPath jsonPathEvaluator = response.jsonPath();
           String accesstoken = jsonPathEvaluator.get("access_token");
           System.out.println("access token is: " + accesstoken);
           context.setAttribute("accesstoken", accesstoken);
           String Env = (String) context.getAttribute("Env");

         }


    @Test(groups = "smoke",description = "GetUserInfo", priority = 2)
    public void GetuserInfo(ITestContext context) {


        String token = (String) context.getAttribute("accesstoken");
        System.out.println("passed token is: " + token);
        String payload = PayLoadQuery.GetUserInfo();


        Response res = APIService.sendAPIRequest(payload,token);

        JsonPath js = Utilities.RawToJSON(res);
        assertThat(res.statusCode(), equalTo(200));


        String status = js.get("data.aePlaylistCreate.uuid");
        String uuid = js.getString("data.userGet.activityPlans.uuid[0]");
        String programName = js.get("data.userGet.primaryPathway.programIndication.programName");
        Integer  primaryPathwayID = js.get("data.userGet.primaryPathway.id");

        String activitysessionuuid = js.getString("data.userGet.activityPlans.uuid[0]");


        System.out.println("pathwayId is: " + primaryPathwayID);
        System.out.println("pathwayId is: " + primaryPathwayID);
        System.out.println("pathwayId is: " + uuid );
        context.setAttribute("pathwayId", primaryPathwayID);
        context.setAttribute("activityplanuuid", uuid);

        Assert.assertNotNull(uuid);
        Assert.assertNotNull(primaryPathwayID);

    }
    @Test(groups = "smoke",description = "Create play list",priority = 3)
    public void CreatePlaylist(ITestContext context)  {

        String token = (String) context.getAttribute("accesstoken");
        String activityplanuuid = (String) context.getAttribute("activityplanuuid");
        Integer pathwayid = (Integer) context.getAttribute("pathwayId");
        String pathwayidstring =String.valueOf(pathwayid);
        System.out.println("passed token is: " + token);

        System.out.println("passed token is: " + activityplanuuid);
        System.out.println("passed token is: " + token);
        System.out.println("passed token is: " + pathwayidstring);

        String payload = PayLoadMutation.saveFeedback(activityplanuuid,pathwayidstring);

        Response res = APIService.sendAPIRequest(payload,token);
        assertThat(res.statusCode(), equalTo(200));

        JsonPath js = Utilities.RawToJSON(res);

        String etsessionuuid = js.get("data.aePlaylistCreate.playlistItems.etSession.uuid");

        System.out.println("Response Body is: " + etsessionuuid);
        context.setAttribute("etsessionid", etsessionuuid);

        Assert.assertNotNull(etsessionuuid);

    }

    @Test(groups = "smoke",description = "ChronicuserBasicInfoGet",priority = 3)
    public void ChronicuserBasicInfoGet(ITestContext context)  {

        String token = (String) context.getAttribute("accesstoken");
        String etsessionid= (String) context.getAttribute("etsessionuuid");
        Integer pathwayid = (Integer) context.getAttribute("pathwayId");
        String pathwayidstring =String.valueOf(pathwayid);
        System.out.println("passed token is: " + token);

        System.out.println("passed token is: " + etsessionid);
        System.out.println("passed token is: " + token);
        System.out.println("passed token is: " + pathwayidstring);

        String payload = PayLoadQuery.chronicUserBasicInfoGet();

        Response res = APIService.sendAPIRequest(payload,token);
        assertThat(res.statusCode(), equalTo(200));

        JsonPath js = Utilities.RawToJSON(res);

        String status = js.get("data.aePlaylistCreate.playlistItems.etSession.uuid");

        System.out.println("Response Body is: " + status);
        context.setAttribute("etsessionid", status);



    }
    @Test(groups = "smoke", description = "Saving progress",priority = 4)
    public void SavingProgress(ITestContext context)  {

        String token = (String) context.getAttribute("accesstoken");
        String etsessionid = (String) context.getAttribute("etsessionid");
        System.out.println(":etsession id is " + etsessionid);
        String payload = PayLoadMutation.SavingProgress(etsessionid);

        Response res = APIService.sendAPIRequest(payload,token);
        assertThat(res.statusCode(), equalTo(200));

        JsonPath js = Utilities.RawToJSON(res);
        String status = js.get("data.etPlaylistItemRecordSave.uuid");
        Assert.assertNotNull(status);

        System.out.println("Response Body is: " + status);

    }

    @Test(groups = "smoke", description = "ETcheckinget",priority = 4)
    public void ETcheckinget(ITestContext context)  {

        String token = (String) context.getAttribute("accesstoken");
        String etsessionid = (String) context.getAttribute("etsessionid");
        System.out.println(":etsession id is " + etsessionid);
        String payload = PayLoadQuery.ETcheckinfunctionalget();

        Response res = APIService.sendAPIRequest(payload,token);
        assertThat(res.statusCode(), equalTo(200));

        JsonPath js = Utilities.RawToJSON(res);
        String status = js.get("data.etPlaylistItemRecordSave.uuid");


        System.out.println("Response Body is: " + status);

    }
    @Test(groups = "smoke", description = "PlaylistAggregateGet",priority = 5)
    public void PlaylistAggregateGet(ITestContext context)  {

        String token = (String) context.getAttribute("accesstoken");
        String etsessionid = (String) context.getAttribute("etsessionid");
        System.out.println(":etsession id is " + etsessionid);
        String payload = PayLoadQuery.PlaylistAggregateGet();

        Response res = APIService.sendAPIRequest(payload,token);
        assertThat(res.statusCode(), equalTo(200));

        JsonPath js = Utilities.RawToJSON(res);
        String status = js.get("data.aePlaylistCreate.uuid");
        String uuid = js.getString("data.userGet.activityPlans.uuid[0]");

        String playlistuuid = js.getString("data.playlistAggregatesGet[0].uuid");
        System.out.println("Response Body is: " + playlistuuid);


    }
    @Test(groups = "smoke",description = "PlaylistItemAggregateGet",priority = 6)
    public void PlaylistItemAggregateGet(ITestContext context)  {

        String token = (String) context.getAttribute("accesstoken");
        String etsessionid = (String) context.getAttribute("etsessionid");
        System.out.println(":etsession id is " + etsessionid);
        String payload = PayLoadQuery.PlaylistAggregateGet();

        Response res = APIService.sendAPIRequest(payload,token);
        assertThat(res.statusCode(), equalTo(200));

        JsonPath js = Utilities.RawToJSON(res);
        String status = js.get("data.aePlaylistCreate.uuid");
        System.out.println("Response Body is: " + status);

    }
    @Test(groups = "smoke",description = "Pre-etsession",priority = 7)
    public void Preetsession(ITestContext context)  {


        String token = (String) context.getAttribute("accesstoken");
        String etsessionid = (String) context.getAttribute("etsessionid");
        System.out.println(":etsession id is " + etsessionid);
        String payload = PayLoadMutation.Preetsession();

        Response res = APIService.sendAPIRequest(payload,token);
        assertThat(res.statusCode(), equalTo(200));

        JsonPath js = Utilities.RawToJSON(res);

        String status = js.get("data.aePlaylistCreate.uuid");
        System.out.println("Response Body is: " + status);



    }

    @Test(groups = "smoke", description = "Login_and_user_info",priority = 8)
    public void Login_and_user_info(ITestContext context)  {


        String token = (String) context.getAttribute("accesstoken");
        String etsessionid = (String) context.getAttribute("etsessionid");
        System.out.println(":etsession id is " + etsessionid);
        String payload = PayLoadMutation.Login_and_user_info();

        Response res = APIService.sendAPIRequest(payload,token);
        assertThat(res.statusCode(), equalTo(200));

        JsonPath js = Utilities.RawToJSON(res);

        String status = js.get("data.aePlaylistCreate.uuid");
        System.out.println("Response Body is: " + status);



    }

    @Test(groups = "smoke", description = "Logout_Mutation(",priority = 9)
    public void Logout_Mutation(ITestContext context)  {


        String token = (String) context.getAttribute("accesstoken");
        String etsessionid = (String) context.getAttribute("etsessionid");
        System.out.println(":etsession id is " + etsessionid);
        String payload = PayLoadMutation.Logout_mutation();

        Response res = APIService.sendAPIRequest(payload,token);
        assertThat(res.statusCode(), equalTo(200));

        JsonPath js = Utilities.RawToJSON(res);

        String status = js.get("data.aePlaylistCreate.uuid");
        System.out.println("Response Body is: " + status);

    }


    @Test(groups = "smoke", description = "Playlist_save_record",priority = 10)
    public void Playlist_save_record(ITestContext context)  {


        String token = (String) context.getAttribute("accesstoken");
        String etsessionid = (String) context.getAttribute("etsessionid");
        System.out.println(":etsession id is " + etsessionid);
        String payload = PayLoadMutation.Logout_mutation();

        Response res = APIService.sendAPIRequest(payload,token);
        assertThat(res.statusCode(), equalTo(200));

        JsonPath js = Utilities.RawToJSON(res);

        String status = js.get("data.aePlaylistCreate.uuid");

        System.out.println("Response Body is: " + status);
    }

    @Test(groups = "smoke", description = "password_reset_mutation",priority = 11)
    public void password_reset_mutation(ITestContext context)  {


        String token = (String) context.getAttribute("accesstoken");
        String etsessionid = (String) context.getAttribute("etsessionid");
        System.out.println(":etsession id is " + etsessionid);
        String payload = PayLoadMutation.password_reset_mutation();

        Response res = APIService.sendAPIRequest(payload,token);
        assertThat(res.statusCode(), equalTo(200));

        JsonPath js = Utilities.RawToJSON(res);

        String status = js.get("data.aePlaylistCreate.uuid");

        System.out.println("Response Body is: " + status);
    }

    @Test(groups = "smoke", description = "Login_Tokens_Query",priority = 12)
    public void Login_Tokens_Query(ITestContext context)  {


        String token = (String) context.getAttribute("accesstoken");
        String etsessionid = (String) context.getAttribute("etsessionid");
        System.out.println(":etsession id is " + etsessionid);
        String payload = PayLoadMutation.Login_token_query();

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

















