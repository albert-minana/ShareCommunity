package cat.fib.sharecommunity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class PreferencesActivityTest {

    @Rule
    @JvmField
    val rule: ActivityTestRule<PreferencesActivity> = ActivityTestRule(PreferencesActivity::class.java)

    @Test
    fun user_can_enter_principiant_experience() {
        onView(withId(R.id.radioButtonPrincipiant)).perform(click())
        onView(withId(R.id.radioButtonPrincipiant)).check(matches(isChecked()))
        onView(withId(R.id.radioButtonIntermedi)).check(matches(not(isChecked())))
        onView(withId(R.id.radioButtonAvançat)).check(matches(not(isChecked())))
    }

    @Test
    fun user_can_enter_intermedi_experience() {
        onView(withId(R.id.radioButtonIntermedi)).perform(click())
        onView(withId(R.id.radioButtonIntermedi)).check(matches(isChecked()))
        onView(withId(R.id.radioButtonPrincipiant)).check(matches(not(isChecked())))
        onView(withId(R.id.radioButtonAvançat)).check(matches(not(isChecked())))
    }

    @Test
    fun user_can_enter_avançat_experience() {
        onView(withId(R.id.radioButtonAvançat)).perform(click())
        onView(withId(R.id.radioButtonAvançat)).check(matches(isChecked()))
        onView(withId(R.id.radioButtonPrincipiant)).check(matches(not(isChecked())))
        onView(withId(R.id.radioButtonIntermedi)).check(matches(not(isChecked())))
    }

    @Test
    fun user_can_check_objectives() {
        onView(withId(R.id.checkBoxSalut)).perform(click())
        onView(withId(R.id.checkBoxForça2)).perform(click())
        onView(withId(R.id.checkBoxPerduaPes)).perform(click())
        onView(withId(R.id.checkBoxSalut)).check(matches(isChecked()))
        onView(withId(R.id.checkBoxForça2)).check(matches(isChecked()))
        onView(withId(R.id.checkBoxPerduaPes)).check(matches(isChecked()))
        onView(withId(R.id.checkBoxFlexibilitat)).check(matches(not(isChecked())))
        onView(withId(R.id.checkBoxResistència)).check(matches(not(isChecked())))
        onView(withId(R.id.checkBoxRecuperació)).check(matches(not(isChecked())))
        onView(withId(R.id.checkBoxAgilitat)).check(matches(not(isChecked())))
    }

    @Test
    fun user_can_check_categories() {
        onView(withId(R.id.checkBoxForça)).perform(click())
        onView(withId(R.id.checkBoxCardio)).perform(click())
        onView(withId(R.id.checkBoxIoga)).perform(click())
        onView(withId(R.id.checkBoxForça)).check(matches(isChecked()))
        onView(withId(R.id.checkBoxCardio)).check(matches(isChecked()))
        onView(withId(R.id.checkBoxIoga)).check(matches(isChecked()))
        onView(withId(R.id.checkBoxEstiraments)).check(matches(not(isChecked())))
        onView(withId(R.id.checkBoxRehabilitació)).check(matches(not(isChecked())))
        onView(withId(R.id.checkBoxPilates)).check(matches(not(isChecked())))
    }

    @Test
    fun send_error_experience(){
        user_can_check_categories()
        user_can_check_objectives()
        onView(withId(R.id.buttonEnviar)).perform(ViewActions.click())
    }

    @Test
    fun send_okey(){
        user_can_enter_avançat_experience()
        user_can_check_categories()
        user_can_check_objectives()
        onView(withId(R.id.buttonEnviar)).perform(ViewActions.click())
    }

    @Test
    fun send_incorrect_number_objectives(){
        user_can_enter_intermedi_experience()
        onView(withId(R.id.checkBoxSalut)).perform(click())
        onView(withId(R.id.checkBoxForça2)).perform(click())
        onView(withId(R.id.checkBoxPerduaPes)).perform(click())
        onView(withId(R.id.checkBoxFlexibilitat)).perform(click())
        user_can_check_categories()
        onView(withId(R.id.buttonEnviar)).perform(ViewActions.click())
    }

    @Test
    fun send_incorrect_number_categories(){
        user_can_enter_principiant_experience()
        onView(withId(R.id.checkBoxForça)).perform(click())
        onView(withId(R.id.checkBoxCardio)).perform(click())
        onView(withId(R.id.checkBoxIoga)).perform(click())
        onView(withId(R.id.checkBoxEstiraments)).perform(click())
        user_can_check_objectives()
        onView(withId(R.id.buttonEnviar)).perform(ViewActions.click())
    }


}