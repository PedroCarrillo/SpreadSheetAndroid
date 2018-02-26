package com.pedrocarrillo.spreadsheetandroid.ui.read

import com.pedrocarrillo.spreadsheetandroid.data.model.Person
import com.pedrocarrillo.spreadsheetandroid.data.repository.sheets.SheetsRepository
import com.pedrocarrillo.spreadsheetandroid.data.manager.AuthenticationManager
import com.pedrocarrillo.spreadsheetandroid.ui.read.ReadSpreadsheetContract.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * @author Pedro Carrillo
 */

class ReadSpreadsheetPresenter(private val view: ReadSpreadsheetContract.View,
                               private val authenticationManager: AuthenticationManager,
                               private val sheetsRepository: SheetsRepository) : Presenter {

    private lateinit var readSpreadsheetDisposable : Disposable
    private val people : MutableList<Person> = mutableListOf()

    override fun startAuthentication() {
        view.launchAuthentication(authenticationManager.googleSignInClient)
    }

    override fun init() {
        startAuthentication()
        view.initList(people)
    }

    override fun dispose() {
        readSpreadsheetDisposable.dispose()
    }

    override fun loginSuccessful() {
        view.showName(authenticationManager.getLastSignedAccount()?.displayName!!)
        authenticationManager.setUpGoogleAccountCredential()
        startReadingSpreadsheet(spreadsheetId, range)
    }

    override fun loginFailed() {

    }

    private fun startReadingSpreadsheet(spreadsheetId : String, range : String) {
        people.clear()
        readSpreadsheetDisposable=
                sheetsRepository.readSpreadSheet(spreadsheetId, range)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { view.showError(it.message!!) }
                .subscribe(Consumer {
                    people.addAll(it)
                    view.showPeople()
                })
    }

    companion object {
        val spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms"
        val range = "Class Data!A2:E"
    }
}
