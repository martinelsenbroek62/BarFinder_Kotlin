package com.lemust.repository.api

import io.reactivex.subjects.PublishSubject
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class TokenAuthenticator(var apiLeMust: PublishSubject<Any>) : Authenticator {
    override fun authenticate(route: Route?, response: Response?): Request? {
        apiLeMust.onNext(Any())

        return response?.request()?.newBuilder()
                ?.build()
    }


}