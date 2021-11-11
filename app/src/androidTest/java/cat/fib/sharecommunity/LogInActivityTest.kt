package cat.fib.sharecommunity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LogInActivityTest {

    @Rule
    @JvmField
    val rule: ActivityTestRule<AuthenticationProviders> = ActivityTestRule(AuthenticationProviders::class.java)

    @Test
    fun user_can_enter_email() {
        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText("adria@gmail.com"))
    }

    @Test
    fun user_can_enter_password() {
        onView(withId(R.id.editTextTextPassword)).perform(typeText("123456"))
    }

    @Test
    fun user_can_enter_email_password() {
        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText("adria@mail.com"))
        onView(withId(R.id.editTextTextPassword)).perform(typeText("123456"))
    }

    @Test
    fun user_enter_email_password_check_format_correct() {
        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText("adria@mail.com"))
        onView(withId(R.id.editTextTextPassword)).perform(typeText("123456"))
        onView(withId(R.id.signInButton)).perform(click())
    }

    @Test
    fun test_incorrect_format_email() {
        val EXPECTED_ERROR = "El format del Email és incorrecte"
        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText("adria"))
        onView(withId(R.id.editTextTextPassword)).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.signInButton)).perform(ViewActions.click())
    }


}