package com.pedrocarrillo.spreadsheetandroid.data.repository.sheets

import com.google.api.services.sheets.v4.model.Spreadsheet
import com.pedrocarrillo.spreadsheetandroid.data.model.Person
import com.pedrocarrillo.spreadsheetandroid.data.model.SpreadsheetInfo
import io.reactivex.Observable
import io.reactivex.Single

/**
 * @author Pedro Carrillo.
 */
interface SheetsDataSource {

    fun readSpreadSheet(spreadsheetId : String,
                        spreadsheetRange : String): Single<List<Person>>

    fun createSpreadsheet(spreadSheet : Spreadsheet) : Observable<SpreadsheetInfo>
}