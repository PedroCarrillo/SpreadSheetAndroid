package com.pedrocarrillo.spreadsheetandroid.ui.read

import com.pedrocarrillo.spreadsheetandroid.data.model.Person
import com.pedrocarrillo.spreadsheetandroid.data.repository.SheetsRepository
import com.pedrocarrillo.spreadsheetandroid.ui.base.AuthenticationManager
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

    lateinit var readSpreadsheetDisposable : Disposable

    override fun startAuthentication() {
        view.launchAuthentication(authenticationManager.googleSignInClient)
    }

    override fun init() {
        startAuthentication()
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun startReadingSpreadsheet(spreadsheetId : String, range : String) {
        readSpreadsheetDisposable=
                sheetsRepository.readSpreadSheet(spreadsheetId, range)
                .flatMapIterable { it -> it }
                .map { Person(it[0].toString(), it[4].toString()) }
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { view.showError(it.localizedMessage) }
                .subscribe(Consumer {
                    view.showPeople(it)
                })
    }

    companion object {
        val spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms"
        val range = "Class Data!A2:E"
    }
}
