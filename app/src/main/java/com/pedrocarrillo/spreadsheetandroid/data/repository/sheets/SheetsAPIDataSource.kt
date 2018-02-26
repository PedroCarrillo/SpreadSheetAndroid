package com.pedrocarrillo.spreadsheetandroid.data.repository.sheets

import android.util.Log
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import com.pedrocarrillo.spreadsheetandroid.data.manager.AuthenticationManager
import io.reactivex.Completable
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

    override fun createSpreadsheet(spreadSheet : Spreadsheet) : Observable<MutableCollection<Any>> {
        return Observable
                .fromCallable{
                    val response =
                            sheetsAPI
                                    .spreadsheets()
                                    .create(spreadSheet)
                                    .execute()
                    response.values
                }
    }
}