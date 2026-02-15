package com.slmoney.app.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.slmoney.app.data.parser.SmsParserEngine
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TransactionNotificationListener : NotificationListenerService() {

    @Inject
    lateinit var parseSmsUseCase: com.slmoney.app.domain.usecase.ParseSmsUseCase

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        if (packageName !in bankPackages) return

        val extras = sbn.notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE) ?: ""
        val text = extras.getString(Notification.EXTRA_TEXT) ?: ""
        val sender = title.ifEmpty { packageName }
        
        Log.d("SLMoney", "Notification from $sender: $text")

        kotlinx.coroutines.MainScope().launch {
            val transaction = parseSmsUseCase(sender, text, sbn.postTime)
            if (transaction != null) {
                Log.d("SLMoney", "Saved Transaction from notification: $transaction")
            }
        }
    }
}
