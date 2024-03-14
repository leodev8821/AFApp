package com.example.afapp.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.afapp.R
import com.example.afapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)

        // Show Back Button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // LOGIN BUTTON
        binding.loginButton.setOnClickListener{
            val intent = Intent(this, PostsActivity::class.java)
            startActivity(intent)
        }

        // REGISTER BUTTON
        binding.registerButton.setOnClickListener {

            if(binding.emailTextField.editText?.text?.isNotEmpty() == true){
                val email:String = binding.emailTextField.editText!!.text.toString()

                Log.i("EMAIL", email)

                val intent = Intent(this, UserRegisterActivity::class.java)
                intent.putExtra(UserRegisterActivity.EXTRA_EMAIL, email)
                startActivity(intent)
            }
            else{
                val intent = Intent(this, UserRegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // TO SHOW A CONFIRM EXIT DIALOG
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        //super.onBackPressed()
        showExitDialog()
    }

    private fun showExitDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setIcon(R.drawable.caution_svg)
            .setTitle("Cerrar aplicación")
            .setMessage("Esta seguro de que quiere salir de la aplicación?")
            .setPositiveButton("Salir") { _, _ -> finish() }
            .setNegativeButton("No") { dialog, _ -> dialog?.cancel() }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}