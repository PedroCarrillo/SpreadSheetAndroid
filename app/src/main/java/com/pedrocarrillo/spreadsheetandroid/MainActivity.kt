package com.pedrocarrillo.spreadsheetandroid

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {

    val RQ_GOOGLE_SIGN_IN = 999
    val TAG = "MainActivity"

    // authentication client
    lateinit var googleSignInClient : GoogleSignInClient
    // drive client
    lateinit var driveClient : DriveClient

    lateinit var tvUsername : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        googleSignInClient = buildClient()
        tvUsername = findViewById(R.id.tv_username)
        startActivityForResultSignIn()
    }

    //TODO: Silent sign in not working properly
    private fun silentSignIn() {
        val task = googleSignInClient.silentSignIn()
        task.let {
            if (it.isSuccessful){
                var result = it.result
                showName(result.displayName)
            } else {
                task.addOnCompleteListener {
                    val tsignInAccount = try {
                        task.result
                        showName(task.result.displayName)
                    } catch (runTimeException: RuntimeException) {
                        Log.e(TAG, "ERROR", runTimeException)
                    } catch (apiExcetion: ApiException) {
                        Log.e(TAG, "ERROR", apiExcetion)
                    }
                }
            }
        }
    }

    private fun showName(username : String?) {
        tvUsername.text = username
    }

    private fun startActivityForResultSignIn() {
        startActivityForResult(googleSignInClient.signInIntent, RQ_GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_GOOGLE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e(TAG, "log in successfully")
                showName(GoogleSignIn.getLastSignedInAccount(this)?.displayName)
                val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
                driveClient = Drive.getDriveClient(this, googleSignInAccount!!)

            }
        }
    }

    private fun buildClient(): GoogleSignInClient {
        var signInOptions : GoogleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Drive.SCOPE_FILE)
                .build()
        return GoogleSignIn.getClient(this, signInOptions)
    }

}
