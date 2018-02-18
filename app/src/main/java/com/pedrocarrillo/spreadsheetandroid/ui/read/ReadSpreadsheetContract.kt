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
        fun showPeople(people : List<Person>)
        fun launchAuthentication(client : GoogleSignInClient)
    }

    interface Presenter : BasePresenter {
        fun startAuthentication()
    }

}