package org.hyperskill.projectname

import android.app.Activity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hyperskill.projectname.internals.OrdersMenuUnitTest
import org.junit.Assert.assertEquals
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class Stage2UnitTest : OrdersMenuUnitTest<MainActivity>(MainActivity::class.java){



    @Test
    fun test00_checkTitle() {

        composeTestRule.activityRule.scenario.onActivity { activity : Activity ->
            debugResearchPurposes(activity)


            composeTestRule.apply {

                val titleNode = onNodeWithText(
                    "Orders Menu", substring = false, ignoreCase = false
                )
                titleNode.assertExists("There should exist a title node with text \"Orders Menu\"")
                titleNode.assertIsDisplayed()

                titleNode.assertTextStyle { style ->
                    assertEquals("The title should have size 48sp", 48.sp, style.fontSize)
                }

                titleNode.assertCenter { rootCenter: Float, nodeCenter: Float ->
                    assertEquals("Title should be centered", rootCenter, nodeCenter, 10f)
                }
            }
        }
    }

    @Test
    fun test01_checkFettuccine() {
        composeTestRule.activityRule.scenario.onActivity { activity : Activity ->
            debugResearchPurposes(activity)

            composeTestRule.apply {

                val fettuccineNode = onNodeWithText(
                    "Fettuccine", substring = false, ignoreCase = false
                )
                fettuccineNode.assertExists("There should exist a title node with text \"Fettuccine\"")
                fettuccineNode.assertIsDisplayed()

                fettuccineNode.assertTextStyle { style ->
                    assertEquals("Fettuccine should have size 24sp", 24.sp, style.fontSize)
                }

                fettuccineNode.assertStart { rootStart: Float, nodeStart: Float ->
                    assertEquals(
                        "Fettuccine should be displayed on start",
                        rootStart,
                        nodeStart,
                        10f
                    )
                }
            }
        }
    }
}