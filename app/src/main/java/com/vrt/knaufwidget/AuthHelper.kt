package com.vrt.knaufwidget

import android.app.Activity
import android.content.Context
import android.util.Log
import com.microsoft.graph.authentication.BaseAuthenticationProvider
import com.microsoft.identity.client.*
import com.microsoft.identity.client.exception.MsalException
import java.net.URL
import java.util.concurrent.CompletableFuture


interface IAuthenticationHelperCreatedListener {
    fun onCreated(authHelper: AuthenticationHelper?)
    fun onError(exception: MsalException?)
}

class AuthenticationHelper private constructor(
    ctx: Context,
    listener: IAuthenticationHelperCreatedListener
) :
    BaseAuthenticationProvider() {
    private var mPCA: ISingleAccountPublicClientApplication? = null
    private val mScopes = arrayOf("User.Read", "MailboxSettings.Read", "Calendars.ReadWrite")

    fun acquireTokenInteractively(activity: Activity?): CompletableFuture<IAuthenticationResult> {

        val future: CompletableFuture<IAuthenticationResult> = CompletableFuture()
        activity?.let { mPCA?.signIn(it, null, mScopes, getAuthenticationCallback(future)) }
        return future
    }

    fun acquireTokenSilently(): CompletableFuture<IAuthenticationResult> {
        // Get the authority from MSAL config
        val authority: String =
            mPCA
                ?.configuration
                ?.defaultAuthority
                ?.authorityURL.toString()
        val future: CompletableFuture<IAuthenticationResult> = CompletableFuture()
        mPCA?.acquireTokenSilentAsync(mScopes, authority, getAuthenticationCallback(future))
        return future
    }

    fun signOut() {
        mPCA?.signOut(object : ISingleAccountPublicClientApplication.SignOutCallback {
            override fun onSignOut() {
                Log.d("AUTHHELPER", "Signed out")
            }

            override fun onError(exception: MsalException) {
                Log.d("AUTHHELPER", "MSAL error signing out", exception)
            }

        })
    }

    private fun getAuthenticationCallback(future: CompletableFuture<IAuthenticationResult>): AuthenticationCallback {
        return object : AuthenticationCallback {
            override fun onCancel() {
                future.cancel(true)
            }

            override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                future.complete(authenticationResult)
            }

            override fun onError(exception: MsalException?) {
                future.completeExceptionally(exception)
            }
        }
    }

    override fun getAuthorizationTokenAsync(requestUrl: URL): CompletableFuture<String> {
        return if (shouldAuthenticateRequestWithUrl(requestUrl)) {
            acquireTokenSilently()
                .thenApply { result -> result.getAccessToken() }
        } else CompletableFuture.completedFuture(null)
    }

    companion object {
        private var INSTANCE: AuthenticationHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): CompletableFuture<AuthenticationHelper> {
            return if (INSTANCE == null) {
                val future: CompletableFuture<AuthenticationHelper> = CompletableFuture()
                INSTANCE =
                    AuthenticationHelper(ctx, object : IAuthenticationHelperCreatedListener {
                        override fun onCreated(authHelper: AuthenticationHelper?) {
                            future.complete(authHelper)
                        }

                        override fun onError(exception: MsalException?) {
                            future.completeExceptionally(exception)
                        }
                    })
                future
            } else {
                CompletableFuture.completedFuture(INSTANCE)
            }
        }

        // Version called from fragments. Does not create an
        // instance if one doesn't exist
        @get:Synchronized
        val instance: AuthenticationHelper?
            get() {
                checkNotNull(INSTANCE) { "AuthenticationHelper has not been initialized from MainActivity" }
                return INSTANCE
            }
    }

    init {
        PublicClientApplication.createSingleAccountPublicClientApplication(ctx, R.raw.msal_config,
            object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication?) {
                    mPCA = application
                    listener.onCreated(INSTANCE)
                }

                override fun onError(exception: MsalException?) {
                    Log.e("AUTHHELPER", "Error creating MSAL application", exception)
                    listener.onError(exception)
                }
            })
    }
}