package org.hyperskill.projectname

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToString
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hyperskill.projectname.internals.AbstractUnitTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Stage1UnitTest : AbstractUnitTest<MainActivity>(MainActivity::class.java){

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun exampleTest() {

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
        println(rootView.hierarchyString())
        println()
        println(composeTestRule.onRoot().printToString(Int.MAX_VALUE))
    }

    fun View.hierarchyString(tag: String ="| ", acc: String = ""): String {
        return if (this is ViewGroup) {
//            var cur = "$acc"
//            for (i in 0 until this.childCount) {
//                val child = this.getChildAt(i)
//                cur = "$cur \n$tag ${child::class.java}"
//                val childrenString = child.hierarchyString("$tag |--", "")
//                cur = "$cur$childrenString"
//            }
//            cur
            (0 until this.childCount).map {
                this.getChildAt(it)
            }.fold(acc) { a, child ->
                val cur = "$a \n$tag ${child::class.java}"
                val childrenString = child.hierarchyString("$tag |--", "")
                "$cur$childrenString"
            }
        } else {
            acc
        }
    }
}