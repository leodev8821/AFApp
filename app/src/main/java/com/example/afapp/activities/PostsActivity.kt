package com.example.afapp.activities

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.afapp.R
import com.example.afapp.adapters.PostAdapter
import com.example.afapp.data.PostItemResponse
import com.example.afapp.database.Post
import com.example.afapp.data.PostServiceAPI
import com.example.afapp.database.providers.PostDAO
import com.example.afapp.database.utils.DBManager
import com.example.afapp.databinding.ActivityPostsBinding
import com.example.afapp.databinding.ItemPostBinding
import com.example.afapp.utils.RetrofitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class PostsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostsBinding
    private lateinit var bindingItem: ItemPostBinding

    private lateinit var adapter: PostAdapter

    private lateinit var postDAO: PostDAO
    private lateinit var postItemResponseList:List<PostItemResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)

        // Show Back Button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = PostAdapter() {
            onItemClickListener(it)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.progress.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.emptyPlaceholder.visibility = View.VISIBLE

    }

    private fun onItemClickListener(it: Int) {

    }

    private fun fillRecyclerView(){
        postDAO = PostDAO(this)
        binding.progress.visibility = View.VISIBLE

        val service: PostServiceAPI = RetrofitProvider.getRetrofit()

        // Se hace la Co-Rutina para realizar la query
        CoroutineScope(Dispatchers.IO).launch {

            // Llamada en segundo plano
            val response = service.getAll()
            runOnUiThread {
                binding.progress.visibility = View.GONE
                // Modificar UI
                if (response.body() != null) {
                    Log.i("HTTP", "Respuesta correcta :)")
                    postItemResponseList = response.body()?.posts.orEmpty()

                    if(postDAO.find(1) == null){
                        fillDatabase(postDAO, postItemResponseList)
                    }

                    adapter.updateItems(postDAO.findAll())

                    if (postItemResponseList.isNotEmpty()) {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.emptyPlaceholder.visibility = View.GONE
                    } else {
                        binding.recyclerView.visibility = View.GONE
                        binding.emptyPlaceholder.visibility = View.VISIBLE
                    }
                } else {
                    Log.i("HTTP", "respuesta erronea :(")
                }
            }
        }

    }

    private fun fillDatabase(dao:PostDAO, list:List<PostItemResponse>){
        //Query to fill the database
        for(post in list){
            val date:Long = getCurrentDate()
            var tags = ""
            for (tag in post.tags){
                tags += "$tag, "
            }
            val newPost = Post(-1, post.title, post.body, 1, tags, post.reactions, date)
            dao.insert(newPost)
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
            R.id.opt1 ->{
                // TO LOAD INFO FROM API TO DB AND RECYCLERVIEW
                fillRecyclerView()
                Toast.makeText(this, "Actualizando Base de Datos", Toast.LENGTH_LONG).show()
            }
            R.id.opt2 ->{
                Toast.makeText(this, "He pulsado Logout", Toast.LENGTH_LONG).show()
            }
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

    private fun setCurrentDate(){
        bindingItem = ItemPostBinding.inflate(layoutInflater)
        val calendar = getCurrentDate()
        val dateFormat = DateFormat.format("dd-MMMM-yyyy", calendar)
        bindingItem.dateItemtextView.text = dateFormat
    }

    private fun getCurrentDate():Long{
        return Calendar.getInstance().timeInMillis
    }

}