package com.pedrocarrillo.spreadsheetandroid.ui.read

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.drive.Drive
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import java.util.*

/**
 * @author Pedro Carrillo
 */

class AuthenticationManager(val context: Lazy<Context>,
                            val googleSignInClient : GoogleSignInClient,
                            val googleAccountCredential : GoogleAccountCredential?) {

    fun getLastSignedAccount() : GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context.value)
    }

    fun setUpGoogleAccountCredential() {
        googleAccountCredential?.selectedAccount = getLastSignedAccount()?.account
    }

    companion object {
        val SCOPES = arrayOf(SheetsScopes.SPREADSHEETS_READONLY)
    }

}