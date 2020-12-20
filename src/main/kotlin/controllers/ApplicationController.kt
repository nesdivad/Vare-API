package h577870.controllers

import h577870.oppgaveserver.OppgaveGenerator
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi

@KtorExperimentalAPI
@ExperimentalSerializationApi
object ApplicationController {

    fun init() {
        EgenskapController.updateEgenskaper()
        OppgaveGenerator.runner()
    }

}