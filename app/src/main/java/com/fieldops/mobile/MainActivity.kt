package com.fieldops.mobile

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

private const val TAG = "TS7AutoLaunch"

private const val IPHONE_MAC = "70:96:84:39:09:7C"

private const val DIPLAY_PACKAGE = "com.shihab.diplay"
private const val DIPLAY_ACTIVITY =
    "com.shilapi.xcertplay.DiPlayActivity"

private const val DEBOUNCE_MS = 45_000L


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
        }

        val title = TextView(this).apply {
            text = "TS7 AutoLaunch"
            textSize = 26f
            gravity = Gravity.CENTER
        }

        val description = TextView(this).apply {
            text = """
                Bluetooth AutoLaunch is installed.

                Waiting for your iPhone:
                $IPHONE_MAC

                DiPlay will launch when your iPhone connects.
            """.trimIndent()
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 30, 0, 30)
        }

        val testButton = Button(this).apply {
            text = "Test: Open DiPlay"
            setOnClickListener {
                launchDiPlay(this@MainActivity)
            }
        }

        layout.addView(title)
        layout.addView(description)
        layout.addView(testButton)

        setContentView(layout)
    }
}


class BluetoothBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {

            Intent.ACTION_BOOT_COMPLETED -> {
                Log.i(TAG, "Head unit booted. Bluetooth listener registered.")
            }

            BluetoothDevice.ACTION_ACL_CONNECTED -> {

                @Suppress("DEPRECATION")
                val device = intent.getParcelableExtra<BluetoothDevice>(
                    BluetoothDevice.EXTRA_DEVICE
                )

                val connectedAddress = device?.address ?: return

                Log.i(TAG, "Bluetooth connected: $connectedAddress")

                if (!connectedAddress.equals(IPHONE_MAC, ignoreCase = true)) {
                    Log.i(TAG, "Not the configured iPhone. Ignoring.")
                    return
                }

                val prefs = context.getSharedPreferences(
                    "ts7_autolaunch",
                    Context.MODE_PRIVATE
                )

                val now = System.currentTimeMillis()
                val lastLaunch = prefs.getLong("last_launch", 0L)

                if (now - lastLaunch < DEBOUNCE_MS) {
                    Log.i(TAG, "Debounce active. Skipping duplicate launch.")
                    return
                }

                prefs.edit()
                    .putLong("last_launch", now)
                    .apply()

                Log.i(TAG, "Configured iPhone connected. Launching DiPlay.")

                launchDiPlay(context)
            }
        }
    }
}


private fun launchDiPlay(context: Context) {

    try {
        val launchIntent = Intent().apply {
            setClassName(DIPLAY_PACKAGE, DIPLAY_ACTIVITY)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        context.startActivity(launchIntent)

        Log.i(TAG, "DiPlay launch requested successfully.")

    } catch (e: Exception) {

        Log.e(TAG, "Could not launch DiPlay.", e)
    }
}
