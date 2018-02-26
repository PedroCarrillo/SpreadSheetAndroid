package com.pedrocarrillo.spreadsheetandroid.data.repository.sheets

import com.google.api.services.sheets.v4.model.Spreadsheet
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * @author Pedro Carrillo.
 */
class SheetsRepository(private val sheetsAPIDataSource: SheetsAPIDataSource) {


    fun readSpreadSheet(spreadsheetId : String,
                        spreadsheetRange : String): Observable<MutableList<MutableList<Any>>> {
        return sheetsAPIDataSource.readSpreadSheet(spreadsheetId, spreadsheetRange)
    }

    fun createSpreadsheet(spreadSheet : Spreadsheet) : Observable<MutableCollection<Any>>  {
        return sheetsAPIDataSource.createSpreadsheet(spreadSheet)
    }

}