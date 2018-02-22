package com.pedrocarrillo.spreadsheetandroid.data.repository.sheets

import io.reactivex.Observable

/**
 * @author Pedro Carrillo.
 */
class SheetsRepository(private val sheetsAPIDataSource: SheetsAPIDataSource) {


    fun readSpreadSheet(spreadsheetId : String,
                        spreadsheetRange : String): Observable<MutableList<MutableList<Any>>> {
        return sheetsAPIDataSource.readSpreadSheet(spreadsheetId, spreadsheetRange)
    }

}