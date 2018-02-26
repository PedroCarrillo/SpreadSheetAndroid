package com.pedrocarrillo.spreadsheetandroid.ui.create

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.pedrocarrillo.spreadsheetandroid.data.model.Person
import com.pedrocarrillo.spreadsheetandroid.ui.base.BasePresenter
import com.pedrocarrillo.spreadsheetandroid.ui.base.BaseView

/**
 * @author Pedro Carrillo.
 */
interface CreateSpreadsheetContract {

    interface View : BaseView {
        fun showPerson()
        fun showName(username : String)
        fun launchAuthentication(client : GoogleSignInClient)
        fun initList(people: MutableList<Person>)
        fun clearFields()
        fun showResult(id : String, url : String)
    }

    interface Presenter : BasePresenter {
        fun addPerson(name : String, major : String)
        fun uploadPeopleList()
        fun loginSuccessful()
        fun loginFailed()
    }

}