package com.example.pnbase.userprofile.domain

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.pnbase.userprofile.data.ContactsProvider
import com.example.pnbase.userprofile.data.SmsProvider
import org.json.JSONObject
import java.io.File
import java.io.IOException

fun exportContactToJson(
    context: Context
): Uri? {
    val contacts = ContactsProvider(context.contentResolver).getContacts()
    val sms = SmsProvider(context.contentResolver).getSms()

    val exportContacts = mutableMapOf<String, String>()
    contacts.forEach { exportContacts[it.name] = it.mimeType }

    val exportSms = mutableMapOf<String, String>()
    sms.forEach { exportSms[it.address] = it.id }
    /*
    val file = File("0")
    file.listFiles()
    val file1 = File("0/1")
    file1.listFiles()
*/

    val json = JSONObject().apply {
        put("contacts", JSONObject(exportContacts as Map<*, *>))
        put("sms", JSONObject(exportSms as Map<*, *>))
    }


    return try {
        val file = File(context.cacheDir, "phone_export_${System.currentTimeMillis()}.json")
        file.writeText(json.toString(2))

        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
