package com.pedrocarrillo.spreadsheetandroid.data.repository.sheets

import com.google.api.services.sheets.v4.model.Spreadsheet
import io.reactivex.Observable

/**
 * @author Pedro Carrillo.
 */
interface SheetsDataSource {

    fun readSpreadSheet(spreadsheetId : String,
                        spreadsheetRange : String): Observable<MutableList<MutableList<Any>>>

    fun createSpreadsheet(spreadSheet : Spreadsheet) : Observable<MutableCollection<Any>>
}