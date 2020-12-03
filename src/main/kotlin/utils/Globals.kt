package h577870.utils

import h577870.dao.BrukerService
import h577870.dao.OppgaveService
import h577870.dao.VareEService
import h577870.dao.VareService
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi

@KtorExperimentalAPI
val vareservice = VareService()
@KtorExperimentalAPI
val brukerservice = BrukerService()
@ExperimentalSerializationApi
@KtorExperimentalAPI
val oppgaveservice = OppgaveService()
val vareeservice = VareEService()
val validator = Validator()