package com.pedrocarrillo.spreadsheetandroid.ui.read

import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.sheets.v4.Sheets

/**
 * @author Pedro Carrillo
 */
class SheetsAPIManager(val authManager : AuthenticationManager,
                       val transport : HttpTransport,
                       val jsonFactory: JsonFactory) {

    val sheetsAPI : Sheets
        get() {
            return Sheets.Builder(transport,
                            jsonFactory,
                            authManager.googleAccountCredential)
                    .setApplicationName("t")
                    .build()
        }

}