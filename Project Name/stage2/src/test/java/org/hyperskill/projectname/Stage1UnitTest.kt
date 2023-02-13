package org.hyperskill.projectname

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.core.Is
import org.hyperskill.projectname.internals.AbstractUnitTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Stage1UnitTest : AbstractUnitTest<MainActivity>(MainActivity::class.java){

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun checkTitle() {

        composeTestRule.activityRule.scenario.onActivity { activity : Activity ->
            debugResearchPurposes(activity)


            // actual test
            composeTestRule.apply {
                val titleNode = onNodeWithText(
                    "Orders Menu", substring = false, ignoreCase = false
                )
                titleNode.assertExists("There should exist a title node with text \"Orders Menu\"")
                titleNode.assertIsDisplayed()
            }
        }
    }


    fun debugResearchPurposes(activity: Activity) {
        val rootView = activity.window.decorView.rootView
        println()
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