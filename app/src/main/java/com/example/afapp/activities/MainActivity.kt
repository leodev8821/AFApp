package com.example.afapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

    private lateinit var emailEditText:EditText
    private lateinit var passwordEditText:EditText
    private lateinit var loginButton:Button
    private lateinit var registerButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailEditText = binding.emailTextField.editText!!
        passwordEditText = binding.passwordTextField.editText!!
        loginButton = binding.loginButton
        registerButton = binding.registerButton
        
        //To create the admin user if it doesn't exist
        userDAO = UserDAO(this)
        if (userDAO.find("jaime@afapp.com") == null) {
            userDAO.insert(User(-1, "Jaime", "Caicedo", "jaime@afapp.com" , "asdf1234"))
        }

        //Go to PostActivity if is logged
        session = SessionManager(this)

        if(session.getUserLoginState()){
            val intent = Intent(this, PostsActivity::class.java)
            startActivity(intent)
        }

        initView()
    }

    private fun initView() {

        // LOGIN BUTTON
        loginButton.setOnClickListener{
            val email:String = emailEditText.text.toString()
            val password:String = passwordEditText.text.toString()

            if(userValidation(email, password) && email.isNotEmpty() && password.isNotEmpty()){
                session.setUserLoginState(true)
                session.setUserLoginEmail(email)
                val intent = Intent(this, PostsActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, R.string.loginTM, Toast.LENGTH_LONG).show()
            }

        }

        // REGISTER BUTTON
        registerButton.setOnClickListener {

            if(emailEditText.text?.isNotEmpty() == true){
                val email:String = emailEditText.text.toString()
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
    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        //super.onBackPressed()
        showExitDialog()
    }

    private fun showExitDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setIcon(R.drawable.caution_svg)
            .setTitle(R.string.exitTitleAD)
            .setMessage(R.string.exitMsgAD)
            .setPositiveButton(R.string.positiveButtonAD) { _, _ -> finish() }
            .setNegativeButton(R.string.negativeButtonAD) { dialog, _ -> dialog?.cancel() }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}