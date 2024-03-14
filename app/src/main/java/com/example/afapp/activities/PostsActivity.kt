package com.example.afapp.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.afapp.R
import com.example.afapp.adapters.PostAdapter
import com.example.afapp.data.PostServiceAPI
import com.example.afapp.database.Post
import com.example.afapp.database.providers.PostDAO
import com.example.afapp.databinding.ActivityMainBinding
import com.example.afapp.databinding.ActivityPostsBinding
import com.example.afapp.utils.RetrofitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostsBinding

    private lateinit var adapter: PostAdapter

    private lateinit var postDAO: PostDAO
    private lateinit var postList:List<Post>
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
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        binding.progress.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.emptyPlaceholder.visibility = View.VISIBLE

        // TO LOAD INFO FROM API TO DB
        fillDataBase()
    }

    private fun onItemClickListener(it: Int) {

    }

    private fun fillDataBase(){
        postDAO = PostDAO(this)
        binding.progress.visibility = View.VISIBLE

        val service: PostServiceAPI = RetrofitProvider.getRetrofit()

        // Se hace la Co-Rutina para realizar la query
        CoroutineScope(
            Dispatchers.IO
        ).launch {

            // Llamada en segundo plano
            val response = service.getAll()

            runOnUiThread {
                binding.progress.visibility = View.GONE
                // Modificar UI
                if (response.body() != null) {
                    Log.i("HTTP", "Respuesta correcta :)")
                    postList = response.body()?.results.orEmpty()

                    for(post in postList){

                    }
                    adapter.updateItems(postList)

                    if (postList.isNotEmpty()) {
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

    // To listen the item selected in a menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.opt1 ->{
                Toast.makeText(this, "He pulsado Refresh", Toast.LENGTH_LONG).show()
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

}