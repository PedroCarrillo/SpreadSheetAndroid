package com.pedrocarrillo.spreadsheetandroid.data.repository

import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.pedrocarrillo.spreadsheetandroid.ui.base.AuthenticationManager
import io.reactivex.Observable

/**
* @author Pedro Carrillo.
*/
class SheetsAPIDataSource(val authManager : AuthenticationManager,
                          val transport : HttpTransport,
                          val jsonFactory: JsonFactory) : SheetsDataSource {


    private val sheetsAPI : Sheets
        get() {
            return Sheets.Builder(transport,
                    jsonFactory,
                    authManager.googleAccountCredential)
                    .setApplicationName("test")
                    .build()
        }

    override fun readSpreadSheet(spreadsheetId: String,
                                 spreadsheetRange: String): Observable<MutableList<MutableList<Any>>> {
        return Observable
                .fromCallable{
                    val response = sheetsAPI.spreadsheets().values()
                            .get(spreadsheetId, spreadsheetRange)
                            .execute()
                    response.getValues() }
    }
}