package com.pedrocarrillo.spreadsheetandroid.ui.read

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.pedrocarrillo.spreadsheetandroid.data.model.Person
import com.pedrocarrillo.spreadsheetandroid.ui.base.BasePresenter
import com.pedrocarrillo.spreadsheetandroid.ui.base.BaseView

/**
 * @author Pedro Carrillo
 */

interface ReadSpreadsheetContract {

    interface View : BaseView{
        fun initList(people: MutableList<Person>)
        fun showPeople()
        fun showName(username : String)
        fun launchAuthentication(client : GoogleSignInClient)
    }

    interface Presenter : BasePresenter {
        fun startAuthentication()
        fun loginSuccessful()
        fun loginFailed()
    }

}