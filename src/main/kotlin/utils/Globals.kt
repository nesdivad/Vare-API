package h577870.utils

import h577870.dao.BrukerService
import h577870.dao.VareService
import io.ktor.util.*

@KtorExperimentalAPI
val vareservice = VareService()
@KtorExperimentalAPI
val brukerservice = BrukerService()

val validator = Validator()