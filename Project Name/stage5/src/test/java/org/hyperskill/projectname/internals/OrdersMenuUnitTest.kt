package org.hyperskill.projectname.internals

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import org.hyperskill.projectname.MainActivity
import org.junit.Rule
import kotlin.math.abs

open class OrdersMenuUnitTest<T : Activity>(clazz: Class<T>): AbstractUnitTest<T>(clazz) {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val recipesOnMenuToInitialStockMap = mapOf(
        "Fettuccine" to 5,
        "Risotto" to 6,
        "Gnocchi" to 4,
        "Spaghetti" to 3,
        "Lasagna" to 5,
        "Steak Parmigiana" to 2
    )

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

            val nodeStart = fetchSemanticsNode()
                .boundsInWindow
                .left

            rootStart to nodeStart
        }

        block(rootStart, nodeStart)
    }

    fun isOnSameRowAs(fettuccineNode: SemanticsNodeInteraction): SemanticsMatcher {
        return SemanticsMatcher(
            "is on same row as $fettuccineNode"
        ) { node ->
            val otherNodeYPosition = fettuccineNode.fetchSemanticsNode().positionInWindow.y
            val nodeYPosition = node.positionInWindow.y
            abs(otherNodeYPosition - nodeYPosition) < 10f
        }
    }

    //////////////////////

    fun debugResearchPurposes(activity: Activity) {
        val rootView = activity.window.decorView.rootView
        println(rootView.hierarchyString())
        println()
        println(composeTestRule.onRoot(useUnmergedTree = true).printToString(Int.MAX_VALUE))
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