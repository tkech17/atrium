package ch.tutteli.atrium.reporting

import ch.tutteli.atrium.reporting.AtriumError.Companion

/**
 * Indicates that an assertion made by Atrium failed.
 *
 * Its [stackTrace] might be filtered so that reporting does not include all stack frames.
 * This depends on the chosen [AtriumErrorAdjuster] - so theoretically more than the stack trace
 * could be adjusted.
 *
 * To create such an error you need to use the [AtriumError.Companion.create][Companion.create] function.
 */
actual class AtriumError internal actual constructor(message: String) : AssertionError(message, null) {

    // Spek as well as the JUnit Jupiter runner in IntelliJ use message instead of localizedMessage
    // (which is kind of a bug so this might change at any time). With this hack we can prevent that the error message
    // is printed twice in the console
    override val message: String = ""

    override fun getLocalizedMessage(): String? = super.message

    //print first the message and then the qualifiedName + stack (Throwable prints qualifiedName and then message)
    override fun toString(): String = localizedMessage + "\n\n" + this::class.qualifiedName

    actual companion object {
        /**
         * * Creates an [AtriumError] and adjusts it with the given [atriumErrorAdjuster] before it is returned
         * (adjusting might filter the [stackTrace]).
         */
        actual fun create(message: String, atriumErrorAdjuster: AtriumErrorAdjuster): AtriumError =
            createAtriumError(message, atriumErrorAdjuster)
    }
}
