package com.example.afapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {

        initActionBar()

        userDAO = UserDAO(this)

        email = intent.getStringExtra(EXTRA_EMAIL)

        // To focus Name EditText if email != null
        if (email != null){
            binding.emailTextField.editText?.setText(email)
            binding.nameTextField.requestFocus()
        }
        else{
            binding.emailTextField.requestFocus()
        }

        binding.clearButton.setOnClickListener {
            clearForm()
        }

        binding.createUserButton.setOnClickListener {
            val name:String = binding.nameTextField.editText?.text.toString()
            val lastname:String = binding.lastnameTextField.editText?.text.toString()
            val pass1:String = binding.password1TextField.editText?.text.toString()
            val pass2:String = binding.password2TextField.editText?.text.toString()

            if(email == null){
                val inputEmail:String = binding.emailTextField.editText?.text.toString()
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
        binding.emailTextField.editText?.setText("")
        binding.nameTextField.editText?.setText("")
        binding.lastnameTextField.editText?.setText("")
        binding.password1TextField.editText?.setText("")
        binding.password2TextField.editText?.setText("")
        binding.emailTextField.editText?.focusable
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