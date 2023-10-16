package com.investbuddy.features.activity

import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.investbuddy.R
import com.investbuddy.common.decoder.DecoderDelegate
import com.investbuddy.common.di.IOnBackPressed
import com.investbuddy.common.event.ChangeNavViewVisibilityEvent
import com.investbuddy.core.base.mvvm.ViewCommand
import com.investbuddy.core.extension.observeCommands
import com.investbuddy.databinding.ActivityInitialBinding
import com.investbuddy.features.settings.ui.TermsFragment
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.RenderScriptBlur
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


@AndroidEntryPoint
class InitialActivity : AppCompatActivity() {

    companion object {
        const val TAG = "InitialActivity"
    }

    private val viewModel: InitialViewModel by viewModels()

    private lateinit var binding: ActivityInitialBinding

    private val setOfScreensWithVisibleBottomBar by lazy { getScreensWithVisibleBottomBar() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            getInitialInfo()
        }

        EventBus.getDefault().register(this)
    }

    private fun initViews() {
        binding = ActivityInitialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigationBar()
        observeCommands(viewModel.viewCommands) { handleViewCommand(it) }

        setFullscreenView()
        setupBlurView()
    }

    private fun setupBlurView() {
        val radius = 4f

        val decorView: View = window.decorView
        val rootView: ViewGroup = decorView.findViewById(android.R.id.content)
        val windowBackground: Drawable = decorView.background

        binding.blurView.setupWith(
            rootView,
            RenderScriptBlur(this)
        ) // or RenderEffectBlur
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)
    }

    private fun setFullscreenView() {
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun setStatusBar() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.nav_bar_bg)
    }

    private fun handleViewCommand(viewCommand: ViewCommand) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        when (viewCommand) {
            is NavigateToMain -> {
//                navController.navigate(R.id.action_splashFragment_to_mainFragment)
            }
        }
    }

    private fun setupBottomNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.setupWithNavController(navController)
        binding.navView.setBackgroundColor(ContextCompat.getColor(this, R.color.nav_view_bg))

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                in setOfScreensWithVisibleBottomBar -> {
                    setStatusBar()
                    changeVisibilityOfBottomNavigationBar(true)
                }

                else -> {
                    changeVisibilityOfBottomNavigationBar(false)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        kotlin.runCatching {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController

            if (navController.currentDestination?.id in setOfScreensWithVisibleBottomBar) {
                setStatusBar()
            }
        }
    }

    private fun changeVisibilityOfBottomNavigationBar(isVisible: Boolean) {
        binding.navView.isVisible = isVisible
    }

    /**
     * Набор экранов, в которых должен быть видимый боттом нав бар.
     * Такая логика сделана т.к. это полуофициальная рекомендация по работе с навигацией от Гугла.
     */
    private fun getScreensWithVisibleBottomBar(): Set<Int> {
        return setOf(
            R.id.chatFragment,
            R.id.calendarFragment,
            R.id.exchangeFragment,
            R.id.settingsFragment
        )
    }

    private fun showCloseDialog() {
        AlertDialog.Builder(this)
            .setTitle("Close the app")
            .setTitle("Are you sure you want to close the application?")
            .setNegativeButton("STAY"
            ) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("CLOSE"
            ) { dialog, which ->
                finishAffinity()
            }
            .show()

    }

    @Subscribe
    fun onChangeNavViewVisibilityEvent(e: ChangeNavViewVisibilityEvent) {
        changeVisibilityOfBottomNavigationBar(e.isVisible)
    }

    override fun onBackPressed() {
        val fragment =
            (this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
                .childFragmentManager.fragments.first()

        when (fragment) {
            is TermsFragment -> {
                super.onBackPressed()
            }

            is IOnBackPressed -> {
                (fragment as? IOnBackPressed)?.onBackPressed()?.let {
                    if (!it) {
    //                    super.onBackPressed()
                        showCloseDialog()
                    }
                }
            }

            else -> {
                showCloseDialog()
            }
        }
    }

    private lateinit var referrerClient: InstallReferrerClient
    private lateinit var cloudFirestoreDatabase: FirebaseFirestore
    private var isFirebaseAvailable = true
    private fun getInitialInfo() {

        FirebaseApp.initializeApp(this)
        try {
            cloudFirestoreDatabase = Firebase.firestore
        } catch (e: java.lang.Exception) {
            isFirebaseAvailable = false
        }

        FirebaseAnalytics.getInstance(this).appInstanceId.addOnCompleteListener {
            if (!it.isSuccessful || it.result.isNullOrEmpty()) {
                referrerClient = InstallReferrerClient.newBuilder(this).build()
                referrerClient.startConnection(object : InstallReferrerStateListener {

                    override fun onInstallReferrerSetupFinished(responseCode: Int) {
                        when (responseCode) {
                            InstallReferrerClient.InstallReferrerResponse.OK -> {
                                // Connection established.
                                val response: ReferrerDetails = referrerClient.installReferrer
                                val referrerUrl: String = response.installReferrer

                                initViews()
                            }
                        }
                    }

                    override fun onInstallReferrerServiceDisconnected() {
                        // Try to restart the connection on the next request to
                        // Google Play by calling the startConnection() method.
                    }
                })

                return@addOnCompleteListener
            }

            if (it.isSuccessful) {
                val appInstanceId = it.result.toString()

                referrerClient = InstallReferrerClient.newBuilder(this).build()
                referrerClient.startConnection(object : InstallReferrerStateListener {

                    override fun onInstallReferrerSetupFinished(responseCode: Int) {
                        when (responseCode) {
                            InstallReferrerClient.InstallReferrerResponse.OK -> {
                                // Connection established.
                                val response = URLEncoder.encode(referrerClient.installReferrer.installReferrer, "UTF-8")
//                                val response: ReferrerDetails = referrerClient.installReferrer
                                val referrerUrl: String = response//.installReferrer

                                val sdf = SimpleDateFormat("dd.MM HH:mm:ss", Locale.getDefault())
                                val currentDateandTime: String = sdf.format(Date())

                                val logData = hashMapOf(
                                    "app_instance_id" to appInstanceId,
                                    "referrer" to referrerUrl,
                                    "date" to currentDateandTime
                                )

                                cloudFirestoreDatabase.collection("referrers")
                                    .document(appInstanceId)
                                    .set(logData)
                                    .addOnSuccessListener {
                                        Log.d(
                                            "logs firebase",
                                            "DocumentSnapshot successfully written!"
                                        )
                                    }
                                    .addOnFailureListener { e -> Log.d("logs firebase", "Error writing document", e) }


//                                if (
//                                    referrerUrl.isNullOrEmpty()
//                                    && !appInstanceId.isNullOrEmpty()
//                                    && referrerUrl.contains("utm_source=")
//                                    && referrerUrl.substringAfter("utm_source=").split("&")[0] != "google-play"
//                                ) {
                                    viewModel.saveData(
                                        referrer = referrerUrl.substringAfter("utm_source=").split("&")[0],
                                        appInstanceId = appInstanceId
                                    )
//                                }

                                initViews()
                            }
                        }
                    }

                    override fun onInstallReferrerServiceDisconnected() {
                        // Try to restart the connection on the next request to
                        // Google Play by calling the startConnection() method.
                    }
                })
            }
        }
    }
}