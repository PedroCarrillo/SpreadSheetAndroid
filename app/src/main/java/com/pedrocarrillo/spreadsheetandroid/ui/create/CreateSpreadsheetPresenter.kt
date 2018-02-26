package com.pedrocarrillo.spreadsheetandroid.ui.create

import android.util.Log
import com.google.api.services.sheets.v4.model.*
import com.pedrocarrillo.spreadsheetandroid.data.manager.AuthenticationManager
import com.pedrocarrillo.spreadsheetandroid.data.model.Person
import com.pedrocarrillo.spreadsheetandroid.data.model.SpreadsheetMaker
import com.pedrocarrillo.spreadsheetandroid.data.repository.sheets.SheetsRepository
import com.pedrocarrillo.spreadsheetandroid.ui.read.ReadSpreadsheetPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * @author Pedro Carrillo.
 */
class CreateSpreadsheetPresenter(val view: CreateSpreadsheetContract.View,
                                 val authenticationManager: AuthenticationManager,
                                 val sheetsRepository: SheetsRepository) :
        CreateSpreadsheetContract.Presenter {

    val peopleList : MutableList<Person> = mutableListOf()

    override fun init() {
        view.initList(peopleList)
        startAuthentication()
    }

    override fun dispose() {
    }

    override fun addPerson(name: String, major: String) {
        peopleList.add(Person(name, major))
        view.showPerson()
        view.clearFields()
    }

    override fun uploadPeopleList() {
        val spreadsheetMaker = SpreadsheetMaker()
        val people = mutableListOf<Person>()
        people.add(Person("pedro", "did"))
        people.add(Person("carrillo", "this"))
        val spreadsheet : Spreadsheet =
                spreadsheetMaker
                        .create("Example of creating spreadsheet",
                                "Sheet1",
                                people)

//        val s = Spreadsheet()
//        val sheet = Sheet()
//        val sheetProperty = SheetProperties()
//        sheetProperty.title = "First"
//        sheet.properties = sheetProperty
//        val listGridData = mutableListOf<GridData>()
//        val gridData1 = GridData()
//        gridData1.startColumn = 0
//        gridData1.startRow = 0
//        val listRowData = mutableListOf<RowData>()
//        val rowData = RowData()
//        val listCellData : MutableList<CellData> = mutableListOf()
//        val cellData = CellData()
//        val extendedValue = ExtendedValue()
//        cellData.userEnteredValue = extendedValue.setStringValue("TEST2")
//
//
//        val cellData2 = CellData()
//        val extendedValue2 = ExtendedValue()
//        cellData2.userEnteredValue = extendedValue2.setStringValue("Pedro")
//
//
//        val rowData2 = RowData()
//        val listCellData2 : MutableList<CellData> = mutableListOf()
//        val cellData5 = CellData()
//        val extendedValue3 = ExtendedValue()
//        cellData5.userEnteredValue = extendedValue3.setStringValue("TEST5")
//        listCellData2.add(cellData5)
//        rowData2.setValues(listCellData2)
//
//
//        listCellData.add(cellData)
//        listCellData.add(cellData2)
//        rowData.setValues(listCellData)
//        listRowData.add(rowData)
//        listRowData.add(rowData2)
//        gridData1.rowData = listRowData
//        listGridData.add(gridData1)
//        sheet.data = listGridData
//        val sheet2 = Sheet()
//        sheet2.properties = SheetProperties()
//        sheet2.properties.title = "xinyu"
//        s.sheets = listOf(sheet)
//        val overallProperties = SpreadsheetProperties()
//        overallProperties.title = "TTTTTTTa"
//        s.properties = overallProperties


        sheetsRepository
                .createSpreadsheet(spreadsheet)
//                .flatMapIterable { it -> it }

//                .map { Person(it[0].toString(), it[4].toString()) }
//                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { view.showError(it.message!!)

                    Log.e("TAGGGG", "sdasdsd ")
                }
                .subscribe {
                    it.size
                    Log.e("TAGGGG", "sdasd1111sd " )
                }
//                .subscribe(Consumer {
//                    people.addAll(it)
//                    view.showPeople()
//                })
    }

    override fun loginSuccessful() {
        view.showName(authenticationManager.getLastSignedAccount()?.displayName!!)
        authenticationManager.setUpGoogleAccountCredential()
    }

    override fun loginFailed() {
    }

    private fun startAuthentication() {
        view.launchAuthentication(authenticationManager.googleSignInClient)
    }
}