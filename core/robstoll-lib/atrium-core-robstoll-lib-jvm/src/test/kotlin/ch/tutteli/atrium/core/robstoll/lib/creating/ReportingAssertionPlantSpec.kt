package ch.tutteli.atrium.core.robstoll.lib.creating

import ch.tutteli.atrium.verbs.internal.AssertionVerbFactory

//TODO remove with 1.0.0 - no need to migrate to Spek2
object ReportingAssertionPlantSpec : ch.tutteli.atrium.spec.creating.ReportingAssertionPlantSpec(
    AssertionVerbFactory, ::ReportingAssertionPlantImpl
)
