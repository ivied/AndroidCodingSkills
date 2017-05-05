package com.liftday.android.espresso;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.liftday.android.R;
import com.liftday.android.activities.AcBaseContent;
import com.liftday.android.activities.dialogs.WheelDialog;
import com.liftday.android.activities.fragments.routinedetailsmade.RoutineDetailsFragment;
import com.liftday.android.constants.Constants;
import com.liftday.android.controls.Prefs;
import com.liftday.android.utils.timerbar.LiftDayTimerBar;
import com.liftdayapp.liftday.core.utils.UnitType;

import org.hamcrest.Matchers;

import kankan.wheel.widget.WheelView;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

@RunWith(MockitoJUnitRunner.class)
public class PlayPauseTest extends ActivityInstrumentationTestCase2<AcBaseContent> {

    public static final String DEV_TEST_ROUTINE_NAME = "1Eugene Simple Calculations Test 1 (Don't delete or modify)";
    public static final String DEV_TEST_SUPERSET_ROUTINE_NAME = "1Superset (don't delete or modify)";

    @Mock NetworkAPI network;
    @Mock ArticleDatabase database;

    @InjectMocks private ExerciseManager manager; 

    public PlayPauseTest() {
        super(AcBaseContent.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }


    @LargeTest
    public void testNextSetExerciseNameInSuperSet() {
        onView(withText(R.string.fs_ready_to_do)).perform(click());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);
        onView(withText("Arms 3")).perform(click());
        ((RoutineDetailsFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent)).doItNow();
        onView(withId(R.id.swipe)).perform(swipeRight());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);
        onView(withId(R.id.llRest)).perform(click());

        onView(withText("Do Exercise 2")).perform(click());
        onView(withId(R.id.swipe)).perform(swipeRight());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);

                onView(withId(R.id.llRest)).perform(click());

        for (int i = 0; i < 2; i++) {
            onView(Matchers.allOf(withText("Do It"), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
            getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);
            onView(withId(R.id.llRest)).perform(click());
        }

        onView(Matchers.allOf(withId(R.id.next_exercise), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).check(ViewAssertions.matches(withText("Standing Curl")));
    }*/

    @LargeTest
    public void testCheckWeightForExtraset() {
        onView(withText(R.string.fs_ready_to_do)).perform(click());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);
        onView(withText(DEV_TEST_ROUTINE_NAME)).perform(click());
        ((RoutineDetailsFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent)).doItNow();
        onView(withId(R.id.swipe)).perform(swipeRight());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(500);
        onView(withId(R.id.llRest)).perform(click());

        for (int i = 2; i < 6; i++) {
            onView(Matchers.allOf(withText("SET " + i), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
            getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(500);
            onView(withId(R.id.llRest)).perform(click());
        }

        onData(anything()).inAdapterView(allOf(withId(R.id.lvSets))).atPosition(4).onChildView(withId(R.id.etWeight)).check(ViewAssertions.matches(withText(Prefs.getInstance().getMeasure() == UnitType.IMPERIAL? "175":"79.4")));
    }

    @LargeTest
    public void testSkipAndCancelNextExercise() { // LDA-437
        onView(withText(R.string.fs_ready_to_do)).perform(click());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);
        onView(withText(DEV_TEST_ROUTINE_NAME)).perform(click());
        ((RoutineDetailsFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent)).doItNow();
        onView(withId(R.id.swipe)).perform(swipeRight());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);
        onView(withId(R.id.llRest)).perform(click());

        wait500();

        onView(withId(R.id.llDoExercise)).perform(click());

        onView(withId(R.id.ivSkip)).perform(click());

        onView(withId(R.id.ivClose)).perform(click());

        onView(withId(R.id.llDoExercise)).perform(click());

        onView(withId(R.id.tvNumberOfExercise)).check(ViewAssertions.matches(withText("Exercise 2 of 3")));
    }

    @LargeTest
    public void testRestTimeInSuperset() { // LDA-265
        onView(withText(R.string.fs_ready_to_do)).perform(click());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);
        onView(withText(DEV_TEST_SUPERSET_ROUTINE_NAME)).perform(click());
        ((RoutineDetailsFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent)).doItNow();
        onView(withId(R.id.swipe)).perform(swipeRight());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);
        onView(withId(R.id.llRest)).perform(click());
        assertEquals(15, ((LiftDayTimerBar) getActivity().getSupportFragmentManager().findFragmentByTag("restdialog").getView().findViewById(R.id.rest_timer_bar)).getRestTime());//mid exercise

        onView(Matchers.allOf(withText("DO IT"), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(500);
        onView(withId(R.id.llRest)).perform(click());

        assertEquals(15, ((LiftDayTimerBar)getActivity().getSupportFragmentManager().findFragmentByTag("restdialog").getView().findViewById(R.id.rest_timer_bar)).getRestTime());//mid exercise

        onView(Matchers.allOf(withText("DO IT"), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(500);
        onView(withId(R.id.llRest)).perform(click());

        assertEquals(50, ((LiftDayTimerBar)getActivity().getSupportFragmentManager().findFragmentByTag("restdialog").getView().findViewById(R.id.rest_timer_bar)).getRestTime()); //last exercise in superset

    }

    @LargeTest
    public void testAutoScroll() { //LDA-327
        onView(withText(R.string.fs_ready_to_do)).perform(click());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);
        onView(withText(DEV_TEST_ROUTINE_NAME)).perform(click());
        ((RoutineDetailsFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent)).doItNow();
        onView(withId(R.id.swipe)).perform(swipeRight());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(500);
        onView(withId(R.id.llRest)).perform(click());

        for (int i = 2; i < 13; i++) {
            onView(Matchers.allOf(withText("SET " + i), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
            getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(500);
            onView(withId(R.id.llRest)).perform(click());
        }

        onData(anything()).inAdapterView(allOf(withId(R.id.lvSets))).atPosition(12).onChildView(withId(R.id.tvNumberOfSetSecond)).check(ViewAssertions.matches(withText("SET 13")));
    }

    @LargeTest
    public void testCompletedSetCountInSuperset() { //LDA-394

        onView(withText(R.string.fs_ready_to_do)).perform(click());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);
        onView(withText(PlayPauseTest.DEV_TEST_SUPERSET_ROUTINE_NAME)).perform(click());
        ((RoutineDetailsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent)).doItNow();
        onView(withId(R.id.swipe)).perform(swipeRight());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);
        onView(withId(R.id.llRest)).perform(click());

        onView(Matchers.allOf(withText("DO IT"), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
        wait500();
        onView(withId(R.id.llRest)).perform(click());

        onView(Matchers.allOf(withText("DO IT"), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
        wait500();
        onView(withId(R.id.llRest)).perform(click());

        onView(Matchers.allOf(withText("SET 2"), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
        wait500();
        onView(withId(R.id.llRest)).perform(click());

        onView(withText("End")).perform(click());
        wait500();
        onView(withText("OK")).perform(click());

        wait500();

        // 3 exercise / 2 sets / 43 reps
        onView(withId(R.id.summary_ex_sets_reps)).check(ViewAssertions.matches(withText("3 / 4 / 43")));
    }

    @LargeTest
    public void testRepeatLogWithOneExercise() { //LDA-442

        onView(withText(R.string.fs_ready_to_do)).perform(click());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);
        onView(withText(PlayPauseTest.DEV_TEST_ROUTINE_NAME)).perform(click());
        ((RoutineDetailsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent)).doItNow();
        onView(withId(R.id.swipe)).perform(swipeRight());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(1000);
        onView(withId(R.id.llRest)).perform(click());

        onView(withText("End")).perform(click());
        wait500();
        onView(withText("OK")).perform(click());

        wait500();
        onView(withId(R.id.workout_summary_confirm_button)).perform(click());
        wait5000();
        onData(anything()).inAdapterView(allOf(withId(R.id.lvLiftDays))).atPosition(0).perform(click());
        wait500();

        onView(withId(R.id.buttonFloat)).perform(click());
        onView(withId(R.id.swipe)).perform(swipeRight());
        wait500();
        onView(withId(R.id.llRest)).perform(click());
        onData(anything()).inAdapterView(allOf(withId(R.id.lvSets))).atPosition(1).onChildView(withId(R.id.tvNumberOfSetSecond)).check(ViewAssertions.matches(withText("SET 2")));
    }

    WheelDialog wd;
    WheelView wheel;
    @LargeTest
    public void testChangeWeightAfterSetComplete() { //LDA-520 for simple exercise

        onView(withText(R.string.fs_ready_to_do)).perform(click());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);
        onView(withText(PlayPauseTest.DEV_TEST_ROUTINE_NAME)).perform(click());
        ((RoutineDetailsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent)).doItNow();
        onView(withId(R.id.swipe)).perform(swipeRight());
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(1000);
        onView(withId(R.id.llRest)).perform(click());

        onData(anything()).inAdapterView(allOf(withId(R.id.lvSets))).atPosition(1).onChildView(withText("15")).perform(click());
        wait500();
        wd = (WheelDialog) getActivity().getSupportFragmentManager().findFragmentByTag(Constants.Arguments.REPS);
        wait500();
        wheel = (WheelView)wd.getView().findViewById(R.id.wheel);
        wheel.post(new Runnable() {
            @Override
            public void run() {
                wheel.setCurrentItem(24); //25 reps
                wd.selectItem();
            }
        });
        wait500();


        onView(Matchers.allOf(withText("SET 2"), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
        wait500();
        onView(withId(R.id.llRest)).perform(click());

        onData(anything()).inAdapterView(allOf(withId(R.id.lvSets))).atPosition(2).onChildView(withText("25")).perform(click());
        wait500();
        wd = (WheelDialog) getActivity().getSupportFragmentManager().findFragmentByTag(Constants.Arguments.REPS);
        wait500();
        wheel = (WheelView)wd.getView().findViewById(R.id.wheel);
        wheel.post(new Runnable() {
            @Override
            public void run() {
                wheel.setCurrentItem(34); //35 reps
                wd.selectItem();
            }
        });
        wait500();



        onView(Matchers.allOf(withText("SET 3"), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
        wait500();
        onView(withId(R.id.llRest)).perform(click());

        onData(anything()).inAdapterView(allOf(withId(R.id.lvSets))).atPosition(3).onChildView(withText("35")).perform(click());
        wait500();
        wd = (WheelDialog) getActivity().getSupportFragmentManager().findFragmentByTag(Constants.Arguments.REPS);
        wait500();
        wheel = (WheelView)wd.getView().findViewById(R.id.wheel);
        wheel.post(new Runnable() {
            @Override
            public void run() {
                wheel.setCurrentItem(44); //45 reps
                wd.selectItem();
            }
        });
        wait500();

        onView(withText("End")).perform(click());
        wait500();
        onView(withText("OK")).perform(click());

        wait500();
        onView(withId(R.id.workout_summary_confirm_button)).perform(click());
        wait5000();
        onData(anything()).inAdapterView(allOf(withId(R.id.lvLiftDays))).atPosition(0).perform(click());
        wait500();

        // 1 exercise / 3 sets / 105 reps
        onView(withId(R.id.liftday_log_exc_sets_reps_value)).check(ViewAssertions.matches(withText("1 / 3 / 105")));

    }

    private void wait500() {
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(500);
    }

    private void wait5000() {
        getInstrumentation().addMonitor(AcBaseContent.class.getName(), null, false).waitForActivityWithTimeout(5000);
    }

}
