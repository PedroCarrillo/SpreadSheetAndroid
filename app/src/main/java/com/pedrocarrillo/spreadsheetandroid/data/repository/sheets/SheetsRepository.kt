package com.pedrocarrillo.spreadsheetandroid.data.repository.sheets

import com.google.api.services.sheets.v4.model.Spreadsheet
import com.pedrocarrillo.spreadsheetandroid.data.model.Person
import com.pedrocarrillo.spreadsheetandroid.data.model.SpreadsheetInfo
import io.reactivex.Observable
import io.reactivex.Single

/**
 * @author Pedro Carrillo.
 */
class SheetsRepository(private val sheetsAPIDataSource: SheetsAPIDataSource) {


    fun readSpreadSheet(spreadsheetId : String,
                        spreadsheetRange : String): Single<List<Person>> {
        return sheetsAPIDataSource.readSpreadSheet(spreadsheetId, spreadsheetRange)
    }

    fun createSpreadsheet(spreadSheet : Spreadsheet) : Observable<SpreadsheetInfo>  {
        return sheetsAPIDataSource.createSpreadsheet(spreadSheet)
    }

}