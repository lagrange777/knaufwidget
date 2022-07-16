package com.vrt.knaufwidget

import android.Manifest
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.microsoft.identity.client.*
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException
import com.vrt.knaufwidget.appwidgets.ColorSchema
import com.vrt.knaufwidget.appwidgets.IntentType
import com.vrt.knaufwidget.appwidgets.SettingsHelper
import com.vrt.knaufwidget.appwidgets.buildIntent
import com.vrt.knaufwidget.appwidgets.calendar.KnaufWidgetCalendar
import com.vrt.knaufwidget.appwidgets.clock.KnaufWidgetClock
import com.vrt.knaufwidget.appwidgets.fx.KnaufWidgetFX
import com.vrt.knaufwidget.outlook.MSGraphRequestWrapper
import com.vrt.knaufwidget.outlook.OutlookEntity
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MILILOG"
        const val CALLBACK_ID = 42
        const val UPDATE_FROM_ACTIVITY = "android.appwidget.action.APPWIDGET_UPDATE"
        private const val APP_ID = """4779bfe3-69a7-4335-a003-7b28ae1bcd71"""

        fun checkPermission(context: Context, permissionsId: List<String>): Boolean {
            var permissions = true
            for (p in permissionsId) {
                permissions =
                    permissions && ContextCompat.checkSelfPermission(context, p) == PERMISSION_GRANTED
            }
            return permissions
        }
    }

    private val settingsAdapter: CalendarSettingsAdapter = CalendarSettingsAdapter()
    private val calendarUtility = Utility()

    private var mSingleAccountApp: ISingleAccountPublicClientApplication? = null
    private var outlookToken : String?
        get() = SettingsHelper.getSavedOutlookToken(this)
        set(value) {
            value ?: return
            SettingsHelper.saveOutlookToken(this, value)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission(CALLBACK_ID, listOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR))
        initButtons()
        updateTitle()
        initOutlookClient()
        initAdapter()
    }

    private fun checkPermission(callbackId: Int, permissionsIds: List<String>) {
        val permissions = Companion.checkPermission(this, permissionsIds)

        if (!permissions) ActivityCompat.requestPermissions(this, permissionsIds.toTypedArray(), callbackId)
    }

    private fun initButtons() {
        val b1 = findViewById<AppCompatButton>(R.id.buttonS1)
        val b2 = findViewById<AppCompatButton>(R.id.buttonS2)
        val b3 = findViewById<AppCompatButton>(R.id.buttonS3)
        val b4 = findViewById<AppCompatButton>(R.id.buttonS4)

        b1.setOnClickListener { onSchemaBtnClick(ColorSchema.Schema1) }
        b2.setOnClickListener { onSchemaBtnClick(ColorSchema.Schema2) }
        b3.setOnClickListener { onSchemaBtnClick(ColorSchema.Schema3) }
        b4.setOnClickListener { onSchemaBtnClick(ColorSchema.Schema4) }
    }

    private fun onSchemaBtnClick(schema: ColorSchema) {
        SettingsHelper.saveColorScheme(this, schema)
        notifyAllWidgets()
        updateTitle()
    }

    private fun initOutlookClient() {
        PublicClientApplication.createSingleAccountPublicClientApplication(
            this,
            R.raw.msal_config,
            object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication) {
                    /**
                     * This test app assumes that the app is only going to support one account.
                     * This requires "account_mode" : "SINGLE" in the config json file.
                     *
                     */
                    mSingleAccountApp = application

                    loadAccount()
                }

                override fun onError(exception: MsalException) {
                    val t = 0
                }
            })
    }

    private fun updateTitle() {
        val t = findViewById<AppCompatTextView>(R.id.schemaTitle)
        SettingsHelper.getSavedColorScheme(this).let {
            t.text = "Выбрана цветовая схема ${it.schemeID}"
        }
    }

    private fun notifyAllWidgets() {
        val manager = AppWidgetManager.getInstance(this)
        val intent = Intent(UPDATE_FROM_ACTIVITY)
        val clockIds = manager.getAppWidgetIds(ComponentName(this, KnaufWidgetClock::class.java))
        val fxIds = manager.getAppWidgetIds(ComponentName(this, KnaufWidgetFX::class.java))
        val calendarIds = manager.getAppWidgetIds(ComponentName(this, KnaufWidgetCalendar::class.java))
        intent.putExtra(KnaufWidgetClock.SCHEMA_UPDATE_KEY, clockIds)
        intent.putExtra(KnaufWidgetFX.SCHEMA_UPDATE_KEY, fxIds)
        intent.putExtra(KnaufWidgetCalendar.SCHEMA_UPDATE_KEY, calendarIds)
        this.sendBroadcast(intent)
    }

    private fun initAdapter() {
        val rv = findViewById<RecyclerView>(R.id.settingsRV)
        rv.adapter = settingsAdapter

        settingsAdapter.onItemClick { key, accName ->
            if (key == CalendarSettingsItem.ADD) {
                initOutlook()
            } else {
                if (accName == "Outlook")
                    mSingleAccountApp?.acquireToken(this, getScopes(), getAuthInteractiveCallback())
                SettingsHelper.saveAccForSync(this, accName)
            }

            settingsAdapter.data = generateAccList(outlookToken != null)
        }

        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        settingsAdapter.data = generateAccList(outlookToken != null)
    }

    private fun generateAccList(outlookAuthDone: Boolean): MutableList<CalendarSettingsItem> {
        val itemsList = mutableListOf<CalendarSettingsItem>()
        val selectedAcc = SettingsHelper.getSavedAccForSync(this)
        val accounts = calendarUtility.readCalendarEvent(this).map { it.accName }.toSet()

        accounts.forEach { accName ->
            if (accName == selectedAcc)
                CalendarSettingsItem.Enable().apply {
                    text = accName
                    itemsList.add(this)
                }
            else
                CalendarSettingsItem.Disable().apply {
                    text = accName
                    itemsList.add(this)
                }
        }
        if (outlookAuthDone) {
            if (selectedAcc == "Outlook")
                CalendarSettingsItem.Enable().apply {
                    text = "Outlook"
                    itemsList.add(this)
                } else
                CalendarSettingsItem.Disable().apply {
                    text = "Outlook"
                    itemsList.add(this)
                }
        } else
            CalendarSettingsItem.Add().apply {
                itemsList.add(this)
            }
        return itemsList
    }

    private fun initOutlook() {
        outlookToken?.let { callGraphAPI()} ?: mSingleAccountApp?.signIn(this, "", getScopes(), getAuthInteractiveCallback()) ?: return
    }

    private fun getScopes(): Array<String> {
        val scope = "user.read"
        return scope.toLowerCase().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    private fun getAuthInteractiveCallback(): AuthenticationCallback {
        return object : AuthenticationCallback {

            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d(TAG, "Successfully authenticated")
                Log.d(TAG, "ID Token: " + authenticationResult.account.claims!!["id_token"])

                /* Update account */
//                updateUI(authenticationResult.account)

                /* call graph */
                outlookToken = authenticationResult.accessToken
                callGraphAPI()
            }

            override fun onError(exception: MsalException) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: $exception")
//                displayError(exception)

                if (exception is MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception is MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            override fun onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.")
            }
        }
    }

    private fun callGraphAPI() {
        val token = outlookToken ?: return
        MSGraphRequestWrapper.callGraphAPIWithVolley(
            this,
            """https://graph.microsoft.com/v1.0/me/events""",
            token,
            { response ->
                val events = response.toClassObject<OutlookEntity>()
                /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: $response")

            },
            { error ->
                Log.d(TAG, "Error: $error")

            })
    }

    private fun loadAccount() {
        mSingleAccountApp ?: return
        mSingleAccountApp?.getCurrentAccountAsync(object :
            ISingleAccountPublicClientApplication.CurrentAccountCallback {
            override fun onAccountLoaded(activeAccount: IAccount?) {
                generateAccList(true)
            }

            override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) {
                if (currentAccount == null) {
                    // Perform a cleanup task as the signed-in account changed.
//                    performOperationOnSignOut()
                }
            }

            override fun onError(exception: MsalException) {
//                txt_log.text = exception.toString()
            }
        })
    }

}