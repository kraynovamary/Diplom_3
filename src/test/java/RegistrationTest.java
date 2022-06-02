import PageObject.HomePage;
import PageObject.LoginPage;
import PageObject.ProfilePage;
import PageObject.RegisterPage;
import com.UserOperations;
import com.model.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.codeborne.selenide.Selenide.page;

public class RegistrationTest extends MainTest{

    LoginPage loginPage = page(LoginPage.class);

    @Before
    public void init() {
        super.init();
        homePage.clickToProfileLink();
    }

    @Test
    @DisplayName("Registration correct test")
    @Description("Basic success test")
    public void createNewUserRegistrationTest(){

        loginPage.clickToRegistrationLink();
        User newUser = UserOperations.getRandomDataForUser(10);
        RegisterPage registerPage = page(RegisterPage.class);
        registerPage.setRegistrationUser(newUser);
        Assert.assertTrue("Ожидаем переход на страницу Входа", loginPage.checkVisibleEnterText());

        loginPage.setLoginAndEnter(newUser);
        HomePage homePage = page(HomePage.class);
        Assert.assertTrue("Кнопка Оформить заказ не отображается", homePage.isVisibleOrderButton());
        homePage.clickToProfileLink();

        ProfilePage profilePage = page(ProfilePage.class);
        User actualUser = profilePage.getUserAuthorization();
        Assert.assertEquals("Не верное имя", newUser.getName(), actualUser.getName());
        Assert.assertEquals("Не верный email", newUser.getEmail().toLowerCase(), actualUser.getEmail());
    }

    @Test
    @DisplayName("Registration not correct password test")
    @Description("Basic password test")
    public void checkNotCorrectPasswordRegistrationTest(){

        loginPage.clickToRegistrationLink();
        User newUser = UserOperations.getRandomDataForUser(5);
        RegisterPage registerPage = page(RegisterPage.class);
        registerPage.setRegistrationUser(newUser);
        Assert.assertTrue("Ожидаем ошибку Некорректный пароль", registerPage.isVisibleErrorPasswordText());
    }
}
