package com.lemust.utils

class UserCredentials {
    private var token: String = ""
    private var currentUserId: Int = -1

    fun getAccessToken(): String {
        if (token.isEmpty()) {
            token = AppHelper.preferences.getToken()
        }
        return token
    }

    fun saveAccessToken(token: String) {
        AppHelper.preferences.saveAccessToken(token)
    }


    fun getCurrentUserId(): Int {
        if (currentUserId < 0) {
            currentUserId = AppHelper.preferences.getUserId()
        }
        return currentUserId
    }

//    fun saveCurrentId(id: Int) {
//        AppHelper.preferences.saveAccessToken(token)
  //  }

    fun clear() {
        AppHelper.preferences.clearToken()
        token = ""
        currentUserId = -1
       // saveCurrentId(currentUserId)

    }


}
