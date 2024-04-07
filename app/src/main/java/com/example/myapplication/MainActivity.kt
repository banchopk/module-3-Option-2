package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val PREFS_NAME = "ContactBookPrefs"
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var contactsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEditText = findViewById(R.id.editTextName)
        phoneEditText = findViewById(R.id.editTextPhone)
        val addButton = findViewById<Button>(R.id.addButton)
        val deleteButton = findViewById<Button>(R.id.deleteButton)
        val editButton = findViewById<Button>(R.id.editButton)
        contactsTextView = findViewById(R.id.contactsTextView)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        addButton.setOnClickListener {
            addContact()
        }

        deleteButton.setOnClickListener {
            deleteContact()
        }

        editButton.setOnClickListener {
            editContact()
        }

        displayContacts()
    }

    private fun addContact() {
        val name = nameEditText.text.toString()
        val phone = phoneEditText.text.toString()

        if (name.isNotEmpty() && phone.isNotEmpty()) {
            val contactObject = JSONObject()
            try {
                contactObject.put("name", name)
                contactObject.put("phone", phone)

                val editor = sharedPreferences.edit()
                editor.putString(name, contactObject.toString())
                editor.apply()

                Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show()
                nameEditText.text.clear()
                phoneEditText.text.clear()

                displayContacts()

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "Please enter name and phone number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteContact() {
        val name = nameEditText.text.toString()
        if (name.isNotEmpty()) {
            val editor = sharedPreferences.edit()
            editor.remove(name)
            editor.apply()

            Toast.makeText(this, "Contact deleted successfully", Toast.LENGTH_SHORT).show()
            nameEditText.text.clear()
            phoneEditText.text.clear()

            displayContacts()
        } else {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editContact() {
        val name = nameEditText.text.toString()
        val phone = phoneEditText.text.toString()

        if (name.isNotEmpty() && phone.isNotEmpty()) {
            if (sharedPreferences.contains(name)) {
                val contactObject = JSONObject()
                try {
                    contactObject.put("name", name)
                    contactObject.put("phone", phone)

                    val editor = sharedPreferences.edit()
                    editor.putString(name, contactObject.toString())
                    editor.apply()

                    Toast.makeText(this, "Contact edited successfully", Toast.LENGTH_SHORT).show()
                    nameEditText.text.clear()
                    phoneEditText.text.clear()

                    displayContacts()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, "Contact not found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please enter name and phone number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayContacts() {
        val contacts = sharedPreferences.all
        var contactsList = ""
        for ((name, value) in contacts) {
            val contact = JSONObject(value.toString())
            val contactName = contact.getString("name")
            val contactPhone = contact.getString("phone")
            contactsList += "$contactName : $contactPhone\n"
        }
        contactsTextView.text = contactsList
    }
}
