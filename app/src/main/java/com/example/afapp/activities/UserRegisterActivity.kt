package com.example.afapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.afapp.database.User
import com.example.afapp.database.providers.UserDAO
import com.example.afapp.databinding.ActivityUserRegisterBinding

class UserRegisterActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_EMAIL = "EMAIL"
    }

    private var email:String? = null
    private lateinit var userDAO:UserDAO

    private lateinit var binding:ActivityUserRegisterBinding

    private lateinit var emailEditText:EditText
    private lateinit var nameEditText:EditText
    private lateinit var lastnameEditText:EditText
    private lateinit var pass1EditText:EditText
    private lateinit var pass2EditText:EditText
    private lateinit var registerButton:Button
    private lateinit var clearButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailEditText = binding.emailTextField.editText!!
        nameEditText = binding.nameTextField.editText!!
        lastnameEditText = binding.lastnameTextField.editText!!
        pass1EditText = binding.password1TextField.editText!!
        pass2EditText = binding.password2TextField.editText!!

        registerButton = binding.createUserButton
        clearButton = binding.clearButton

        initView()
    }

    private fun initView() {

        initActionBar()

        userDAO = UserDAO(this)

        //Get the email from MainActivity
        email = intent.getStringExtra(EXTRA_EMAIL)

        // To focus Name EditText if email != null
        if (email != null){
            emailEditText.setText(email)
            nameEditText.requestFocus()
        }
        else{
            emailEditText.requestFocus()
        }

        clearButton.setOnClickListener {
            clearForm()
        }

        registerButton.setOnClickListener {
            val name:String = nameEditText.text.toString()
            val lastname:String = lastnameEditText.text.toString()
            val pass1:String = pass1EditText.text.toString()
            val pass2:String = pass2EditText.text.toString()

            if(email == null){
                val inputEmail:String = emailEditText.text.toString()
                val message:String = registerUser(name, lastname, inputEmail, pass1, pass2)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            else{
                val message:String = registerUser(name, lastname, email, pass1, pass2)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(name:String, lastname:String , email: String?, pass1:String, pass2:String) :String {

        if (email != null) {
            return if (name.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty() && pass1.isNotEmpty() && pass2.isNotEmpty()){

                if (comparePass(pass1,pass2)){

                    val newUser = User(-1, name, lastname, email, pass1)
                    userDAO.insert(newUser)
                    clearForm()

                    intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                    "New user was created!"
                } else{
                    "Please, confirm your password"
                }
            } else{
                "Please fill all the inputs"
            }
        }
        else{
            return "Email cannot be empty"
        }

    }

    private fun comparePass(pass1:String, pass2:String) : Boolean{
        return pass1 == pass2
    }

    private fun clearForm() {
        emailEditText.setText("")
        nameEditText.setText("")
        lastnameEditText.setText("")
        pass1EditText.setText("")
        pass2EditText.setText("")
        emailEditText.focusable
    }

    private fun initActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // To listen the item selected in a menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}