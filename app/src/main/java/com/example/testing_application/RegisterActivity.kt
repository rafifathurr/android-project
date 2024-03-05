package com.example.testing_application

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextNewUsername: EditText
    private lateinit var editTextNewPassword: EditText
    private lateinit var editTextNewRePassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var txtLogin: TextView

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextNewUsername = findViewById(R.id.editTextNewUsername)
        editTextNewPassword = findViewById(R.id.editTextNewPassword)
        editTextNewRePassword = findViewById(R.id.editTextNewRePassword)
        btnRegister = findViewById(R.id.btnRegister)
        txtLogin = findViewById(R.id.txtLogin)

        databaseHelper = DatabaseHelper(this)

        btnRegister.setOnClickListener {
            val username = editTextNewUsername.text.toString()
            val password = editTextNewPassword.text.toString()
            val rePassword = editTextNewRePassword.text.toString()

            // Validate field of register view
            if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                // Show an alert
                showAlertEmptyFields()
            } else {

                // Validate the password confirmation fields
                if(password.equals(rePassword)){
                    // Process registration
                    val user = User(username = username, password = password)
                    insertUser(user)

                     // Redirect to login page
                    finish()
                }else{
                    showAlertPassword()
                }
            }
        }

        txtLogin.setOnClickListener {
            finish()
        }
    }

    private fun showAlertEmptyFields() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Error")
        alertDialogBuilder.setMessage("Please Fill In All Fields!")
        alertDialogBuilder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showAlertPassword() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Alert")
        alertDialogBuilder.setMessage("Password Not Same!")
        alertDialogBuilder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun insertUser(user: User) {
        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_USERNAME, user.username)
            put(DatabaseHelper.COLUMN_PASSWORD, user.password)
        }

        val newRowId = db.insert(DatabaseHelper.TABLE_USERS, null, values)

        println(newRowId)

        if (newRowId > -1) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error registering user.", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }
}