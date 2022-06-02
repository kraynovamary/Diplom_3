package com;

import com.model.Tokens;
import com.model.User;
import com.model.UserRegisterResponse;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserOperations {
    public static final String EMAIL_POSTFIX = "@yandex.ru";

    public static User getRandomDataForUser(int passwordLength) {
        String email = RandomStringUtils.randomAlphabetic(10) + EMAIL_POSTFIX;
        String password = RandomStringUtils.randomAlphabetic(passwordLength);
        String name = RandomStringUtils.randomAlphabetic(10);

        return new User(email, password, name);
    }

    public User register() {

        User newUser = getRandomDataForUser(10);

        UserRegisterResponse response = given()
                .spec(Base.getBaseSpec())
                .and()
                .body(newUser)
                .when()
                .post("auth/register")
                .body()
                .as(UserRegisterResponse.class);

        Map<String, String> responseData = new HashMap<>();
        if (response != null) {
            responseData.put("email", response.getUser().getEmail());
            responseData.put("name", response.getUser().getName());
            responseData.put("password", newUser.getPassword());

            Tokens.setAccessToken(response.getAccessToken().substring(7));
            Tokens.setRefreshToken(response.getRefreshToken());

            return newUser;
        }
        return null;
    }

    public void delete() {
        if (Tokens.getAccessToken() == null) {
            return;
        }
        given()
                .spec(Base.getBaseSpec())
                .auth().oauth2(Tokens.getAccessToken())
                .when()
                .delete("auth/user")
                .then()
                .statusCode(202);
    }
}
