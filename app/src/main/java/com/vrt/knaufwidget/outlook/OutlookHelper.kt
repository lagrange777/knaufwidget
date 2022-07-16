package com.vrt.knaufwidget.outlook

import android.app.Activity
import android.content.Context
import android.util.Log
import com.microsoft.identity.client.*
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException
import com.vrt.knaufwidget.MainActivity
import com.vrt.knaufwidget.R
import com.vrt.knaufwidget.appwidgets.SettingsHelper
import com.vrt.knaufwidget.toClassObject

class OutlookHelper(private val context: Context) {

    private var mSingleAccountApp: ISingleAccountPublicClientApplication? = null
    private var outlookToken: String?
        get() = SettingsHelper.getSavedOutlookToken(context)
        set(value) {
            value ?: return
            SettingsHelper.saveOutlookToken(context, value)
        }

    init {
        initOutlookClient()
    }

    fun initOutlook(activity: Activity?, getEvents: ((List<OutlookEventEntity>) -> Unit)?) {
        outlookToken?.let { callGraphAPI(getEvents) } ?: activity?.let { mSingleAccountApp?.signIn(activity, "", getScopes(), getAuthInteractiveCallback()) } ?: return
    }

    private fun initOutlookClient() {
        PublicClientApplication.createSingleAccountPublicClientApplication(
            context,
            R.raw.msal_config,
            object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication) {
                    /**
                     * This test app assumes that the app is only going to support one account.
                     * This requires "account_mode" : "SINGLE" in the config json file.
                     *
                     */
                    mSingleAccountApp = application

//                    loadAccount()
                }

                override fun onError(exception: MsalException) {
                    val t = 0
                }
            })
    }

    private fun callGraphAPI(getEvents: ((List<OutlookEventEntity>) -> Unit)? = null) {
        val token = outlookToken ?: return
        MSGraphRequestWrapper.callGraphAPIWithVolley(
            context,
            """https://graph.microsoft.com/v1.0/me/events""",
            token,
            { response ->
                response.toClassObject<OutlookEntity>().info.also { getEvents?.invoke(it) }
                /* Successfully called graph, process data and send to UI */
                Log.d(MainActivity.TAG, "Response: $response")

            },
            { error ->
                Log.d(MainActivity.TAG, "Error: $error")

            })
    }

    private fun getScopes(): Array<String> {
        val scope = "user.read"
        return scope.toLowerCase().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    private fun getAuthInteractiveCallback(): AuthenticationCallback {
        return object : AuthenticationCallback {

            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d(MainActivity.TAG, "Successfully authenticated")
                Log.d(MainActivity.TAG, "ID Token: " + authenticationResult.account.claims!!["id_token"])

                /* Update account */
//                updateUI(authenticationResult.account)

                /* call graph */
                outlookToken = authenticationResult.accessToken
//                callGraphAPI()
            }

            override fun onError(exception: MsalException) {
                /* Failed to acquireToken */
                Log.d(MainActivity.TAG, "Authentication failed: $exception")
//                displayError(exception)

                if (exception is MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception is MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            override fun onCancel() {
                /* User canceled the authentication */
                Log.d(MainActivity.TAG, "User cancelled login.")
            }
        }
    }
}