package com.pedrocarrillo.spreadsheetandroid

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.pedrocarrillo.spreadsheetandroid.data.model.Person
import com.pedrocarrillo.spreadsheetandroid.ui.adapter.SpreadsheetAdapter
import com.pedrocarrillo.spreadsheetandroid.ui.read.AuthenticationManager
import com.pedrocarrillo.spreadsheetandroid.ui.read.ReadSpreadsheetContract
import com.pedrocarrillo.spreadsheetandroid.ui.read.ReadSpreadsheetPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * @author Pedro Carrillo
 */

class ReadSpreadsheetActivity : AppCompatActivity(), ReadSpreadsheetContract.View {

    // Google Account Credential
    lateinit var googleAccountCredential : GoogleAccountCredential
    // views
    lateinit var tvUsername : TextView
    lateinit var rvSpreadsheet : RecyclerView

    lateinit var presenter : ReadSpreadsheetContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_spreadsheet)
        bindingViews()
        presenter = ReadSpreadsheetPresenter(this, AuthenticationManager(lazyOf(this)))
        initComponents()
        presenter.init()
    }

    override fun showError(error: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showPeople(people: List<Person>) {
        val adapter = SpreadsheetAdapter(people)
        rvSpreadsheet.layoutManager = LinearLayoutManager(this)
        rvSpreadsheet.adapter = adapter
    }

    override fun launchAuthentication(client: GoogleSignInClient) {
        startActivityForResult(client.signInIntent, RQ_GOOGLE_SIGN_IN)
    }

    private fun bindingViews() {
        tvUsername = findViewById(R.id.tv_username)
        rvSpreadsheet = findViewById(R.id.rv_spreadsheet)
    }

    private fun startReadingSpreadSheet(spreadsheetId : String, range : String, sheet : Sheets) {
        Observable
                .fromCallable{
                    val response = sheet.spreadsheets().values()
                            .get(spreadsheetId, range)
                            .execute()
                    response.getValues() }
                .flatMapIterable { it -> it }
                .map { Person(it[0].toString(), it[4].toString()) }
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    val adapter = SpreadsheetAdapter(it)
                    rvSpreadsheet.layoutManager = LinearLayoutManager(this)
                    rvSpreadsheet.adapter = adapter
                })
    }

    private fun obtainingSheetsApi() : Sheets {
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        val sheets = Sheets.Builder(transport, jsonFactory, googleAccountCredential)
//                .setApplicationName("Reading my first Spreadsheet")
                .build()
        return sheets
    }

    private fun initComponents() {
        googleAccountCredential =
                GoogleAccountCredential.usingOAuth2(applicationContext, Arrays.asList(*SCOPES))
                        .setBackOff(ExponentialBackOff())
    }

    private fun showName(username : String?) {
        tvUsername.text = username
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_GOOGLE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e(TAG, "log in successfully")
                showName(GoogleSignIn.getLastSignedInAccount(this)?.displayName)
                val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
                googleAccountCredential.selectedAccount = googleSignInAccount?.account
                startReadingSpreadSheet(spreadsheetId, range, obtainingSheetsApi())
            }
        }
    }

    companion object {
        const val TAG = "ReadSpreadsheetActivity"
        const val RQ_GOOGLE_SIGN_IN = 999
        val SCOPES = arrayOf(SheetsScopes.SPREADSHEETS_READONLY)
        val spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms"
        val range = "Class Data!A2:E"
    }

}
