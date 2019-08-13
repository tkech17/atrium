package ch.tutteli.atrium.specs.creating

import ch.tutteli.atrium.api.fluent.en_GB.containsExactly
import ch.tutteli.atrium.api.fluent.en_GB.feature
import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.assertions.Assertion
import ch.tutteli.atrium.core.Option
import ch.tutteli.atrium.core.Some
import ch.tutteli.atrium.creating.CollectingAssertionContainer
import ch.tutteli.atrium.specs.describeFunTemplate
import ch.tutteli.atrium.specs.verbs.AssertionVerbFactory
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.Suite

abstract class CollectingAssertionContainerSpec(
    verbs: AssertionVerbFactory,
    testeeFactory: (Option<Int>) -> CollectingAssertionContainer<Int>,
    describePrefix: String = "[Atrium] "
) : Spek({

    fun describeFun(vararg funName: String, body: Suite.() -> Unit) =
        describeFunTemplate(describePrefix, funName, body = body)

    describeFun(CollectingAssertionContainer<Int>::getAssertions.name) {
        val assertion: Assertion by memoized {
            object : Assertion {
                override fun holds() = true
            }
        }
        val testee by memoized { testeeFactory(Some(1)) }

        context("no assertion has been added so far") {
            it("returns an empty list") {
                verbs.check(testee.getAssertions()).toBe(listOf())
            }

            context("an assertion is added") {
                beforeEachTest {
                    testee.addAssertion(assertion)
                }
                it("returns the assertion") {
                    verbs.check(testee.getAssertions()).toBe(listOf(assertion))
                }

                it("returns the assertion when calling ${testee::getAssertions.name} a second time") {
                    verbs.check(testee.getAssertions()).toBe(listOf(assertion))
                }
            }
        }
    }

    describeFun(CollectingAssertionContainer<Int>::addAssertionsCreatedBy.name) {
        it("adds a failing assertion in case an empty assertionContainer lambda is passed") {
            val testee = testeeFactory(Some(1))
            testee.addAssertionsCreatedBy { }
            verbs.check(testee.getAssertions()).containsExactly {
                feature { f(it::holds) }.toBe(false)
            }
        }
    }
})
