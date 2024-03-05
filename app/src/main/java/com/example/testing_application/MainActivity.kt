package com.example.testing_application

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtRegister: TextView

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextUsername = findViewById(R.id.usernameText)
        editTextPassword = findViewById(R.id.editTextTextPassword)
        btnLogin = findViewById(R.id.loginButton)
        txtRegister = findViewById(R.id.txtRegister)

        btnLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            // Validate field of register view
            if (username.isEmpty() || password.isEmpty()) {
                // Show an alert
                showAlertEmptyFields()
            } else {
                // Check if the user exists in the database
                if (checkUser(username, password)) {
                    val intent = Intent(this, HomePage::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Success Login!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    // Show an alert
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkUser(username: String, password: String): Boolean {
        val db = databaseHelper.readableDatabase

        val selection = "${DatabaseHelper.COLUMN_USERNAME} = ? AND ${DatabaseHelper.COLUMN_PASSWORD} = ?"
        val selectionArgs = arrayOf(username, password)

        val cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val userExists = cursor.count > 0

        cursor.close()
        db.close()

        return userExists
    }


    private fun showAlertEmptyFields() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Alert")
        alertDialogBuilder.setMessage("Please Fill In All Fields!")
        alertDialogBuilder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }}