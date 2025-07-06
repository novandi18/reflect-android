package com.novandiramadhan.reflect.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun sendFeedbackEmail(context: Context, email: String, subject: String, message: String) {
    val emailBody = """
        Hello Reflect Team,

        I would like to share the following feedback:

        $message

        Thank you for your attention.

        (Sent anonymously via Reflect App)
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:$email".toUri()
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, emailBody)
    }
    context.startActivity(Intent.createChooser(intent, "Send Feedback"))
}

fun sendBugReportEmail(
    context: Context,
    to: String,
    subject: String,
    category: String,
    description: String,
    opening: String,
    closing: String
) {
    val body = "$opening\n\nCategory: $category\n\nDescription:\n$description\n$closing"
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:$to".toUri()
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
    context.startActivity(Intent.createChooser(intent, "Send Email"))
}