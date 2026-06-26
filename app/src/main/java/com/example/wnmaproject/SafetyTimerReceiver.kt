package com.example.trekmesh

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SafetyTimerReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_SAFETY_TIMER_EXPIRED = "com.example.trekmesh.ACTION_SAFETY_TIMER_EXPIRED"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_SAFETY_TIMER_EXPIRED) {
            Log.i("SafetyTimerReceiver", "Safety timer expired! Triggering SOS via service.")

            val serviceIntent = Intent(context, TrekMeshService::class.java).apply {
                action = TrekMeshService.ACTION_AUTO_SOS
            }
            context.startForegroundService(serviceIntent)

            TrekMeshBus.updateSafetyTimer(0)
        }
    }
}
