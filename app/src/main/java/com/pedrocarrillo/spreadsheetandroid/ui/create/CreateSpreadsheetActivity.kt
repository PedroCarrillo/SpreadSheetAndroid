package com.pedrocarrillo.spreadsheetandroid.ui.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
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
import com.pedrocarrillo.spreadsheetandroid.R
import com.pedrocarrillo.spreadsheetandroid.data.manager.AuthenticationManager
import com.pedrocarrillo.spreadsheetandroid.data.model.Person
import com.pedrocarrillo.spreadsheetandroid.data.repository.sheets.SheetsAPIDataSource
import com.pedrocarrillo.spreadsheetandroid.data.repository.sheets.SheetsRepository
import com.pedrocarrillo.spreadsheetandroid.ui.adapter.SpreadsheetAdapter
import com.pedrocarrillo.spreadsheetandroid.ui.base.BaseActivity
import java.util.*

/**
 * @author Pedro Carrillo.
 */
class CreateSpreadsheetActivity :
        BaseActivity<CreateSpreadsheetContract.Presenter>(), CreateSpreadsheetContract.View {

    private lateinit var btnAdd : Button
    private lateinit var btnUpload : Button
    private lateinit var etName : EditText
    private lateinit var etMajor : EditText
    private lateinit var tvUser : TextView
    private lateinit var rvSpreadsheet : RecyclerView

    private lateinit var spreadSheetAdapter : SpreadsheetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_spreadsheet)
        bindingViews()
        presenter.init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CreateSpreadsheetActivity.RQ_GOOGLE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.loginSuccessful()
            } else {
                presenter.loginFailed()
            }
        }
    }

    override fun initDependencies() {
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
        val sheetsApiDataSource =
                SheetsAPIDataSource(authManager,
                        AndroidHttp.newCompatibleTransport(),
                        JacksonFactory.getDefaultInstance())
        val sheetsRepository = SheetsRepository(sheetsApiDataSource)
        presenter = CreateSpreadsheetPresenter(this, authManager, sheetsRepository)
    }

    private fun bindingViews() {
        rvSpreadsheet = findViewById(R.id.rv_spreadsheet)
        tvUser = findViewById(R.id.tv_username)
        btnAdd = findViewById(R.id.btn_add)
        btnUpload = findViewById(R.id.btn_upload)
        etMajor = findViewById(R.id.et_major)
        etName = findViewById(R.id.et_name)
        btnAdd.setOnClickListener({
            addPerson()
        })
        btnUpload.setOnClickListener({
            presenter.uploadPeopleList()
        })
    }

    private fun addPerson() {
        presenter.addPerson(etName.text.toString(), etMajor.text.toString())
    }

    // View implementation

    override fun initList(people: MutableList<Person>) {
        spreadSheetAdapter = SpreadsheetAdapter(people)
        rvSpreadsheet.layoutManager = LinearLayoutManager(this)
        rvSpreadsheet.adapter = spreadSheetAdapter
    }

    override fun clearFields() {
        etMajor.text.clear()
        etName.text.clear()
        etName.requestFocus()
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun showPerson() {
        spreadSheetAdapter.notifyDataSetChanged()
    }

    override fun showName(username: String) {
        tvUser.text = username
    }

    override fun launchAuthentication(client: GoogleSignInClient) {
        startActivityForResult(client.signInIntent, CreateSpreadsheetActivity.RQ_GOOGLE_SIGN_IN)
    }

    override fun showResult(id: String, url: String) {
        Toast.makeText(this, "spreadheet created id: "+ id , Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "ReadSpreadsheetActivity"
        const val RQ_GOOGLE_SIGN_IN = 999
    }


}