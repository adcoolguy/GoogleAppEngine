package io.swagger.sample.servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.sample.model.SampleData;
import io.swagger.util.Json;

@io.swagger.annotations.Api(value = "/sample/users", description = "gets some data from a servlet")
public class SampleServlet extends HttpServlet {

    @ApiOperation(httpMethod = "GET", value = "Resource to get a user", response = SampleData.class, nickname = "getUser")
    @ApiResponses({@ApiResponse(code = 400, message = "Invalid input", response = io.swagger.sample.model.ApiResponse
            .class)})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "User ID", required = true, dataType = "integer", paramType =
                    "query"),
            @ApiImplicitParam(name = "name", value = "User's name", required = true, dataType = "string", paramType =
                    "query"),
            @ApiImplicitParam(name = "email", value = "User's email", required = true, dataType = "string", paramType
                    = "query"),
            @ApiImplicitParam(name = "age", value = "User's age", required = true, dataType = "integer", paramType =
                    "query"),
            @ApiImplicitParam(name = "dateOfBirth", value = "User's date of birth, in dd-MM-yyyy format", required =
                    true, dataType = "java.util.Date", paramType = "query")})
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String result;
        try {
            final Integer id = Integer.parseInt(request.getParameter("id"));
            final String name = request.getParameter("name");
            final String email = request.getParameter("email");
            final Integer age = Integer.parseInt(request.getParameter("age"));
            final Date dateOfBirth = new SimpleDateFormat("dd-MM-yyyy").parse(request.getParameter("dateOfBirth"));
            result = Json.pretty(new SampleData(id, name, email, age, dateOfBirth));
        } catch (Exception ex) {
            result = Json.pretty(new io.swagger.sample.model.ApiResponse(400, ex.getMessage()));
        }

        response.getOutputStream().write(result.getBytes(StandardCharsets.UTF_8));
    }
}
