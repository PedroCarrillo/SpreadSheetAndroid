package com.pedrocarrillo.spreadsheetandroid.ui.read

import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.sheets.v4.Sheets
import io.reactivex.Observable

/**
 * @author Pedro Carrillo
 */
class SheetsAPIManager(val authManager : AuthenticationManager,
                       val transport : HttpTransport,
                       val jsonFactory: JsonFactory) {

    private val sheetsAPI : Sheets
        get() {
            return Sheets.Builder(transport,
                            jsonFactory,
                            authManager.googleAccountCredential)
                    .setApplicationName("t")
                    .build()
        }

    fun readSpreadSheet(spreadsheetId : String, spreadsheetRange : String) : Observable<MutableList<MutableList<Any>>> {
        return Observable
                .fromCallable{
                    val response = sheetsAPI.spreadsheets().values()
                            .get(spreadsheetId, spreadsheetRange)
                            .execute()
                    response.getValues() }
    }

}