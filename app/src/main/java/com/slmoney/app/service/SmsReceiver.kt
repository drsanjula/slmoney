package com.slmoney.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.slmoney.app.data.parser.SmsParserEngine
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    @Inject
    lateinit var parseSmsUseCase: com.slmoney.app.domain.usecase.ParseSmsUseCase

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (sms in messages) {
            val sender = sms.displayOriginatingAddress ?: ""
            val body = sms.displayMessageBody ?: ""
            val timestamp = sms.timestampMillis

            Log.d("SLMoney", "SMS from $sender: $body")

            kotlinx.coroutines.MainScope().launch {
                val transaction = parseSmsUseCase(sender, body, timestamp)
                if (transaction != null) {
                    Log.d("SLMoney", "Saved Transaction from SMS: $transaction")
                }
            }
        }
    }
}
