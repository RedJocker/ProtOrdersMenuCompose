package org.hyperskill.projectname

import android.app.Activity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hyperskill.projectname.internals.OrdersMenuUnitTest
import org.junit.Assert.assertEquals
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class Stage4UnitTest : OrdersMenuUnitTest<MainActivity>(MainActivity::class.java){


    @Test
    fun test00_checkMenuItemAmountIncreases() {
        composeTestRule.activityRule.scenario.onActivity { activity: Activity ->
            debugResearchPurposes(activity)

            composeTestRule.apply {
                menuRecipesList.forEach { recipeName ->
                    val menuItemNode = onNodeWithText(
                        recipeName, substring = false, ignoreCase = false
                    )
                    val siblings = menuItemNode.onSiblings().filter(isOnSameRowAs(menuItemNode))
                    val plusNode = siblings.filterToOne(hasText("+"))
                        .assertExists("$recipeName should have a sibling with text \"+\"")
                        .assertIsDisplayed()

                    plusNode.performClick()
                    siblings.filterToOne(hasText("1"))
                        .assertExists(
                            "After clicking plus for the first time " +
                                    "the amount of $recipeName ordered should increase to 1"
                        )
                        .assertIsDisplayed()

                    plusNode.performClick()
                    siblings.filterToOne(hasText("2"))
                        .assertExists(
                            "After clicking plus for the second time " +
                                    "the amount of $recipeName ordered should increase to 2"
                        )
                        .assertIsDisplayed()
                }
            }
        }
    }

    @Test
    fun test01_checkMenuItemAmountIncreasesUpToLimitOnly() {
        val limit = 5

        composeTestRule.activityRule.scenario.onActivity { activity: Activity ->
            debugResearchPurposes(activity)

            composeTestRule.apply {
                menuRecipesList.forEach { recipeName ->
                    val menuItemNode = onNodeWithText(
                        recipeName, substring = false, ignoreCase = false,
                    )
                    val siblings = menuItemNode.onSiblings().filter(isOnSameRowAs(menuItemNode))
                    val plusNode = siblings.filterToOne(hasText("+"))
                        .assertExists("$recipeName should have a sibling with text \"+\"")
                        .assertIsDisplayed()

                    (1..10).forEach {
                        plusNode.performClick()
                        val expectAmount = if (it <= limit) it else limit
                        siblings.filterToOne(hasText("$expectAmount"))
                            .assertExists(
                                "After clicking plus the amount of $recipeName ordered should increase " +
                                        "until limit is reached then it should remain"
                            )
                            .assertIsDisplayed()
                    }
                }
            }
        }
    }

    @Test
    fun test02_checkMenuItemWhenOnMaxLimitColorIsRed() {

        composeTestRule.activityRule.scenario.onActivity { activity: Activity ->
            debugResearchPurposes(activity)

            composeTestRule.apply {
                menuRecipesList.forEach { recipeName ->
                    val menuItemNode = onNodeWithText(
                        recipeName, substring = false, ignoreCase = false
                    )

                    menuItemNode.assertTextStyle { style ->
                        assertEquals(
                            "While the amount ordered has not reached the quantity of " +
                                    "$recipeName in stock keep showing $recipeName in black color",
                            Color.Black,
                            style.color
                        )
                    }
                    val siblings = menuItemNode.onSiblings().filter(isOnSameRowAs(menuItemNode))
                    val plusNode = siblings.filterToOne(hasText("+"))
                        .assertExists("$recipeName should have a sibling with text \"+\"")
                        .assertIsDisplayed()

                    (1..10).forEach { _ ->
                        plusNode.performClick()
                    }

                    menuItemNode.assertExists().assertIsDisplayed()
                    menuItemNode.assertTextStyle { style ->
                        assertEquals(
                            "When the amount ordered has reached the quantity of $recipeName in stock " +
                                    "show $recipeName in red color",
                            Color.Red,
                            style.color
                        )
                    }
                }
            }
        }
    }

    @Test
    fun test03_checkMenuItemAmountDecrease() {

        composeTestRule.activityRule.scenario.onActivity { activity: Activity ->
            debugResearchPurposes(activity)

            composeTestRule.apply {
                menuRecipesList.forEach { recipeName ->

                    val menuItemNode = onNodeWithText(
                        recipeName, substring = false, ignoreCase = false
                    )

                    val siblings = menuItemNode.onSiblings().filter(isOnSameRowAs(menuItemNode))
                    val plusNode = siblings.filterToOne(hasText("+"))
                        .assertExists("$recipeName should have a sibling with text \"+\"")
                        .assertIsDisplayed()

                    (1..10).forEach { _ ->
                        plusNode.performClick()
                    }

                    menuItemNode.assertExists()
                    menuItemNode.assertIsDisplayed()
                    siblings.filterToOne(hasText("5"))
                        .assertExists(
                            "After clicking plus more than the limit times the amount " +
                                    "of $recipeName ordered should be equal to the limit"
                        )
                        .assertIsDisplayed()

                    val minusNode = siblings.filterToOne(hasText("-"))
                        .assertExists("$recipeName should have a sibling with text \"-\"")
                        .assertIsDisplayed()


                    minusNode.performClick()
                    siblings.filterToOne(hasText("4"))
                        .assertExists(
                            "While the amount ordered is 5, after clicking minus " +
                                    "the amount of $recipeName ordered should decrease to 4"
                        )
                        .assertIsDisplayed()

                    minusNode.performClick()
                    siblings.filterToOne(hasText("3"))
                        .assertExists(
                            "While the amount ordered is 4, after clicking minus " +
                                    "the amount of $recipeName ordered should decrease to 3"
                        )
                        .assertIsDisplayed()
                }
            }
        }
    }

    @Test
    fun test04_checkAmountDecreaseDoesNotGoBellowZero() {

        composeTestRule.activityRule.scenario.onActivity { activity: Activity ->
            debugResearchPurposes(activity)

            composeTestRule.apply {
                menuRecipesList.forEach { recipeName ->

                    val menuItemNode = onNodeWithText(
                        recipeName, substring = false, ignoreCase = false
                    )

                    val siblings = menuItemNode.onSiblings().filter(isOnSameRowAs(menuItemNode))
                    val minusNode = siblings.filterToOne(hasText("-"))
                        .assertExists("$recipeName should have a sibling with text \"-\"")
                        .assertIsDisplayed()

                    siblings.filterToOne(hasText("0"))
                        .assertExists("The initial amount of $recipeName ordered should be zero")
                        .assertIsDisplayed()

                    minusNode.performClick()
                    siblings.filterToOne(hasText("0"))
                        .assertExists(
                            "When the $recipeName ordered amount is zero " +
                                    "and minus is clicked the amount should remain zero"
                        )
                        .assertIsDisplayed()

                    val plusNode = siblings.filterToOne(hasText("+"))
                        .assertExists("$recipeName should have a sibling with text \"+\"")
                        .assertIsDisplayed()

                    (1..10).forEach { _ ->
                        plusNode.performClick()
                    }

                    menuItemNode.assertExists()
                    menuItemNode.assertIsDisplayed()
                    siblings.filterToOne(hasText("5"))
                        .assertExists(
                            "After clicking plus more than the limit times the amount " +
                                    "of $recipeName ordered should be equal to the limit"
                        )
                        .assertIsDisplayed()

                    (1..10).map { 5 - it }.forEach { i ->
                        val expectAmount = if (i > 0) i else 0
                        minusNode.performClick()
                        siblings.filterToOne(hasText("$expectAmount"))
                            .assertExists(
                                "After clicking minus the amount of $recipeName ordered should decrease " +
                                        "until zero is reached then it should remain"
                            )
                            .assertIsDisplayed()
                    }
                }
            }
        }
    }

    @Test
    fun test05_whileColorIsRedCheckColorChangesBackToBlackAfterDecrement() {

        composeTestRule.activityRule.scenario.onActivity { activity: Activity ->
            debugResearchPurposes(activity)

            composeTestRule.apply {
                menuRecipesList.forEach { recipeName ->

                    val menuItemNode = onNodeWithText(
                        recipeName, substring = false, ignoreCase = false
                    )

                    menuItemNode.assertTextStyle { style ->
                        assertEquals(
                            "While the amount ordered has not reached the quantity of " +
                                    "$recipeName in stock keep showing $recipeName in black color",
                            Color.Black,
                            style.color
                        )
                    }
                    val siblings = menuItemNode.onSiblings().filter(isOnSameRowAs(menuItemNode))

                    val plusNode = siblings.filterToOne(hasText("+"))
                        .assertExists("$recipeName should have a sibling with text \"+\"")
                        .assertIsDisplayed()

                    (1..10).forEach { _ ->
                        plusNode.performClick()
                    }

                    menuItemNode.assertExists().assertIsDisplayed()
                    menuItemNode.assertTextStyle { style ->
                        assertEquals(
                            "When the amount ordered has reached the quantity of $recipeName in stock " +
                                    "show $recipeName in red color",
                            Color.Red,
                            style.color
                        )
                    }

                    val minusNode = siblings.filterToOne(hasText("-"))
                        .assertExists("$recipeName should have a sibling with text \"-\"")
                        .assertIsDisplayed()

                    minusNode.performClick()

                    menuItemNode.assertExists().assertIsDisplayed()
                    menuItemNode.assertTextStyle { style ->
                        assertEquals(
                            "When $recipeName color is red and minus is clicked " +
                                    "then $recipeName color should become black",
                            Color.Black,
                            style.color
                        )
                    }
                }
            }
        }
    }
}