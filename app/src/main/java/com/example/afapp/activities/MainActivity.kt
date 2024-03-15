package com.example.afapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.afapp.R
import com.example.afapp.database.User
import com.example.afapp.database.providers.UserDAO
import com.example.afapp.database.utils.SessionManager
import com.example.afapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var session: SessionManager

    private lateinit var userDAO:UserDAO

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //To create the admin user if it doesn't exist
        userDAO = UserDAO(this)
        if (userDAO.find(1) == null) {
            userDAO.insert(User(-1, "Jaime", "Caicedo", "jaime@afapp.com" , "abcd1234"))
        }

        //Go to PostActivity if is logged
        session = SessionManager(this)

        if(session.getLoggedUser() != "null"){
            val intent = Intent(this, PostsActivity::class.java)
            startActivity(intent)
        }

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
            val email:String = binding.emailTextField.editText?.text.toString()
            val password:String = binding.passwordTextField.editText?.text.toString()

            if(userValidation(email, password)){
                val intent = Intent(this, PostsActivity::class.java)
                intent.putExtra(PostsActivity.EXTRA_EMAIL, email)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Datos incorrectos!", Toast.LENGTH_LONG).show()
            }

        }

        // REGISTER BUTTON
        binding.registerButton.setOnClickListener {

            if(binding.emailTextField.editText?.text?.isNotEmpty() == true){
                val email:String = binding.emailTextField.editText!!.text.toString()

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

    //VALIDATE IF A USER IS REGISTERED
    private fun userValidation(email:String, pass:String) :Boolean{
        var ok = false

        if(email.isNotEmpty() && pass.isNotEmpty()){
            val user = userDAO.findByEmailPass(email, pass)

            if (user != null) {
                if (email == user.email && pass == user.password){
                    ok = true
                }
            }
        }
        return ok
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