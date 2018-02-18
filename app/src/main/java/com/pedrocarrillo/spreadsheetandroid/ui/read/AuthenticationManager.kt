package com.pedrocarrillo.spreadsheetandroid.ui.read

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.drive.Drive
import com.google.api.services.sheets.v4.SheetsScopes

/**
 * @author Pedro Carrillo
 */

class AuthenticationManager(val context : Lazy<Context>) {

    val googleSignInClient : GoogleSignInClient
        get() {
            val signInOptions : GoogleSignInOptions =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestScopes(Scope(SheetsScopes.SPREADSHEETS_READONLY))
                            .requestScopes(Drive.SCOPE_FILE)
                            .requestEmail()
                            .build()
            return GoogleSignIn.getClient(context.value, signInOptions)
        }

}