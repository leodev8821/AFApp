package com.example.afapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
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
import com.example.afapp.database.User
import com.example.afapp.database.providers.PostDAO
import com.example.afapp.database.providers.UserDAO
import com.example.afapp.databinding.ActivityPostsBinding
import com.example.afapp.database.utils.RetrofitProvider
import com.example.afapp.database.utils.SessionManager
import com.example.afapp.databinding.ItemPostBinding
import com.example.afapp.databinding.NewPostAlertDialogBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.random.Random

class PostsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostsBinding
    private lateinit var bindingAlert:NewPostAlertDialogBinding
    private lateinit var bindingItemPost: ItemPostBinding

    private lateinit var progress:FrameLayout
    private lateinit var recyclerView:RecyclerView
    private lateinit var emptyPlaceholder:LinearLayout
    private lateinit var newPostButton:FloatingActionButton

    private lateinit var postList:List<Post>
    private lateinit var newPost:Post

    private lateinit var adapter: PostAdapter

    private lateinit var postDAO: PostDAO
    private lateinit var userDAO: UserDAO

    private lateinit var session:SessionManager
    private var isLogged:Boolean = false
    private var loggedEmail:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

    }

    private fun initView() {
        binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDAO = UserDAO(this)
        postDAO = PostDAO(this)

        progress = binding.progress
        emptyPlaceholder = binding.emptyPlaceholder
        newPostButton = binding.newPostFloatingActionButton

        //Save the email in the session
        session = SessionManager(this)
        isLogged= session.getUserLoginState()
        loggedEmail = session.getUserLoginEmail().toString()

        Log.i("LOGGED_EMAIL", loggedEmail)

        postList = postDAO.findAll()
        adapter = PostAdapter(postList, loggedEmail,{
            onPostClickListener(it)
        }, {
            onReactFABListener(it)
        })
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //If Post is empty, no data is showed
        if(postDAO.find(1) == null){
            progress.visibility = View.GONE
            recyclerView.visibility = View.GONE
            emptyPlaceholder.visibility = View.VISIBLE
        }
        else{
            loadData()
        }

        setSupportActionBar(binding.toolbar)

        //Load all the post if a user is logged
        if(isLogged){
            loadData()
        }

        newPostButton.setOnClickListener{
            newPostAlert()
        }
    }

    //Load the RecyclerView with data from DB
    private fun loadData(){
        postList = postDAO.findAll()
        progress.visibility = View.VISIBLE

        if (postDAO.find(1) != null) {
            recyclerView.visibility = View.VISIBLE
            emptyPlaceholder.visibility = View.GONE
            progress.visibility = View.GONE
            adapter.updateItems(postList)
        } else {
            recyclerView.visibility = View.GONE
            emptyPlaceholder.visibility = View.VISIBLE
        }
    }

    /*
    * AlertDialog to create a new post
     */
    private fun newPostAlert() {
        //Inflate the AlertDialog layout
        bindingAlert = NewPostAlertDialogBinding.inflate(layoutInflater)

        //EditText from AlertDialog layout
        val titleEditText:EditText = bindingAlert.titleTextField.editText!!
        val bodyEditText:EditText = bindingAlert.bodyTextField.editText!!
        val tagsEditText:EditText = bindingAlert.tagsEditText.editText!!

        //Create AlertDialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle("Add a new Post")
            .setView(bindingAlert.root)
            .setPositiveButton("Add", null)
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss()}

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()

        // Need to move listener after show dialog to prevent dismiss
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val postTitle:String = titleEditText.text.toString()
            val postBody:String = bodyEditText.text.toString()
            val postTags:String = tagsEditText.text.toString()

            if (postTitle.isNotEmpty() && postBody.isNotEmpty() && postTags.isNotEmpty()){
                newPost(postTitle,postBody,postTags)
                loadData()
                Toast.makeText(this, "New post added!", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }else{
                Toast.makeText(this, "No new post was added!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
    * Method to create a new post into DB
     */
    private fun newPost(postTitle: String, postBody: String, postTags: String) {
        //Get the user from the DB
        val emailUser:User? = userDAO.find(loggedEmail)

        //Get the current Date
        val date:Long = getCurrentDate()

        //Generate a random number for Reactions
        val reactions:Int = Random.nextInt(1,100)

        //Save the new Post in the DB
        newPost = Post(-1,postTitle, postBody, emailUser!!.id, postTags, reactions, date)
        postDAO.insert(newPost)
    }

    private fun onReactFABListener(position: Int){
        var reactFAB:Boolean
        reactFAB = true
        val post:Post = postList[position]
        val reaction:Int

        if(reactFAB){
            reaction = 1
            reactionsController(post,reactFAB, reaction, position)
            Toast.makeText(this, "Like +1", Toast.LENGTH_LONG).show()
        }else{
            reaction = -1
            reactionsController(post,reactFAB, reaction, position)
            Toast.makeText(this, "Unlike -1", Toast.LENGTH_LONG).show()
        }
        loadData()
    }

    private fun reactionsController(post:Post,reactFAB:Boolean, reaction:Int, position: Int){
        bindingItemPost = ItemPostBinding.inflate(layoutInflater)
        val postReaction:Int = post.reactions
        post.reactions = postReaction + reaction
        postDAO.update(post)
        adapter.notifyItemChanged(position)

        val favDrawableId = if (reactFAB) {
            R.drawable.heart_selected
        } else {
            R.drawable.favorite_svg
        }
        bindingItemPost.reactFAB.setImageResource(favDrawableId)
    }

    private fun onPostClickListener(position: Int) {
        val post:Post = postList[position]
        showPostAlert(post.title, post.body, post.reactions, post.userPost, post.id, position)

    }

    private fun showPostAlert(title: String, body: String, reactions: Int, userPost:Int, postId: Int, position:Int) {
        val user: User? = userDAO.findById(userPost)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle(title)
            .setMessage(
                "Body: $body "+
                "\nReactions: $reactions "
            )
        if (user != null) {
            if(session.getUserLoginEmail() == user.email){
                builder.setPositiveButton("Delete") { _, _ -> deletePost(position)}
                builder.setNeutralButton("Edit") {_, _ -> editPost(postId)}
                builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss()}

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }else{
                builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss()}
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        } else{
            Toast.makeText(this, "Editar Post de la position $postId", Toast.LENGTH_LONG).show()
        }
    }

    private fun editPost(postId: Int) {
        val post: Post? = postDAO.find(postId)

        //Inflate the AlertDialog layout
        bindingAlert = NewPostAlertDialogBinding.inflate(layoutInflater)

        //EditText from AlertDialog layout
        val titleEditText:EditText = bindingAlert.titleTextField.editText!!
        val bodyEditText:EditText = bindingAlert.bodyTextField.editText!!
        val tagsEditText:EditText = bindingAlert.tagsEditText.editText!!

        //Set the fields EditText with data from DB
        if (post != null) {
            titleEditText.setText(post.title)
            bodyEditText.setText(post.body)
            tagsEditText.setText(post.tags)
            Log.i("POST","${post.title}, ${post.tags}")

            //Create AlertDialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder
                .setTitle("Edit Post ${post.title}")
                .setView(bindingAlert.root)
                .setPositiveButton("Edit", null)
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss()}

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()

            // Need to move listener after show dialog to prevent dismiss
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val postTitle:String = titleEditText.text.toString()
                val postBody:String = bodyEditText.text.toString()
                val postTags:String = tagsEditText.text.toString()

                if (postTitle.isNotEmpty() && postBody.isNotEmpty() && postTags.isNotEmpty()){
                    post.title = postTitle
                    post.body = postBody
                    post.tags = postTags
                    post.date = getCurrentDate()
                    Log.i("POST UPDATED","${post.title}, ${post.tags}")
                    postDAO.update(post)
                    adapter.notifyItemChanged(postId)
                    loadData()
                    Toast.makeText(this, "Successful post edited!", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }else{
                    Toast.makeText(this, "Unsuccessful post edited!", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this, "Post Error. This post doesn't exist!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deletePost(position:Int){
        val post:Post = postList[position]
        postDAO.delete(post)
        loadData()
        Toast.makeText(this, "El post ${post.title} ha sido borrado!", Toast.LENGTH_LONG).show()
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
                progress.visibility = View.GONE

                if (response.body() != null) {
                    Log.i("HTTP", "Respuesta correcta :)")
                    postItemResponseList = response.body()?.posts.orEmpty()
                    fillDatabase(postItemResponseList)
                    loadData()
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
        val refreshOpt:Int = R.id.opt1
        val logOutOpt:Int = R.id.opt2
        val aboutOpt:Int = R.id.opt3

        when (item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            // Refresh option
            refreshOpt ->{
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
            logOutOpt->{
                isLogged = !isLogged
                session.setUserLoginState(isLogged)

                intent = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)

                Toast.makeText(this, "Has cerrado sesión", Toast.LENGTH_LONG).show()
            }
            //About option
            aboutOpt ->{
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
                isLogged = !isLogged
                session.setUserLoginState(isLogged)
                finish() }
            .setNegativeButton("No") { dialog, _ -> dialog?.cancel() }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}