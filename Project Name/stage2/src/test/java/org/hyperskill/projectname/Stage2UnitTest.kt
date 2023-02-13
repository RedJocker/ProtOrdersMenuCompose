package org.hyperskill.projectname

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hyperskill.projectname.internals.AbstractUnitTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Stage2UnitTest : AbstractUnitTest<MainActivity>(MainActivity::class.java){

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun checkTitle() {

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
    fun checkFettuccine() {
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

    fun SemanticsNodeInteraction.assertTextStyle(block: (style: TextStyle) -> Unit ) {
        val semanticsNode = fetchSemanticsNode()
        val config = semanticsNode.config
        //val text = config[SemanticsProperties.Text].first().text
        val list = mutableListOf<TextLayoutResult>()
        config[SemanticsActions.GetTextLayoutResult].action?.invoke(list) // populates list
        block.invoke(list[0].layoutInput.style)
    }

    fun SemanticsNodeInteraction.assertCenter(block: (rootCenter: Float, nodeCenter: Float) -> Unit ) {
        val rootCenter = composeTestRule.onRoot()
            .fetchSemanticsNode()
            .boundsInWindow
            .center
            .x

        val nodeCenter = fetchSemanticsNode()
            .boundsInWindow
            .center
            .x

        block(rootCenter, nodeCenter)
    }

    fun SemanticsNodeInteraction.assertStart(block: (rootStart: Float, nodeStart: Float) -> Unit ) {
        val config = composeTestRule.activity.resources.configuration


        val (rootStart, nodeStart) = if(config.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            val rootStart = composeTestRule.onRoot()
                .fetchSemanticsNode()
                .boundsInWindow
                .right

            val nodeStart = fetchSemanticsNode()
                .boundsInWindow
                .right

            rootStart to nodeStart
        } else {
            val rootStart = composeTestRule.onRoot()
                .fetchSemanticsNode()
                .boundsInWindow
                .left

            val nodeCenter = fetchSemanticsNode()
                .boundsInWindow
                .left

            rootStart to nodeCenter
        }

        block(rootStart, nodeStart)
    }

    //////////////////////

    fun debugResearchPurposes(activity: Activity) {
        val rootView = activity.window.decorView.rootView
        println(rootView.hierarchyString())
        println()
        println(composeTestRule.onRoot().printToString(Int.MAX_VALUE))
    }

    fun View.hierarchyString(tag: String ="| "): String {

        val cur = "$tag ${this::class.java}"
        return if (this is ViewGroup) {
            val childrenString = (0 until this.childCount).map {
                this.getChildAt(it)
            }.fold("") { a, child ->
                val childString = child.hierarchyString("$tag |--")
                "$a\n$childString"
            }
            "$cur$childrenString"
        } else {
            cur
        }
    }
}