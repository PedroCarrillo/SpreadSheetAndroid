package com.pedrocarrillo.spreadsheetandroid.data.repository.sheets

import io.reactivex.Observable

/**
 * @author Pedro Carrillo.
 */
interface SheetsDataSource {

    fun readSpreadSheet(spreadsheetId : String,
                        spreadsheetRange : String): Observable<MutableList<MutableList<Any>>>
}