package com.bigeye.mort.apitest.demo;

import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

/**
 * Created by yingzhang on 25/07/2017.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataSourceAPI {

    private static String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkNmRhODBiZi00MTAwLTQ1YzUtODZjNy02Y2E1N2UwZjc2MTgiLCJpc3MiOiJCaWdFeWUiLCJleHAiOjE1ODczNzM1ODl9.M9TrTv2P8WLLlSH7Q5PrcSaL5l8jGRU4XVQt0RLGK5I";
    private static String dataSourceID;
    private static String dataSetID;
    private static String dataSourceDirectoryID;
    private static String dataSetDirectoryID;
    private static String reportDirectoryID = "";

    @Test
    public void test_1_CreateDirectories() {

        String postData = "{\"name\":\"API测试\",\"directoryItemIds\":[],\"description\":\"\"}";

        Response response =
                given()
                        .contentType("application/json")
                        .body(postData)
                        .when()
                        .post("http://127.0.0.1/mort/data-sources/directories?token=" + token)
                        .then()
                        .log().all()
                        .assertThat()
                        .statusCode(201)
                        .extract().response();

        Headers allHeaders = response.headers();
        dataSourceDirectoryID = allHeaders.get("location").toString().substring(35);
        System.out.println(dataSourceDirectoryID);


        postData = "{\"name\":\"API测试\",\"directoryItemIds\":[],\"description\":\"\"}";

        response =
                given()
                        .contentType("application/json")
                        .body(postData)
                        .when()
                        .post("http://127.0.0.1/mort/data-sets/directories?token=" + token)
                        .then()
                        .log().all()
                        .assertThat()
                        .statusCode(201)
                        .extract().response();

        allHeaders = response.headers();
        dataSetDirectoryID = allHeaders.get("location").toString().substring(32);
        System.out.println(dataSetDirectoryID);
    }

    @Test
    public void test_2_CreateRDBDataSource() {

        String postData = "{\"name\":\"RDBDataSource\"," +
                "\"dataSourceType\":\"RDB\"," +
                "\"directoryIds\":[\"" + dataSourceDirectoryID + "\"]," +
                "\"options\"" +
                ":{\"databaseType\":\"mysql\"," +
                "\"host\":\"localhost\"," +
                "\"port\":\"3306\"," +
                "\"database\":\"customerdata_dev\"," +
                "\"username\":\"bigeye\"," +
                "\"password\":\"bigeye123\"}}";

        Response response =
                given()
                        .contentType("application/json")
                        .body(postData)
                        .when()
                        .post("http://127.0.0.1/mort/data-sources?token=" + token)
                        .then()
                        .log().all()
                        .assertThat()
                        .statusCode(201)
                        .extract().response();

        Headers allHeaders = response.headers();
        dataSourceID = allHeaders.get("location").toString().substring(23);
        System.out.println(dataSourceID);
    }

    @Test
    public void test_3_CreateRDBDataSet() {

        String postData = "{\"sql\":\"SELECT CDate, PROVINCE, PREM FROM BANK_RECORDS;\"," +
                "\"fields\":[{\"displayName\":\"创建日期\",\"fieldName\":\"CDate\",\"fieldType\":\"Datetime\"}," +
                "{\"displayName\":\"省份\",\"fieldName\":\"PROVINCE\",\"fieldType\":\"Text\"}," +
                "{\"displayName\":\"保额\",\"fieldName\":\"PREM\",\"fieldType\":\"Numeric\"}]," +
                "\"directoryIds\":[\"" + dataSetDirectoryID + "\"]," +
                "\"dataSourceId\":\"" + dataSourceID +"\"," +
                "\"dataSourceType\":\"RDB\",\"name\":\"RDBDataSet\",\"description\":\"\"}";

        Response response =
                given()
                        .contentType("application/json")
                        .body(postData)
                        .when()
                        .post("http://127.0.0.1/mort/data-sets?token=" + token)
                        .then()
                        .log().all()
                        .assertThat()
                        .statusCode(201)
                        .extract().response();

        Headers allHeaders = response.headers();
        dataSetID = allHeaders.get("location").toString().substring(20);
        System.out.println(dataSetID);
    }
}

