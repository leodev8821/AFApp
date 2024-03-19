# App AnotherForumApp

## Minimum requisites
* Debe contar de al menos tres actividades diferentes --> 3.0 pts
La aplicación tiene tres actividades:
**    Main Activity --> Activity principal y sirve como inicio de sesión en la app.
**    PostsActivity --> Activity que se encarga de mostrar todos los post, crear y borrar post.
**    UserRegisterActivity --> Activity con la que se puede registrar un nuevo usuario.

* Debe poder pasar un Extra (parámetro) entre al menos dos actividades, recuperarlo y usarlo en la segunda Activity --> 0.5 pts
En el MainActivity, si un usuario ingresa un email no registrado, y luego hace click en el boton de Registrar, el correo introducido
se pasa por Extra hacia UserRegisterActivity, donde se emplea como un campo del formulario de creación de un nuevo usuario.

##### MainActivity #####
~~~
val email:String = emailEditText.text.toString()
val intent = Intent(this, UserRegisterActivity::class.java)
intent.putExtra(UserRegisterActivity.EXTRA_EMAIL, email)
startActivity(intent)
~~~

##### UserRegisterActivity #####
~~~
//Get the email from MainActivity
email = intent.getStringExtra(EXTRA_EMAIL)

// To focus Name EditText if email != null
if (email != null){
    emailEditText.setText(email)
    nameEditText.requestFocus()
}
~~~

* Debe mostrar al menos un diálogo en respuesta a una acción del usuario --> 0-5 pts
En PostsActivity se usan diálogos para:

##### Crear un nuevo Post #####
~~~
/*
* AlertDialog to create a new post
*/
private fun newPostAlert()
~~~

##### Al seleccionar un Post, con sus diferentes opciones #####
~~~
/*
* AlertDialog when a post is clicked
*/
private fun showPostAlert(title: String, body: String, userPost:Int, postId: Int, position:Int)
~~~

##### Al editar un Post #####
~~~
/*
* AlertDialog to edit a post
*/
private fun editPost(postId: Int)
~~~