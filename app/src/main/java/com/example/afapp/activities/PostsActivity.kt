package com.example.afapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.afapp.R
import com.example.afapp.adapters.PostAdapter
import com.example.afapp.data.PostItemResponse
import com.example.afapp.database.Post
import com.example.afapp.data.PostServiceAPI
import com.example.afapp.database.providers.PostDAO
import com.example.afapp.databinding.ActivityPostsBinding
import com.example.afapp.databinding.ItemPostBinding
import com.example.afapp.database.utils.RetrofitProvider
import com.example.afapp.database.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class PostsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_EMAIL = "USER_EMAIL"
    }

    private lateinit var binding: ActivityPostsBinding
    private lateinit var bindingItem: ItemPostBinding

    private lateinit var adapter: PostAdapter

    private lateinit var postDAO: PostDAO

    private var userLoged: String? = null

    private lateinit var session:SessionManager
    private var isLogged:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //If Post is empty, no data is showed
        postDAO = PostDAO(this)

        if(postDAO.find(1) == null){
            binding.progress.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            binding.emptyPlaceholder.visibility = View.VISIBLE
        }
        else{
            loadData()
        }

        initView()

    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)

        //Get the email from MainActivity
        userLoged = intent.getStringExtra(EXTRA_EMAIL)

        //Save the email in the session
        session = SessionManager(this)

        isLogged= session.getUserLoginState()

        //Load all the post if a user is logged
        if(isLogged){
            loadData()
        }
    }

    private fun onItemClickListener(it: Int) {

    }

    //Load the RecyclerView with data from DB
    private fun loadData(){

        binding.progress.visibility = View.VISIBLE

        adapter = PostAdapter() {
            onItemClickListener(it)
        }
        val recycler:RecyclerView = binding.recyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        if (postDAO.find(1) != null) {
            recycler.visibility = View.VISIBLE
            binding.emptyPlaceholder.visibility = View.GONE
            binding.progress.visibility = View.GONE
            adapter.updateItems(postDAO.findAll())
        } else {
            recycler.visibility = View.GONE
            binding.emptyPlaceholder.visibility = View.VISIBLE
        }
    }

    // Fetch data from the API and fill the DB
    private fun fetchData(){
        var postItemResponseList:List<PostItemResponse>
        val service: PostServiceAPI = RetrofitProvider.getRetrofit()

        // Make the Co-Routine to make the Query
        CoroutineScope(Dispatchers.IO).launch {

            // Background call
            val response = service.getAll()
            runOnUiThread{
                // Modify the UI
                binding.progress.visibility = View.GONE

                if (response.body() != null) {
                    Log.i("HTTP", "Respuesta correcta :)")
                    postItemResponseList = response.body()?.posts.orEmpty()
                    fillDatabase(postItemResponseList)
                } else {
                    postItemResponseList = listOf()
                    Log.i("HTTP", "respuesta erronea :(")
                }
            }
        }
    }

    // Fill the DB with data from the API
    private fun fillDatabase(list:List<PostItemResponse>){
        //Query to fill the database
        for(post in list){
            val date:Long = getCurrentDate()
            var tags = ""
            for (tag in post.tags){
                tags += "$tag, "
            }
            val newPost = Post(-1, post.title, post.body, 1, tags, post.reactions, date)
            postDAO.insert(newPost)
            Log.i("DATABASE","New post from API added, ${post.title}")
        }
    }

    // To listen the item selected in a menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            // Refresh option
            R.id.opt1 ->{
                if(isLogged){
                    // If DB is empty, fill with the data from API
                    if(postDAO.find(1) == null){
                        fetchData()
                    }
                    loadData()
                    Toast.makeText(this, "Actualizando Base de Datos", Toast.LENGTH_LONG).show()
                }
            }
            //Logout option
            R.id.opt2 ->{
                isLogged = !isLogged
                session.setUserLoginState(isLogged)

                intent = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)

                Toast.makeText(this, "Has cerrado sesión", Toast.LENGTH_LONG).show()
            }
            //About option
            R.id.opt3 ->{
                Toast.makeText(this, getString(R.string.toastAbout), Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun getCurrentDate():Long{
        return Calendar.getInstance().timeInMillis
    }

    // TO SHOW A CONFIRM EXIT DIALOG
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        showExitDialog()
    }

    private fun showExitDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setIcon(R.drawable.caution_svg)
            .setTitle("Cerrar Sesión")
            .setMessage("Esta seguro de que desea cerrar la sesión?")
            .setPositiveButton("Yes") { _, _ ->
                session.getUserLoginState()
                finish() }
            .setNegativeButton("No") { dialog, _ -> dialog?.cancel() }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}