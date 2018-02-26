package com.pedrocarrillo.spreadsheetandroid.ui.create

import com.google.api.services.sheets.v4.model.*
import com.pedrocarrillo.spreadsheetandroid.data.manager.AuthenticationManager
import com.pedrocarrillo.spreadsheetandroid.data.model.Person
import com.pedrocarrillo.spreadsheetandroid.data.model.SpreadsheetMaker
import com.pedrocarrillo.spreadsheetandroid.data.repository.sheets.SheetsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * @author Pedro Carrillo.
 */
class CreateSpreadsheetPresenter(private val view: CreateSpreadsheetContract.View,
                                 private val authenticationManager: AuthenticationManager,
                                 private val sheetsRepository: SheetsRepository) :
        CreateSpreadsheetContract.Presenter {

    private val peopleList : MutableList<Person> = mutableListOf()
    private var disposable : Disposable? = null

    override fun init() {
        view.initList(peopleList)
        startAuthentication()
    }

    override fun dispose() {
        disposable?.dispose()
    }

    override fun addPerson(name: String, major: String) {
        peopleList.add(Person(name, major))
        view.showPerson()
        view.clearFields()
    }

    override fun uploadPeopleList() {
        if (peopleList.isNotEmpty()) {
            val spreadsheetMaker = SpreadsheetMaker()
            val spreadsheet: Spreadsheet =
                    spreadsheetMaker
                            .create("Example of creating spreadsheet",
                                    "Sheet1",
                                    peopleList)

            disposable = sheetsRepository
                    .createSpreadsheet(spreadsheet)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        view.showError(it.message!!)
                    }
                    .subscribe {
                        view.showResult(it.id, it.url)
                   }
        } else {
            view.showError("List is empty, please add some data")
        }
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