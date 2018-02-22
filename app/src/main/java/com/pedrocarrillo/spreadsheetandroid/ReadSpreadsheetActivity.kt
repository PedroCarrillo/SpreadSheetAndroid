package com.pedrocarrillo.spreadsheetandroid

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.drive.Drive
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import com.pedrocarrillo.spreadsheetandroid.data.model.Person
import com.pedrocarrillo.spreadsheetandroid.data.repository.SheetsAPIDataSource
import com.pedrocarrillo.spreadsheetandroid.data.repository.SheetsRepository
import com.pedrocarrillo.spreadsheetandroid.ui.adapter.SpreadsheetAdapter
import com.pedrocarrillo.spreadsheetandroid.ui.base.AuthenticationManager
import com.pedrocarrillo.spreadsheetandroid.ui.read.ReadSpreadsheetContract
import com.pedrocarrillo.spreadsheetandroid.ui.read.ReadSpreadsheetPresenter
import java.util.*

/**
 * @author Pedro Carrillo
 */

class ReadSpreadsheetActivity : AppCompatActivity(), ReadSpreadsheetContract.View {

    private lateinit var tvUsername : TextView
    private lateinit var rvSpreadsheet : RecyclerView

    private lateinit var presenter : ReadSpreadsheetContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_spreadsheet)
        bindingViews()
        initDependencies()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_GOOGLE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.loginSuccessful()
            } else {
                presenter.loginFailed()
            }
        }
    }

    private fun initDependencies() {
        val signInOptions : GoogleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Scope(SheetsScopes.SPREADSHEETS_READONLY))
                        .requestScopes(Drive.SCOPE_FILE)
                        .requestEmail()
                        .build()
        val googleSignInClient = GoogleSignIn.getClient(this, signInOptions)
        val googleAccountCredential = GoogleAccountCredential
                .usingOAuth2(this, Arrays.asList(*AuthenticationManager.SCOPES))
                .setBackOff(ExponentialBackOff())
        val authManager =
                AuthenticationManager(
                        lazyOf(this),
                        googleSignInClient,
                        googleAccountCredential)
        val sheetsApidatasource =
                SheetsAPIDataSource(authManager,
                        AndroidHttp.newCompatibleTransport(),
                        JacksonFactory.getDefaultInstance())
        val sheetsRepository = SheetsRepository(sheetsApidatasource)
        presenter = ReadSpreadsheetPresenter(this, authManager, sheetsRepository)
        presenter.init()

    }

    private fun bindingViews() {
        tvUsername = findViewById(R.id.tv_username)
        rvSpreadsheet = findViewById(R.id.rv_spreadsheet)
    }

    // View related implementations
    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun showPeople(people: List<Person>) {
        val adapter = SpreadsheetAdapter(people)
        rvSpreadsheet.layoutManager = LinearLayoutManager(this)
        rvSpreadsheet.adapter = adapter
    }

    override fun launchAuthentication(client: GoogleSignInClient) {
        startActivityForResult(client.signInIntent, RQ_GOOGLE_SIGN_IN)
    }

    override fun showName(username : String) {
        tvUsername.text = username
    }

    companion object {
        const val TAG = "ReadSpreadsheetActivity"
        const val RQ_GOOGLE_SIGN_IN = 999
    }

}
