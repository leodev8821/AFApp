# App AnotherForumApp

## Minimum requisites
* Debe contar de al menos tres actividades diferentes --> 3.0 pts

    La aplicación tiene tres actividades:

    * Main Activity --> Activity principal y sirve como inicio de sesión en la app.
    * PostsActivity --> Activity que se encarga de mostrar todos los post, crear y borrar post.
    * UserRegisterActivity --> Activity con la que se puede registrar un nuevo usuario.



* Debe poder pasar un Extra (parámetro) entre al menos dos actividades, recuperarlo y usarlo en la segunda Activity --> 0.5 pts

    En el MainActivity, si un usuario ingresa un email no registrado, y luego hace click en el boton de Registrar, el correo introducido se pasa por Extra hacia UserRegisterActivity, donde se emplea como un campo del formulario de creación de un nuevo usuario.

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

    * ##### Crear un nuevo Post #####
    ~~~
    /*
    * AlertDialog to create a new post
    */
    private fun newPostAlert()
    ~~~
    
    * ##### Al seleccionar un Post, con sus diferentes opciones #####
    ~~~
    /*
    * AlertDialog when a post is clicked
    */
    private fun showPostAlert(title: String, body: String, userPost:Int, postId: Int, position:Int)
    ~~~
    
    * ##### Al editar un Post #####
    ~~~
    /*
    * AlertDialog to edit a post
    */
    private fun editPost(postId: Int)
    ~~~
  


* Debe implementar un sistema de sesión, guardando al menos un valor en sesión mediante SharedPreferences. --> 0.5 pts

    En la sesión y mediante SharedPreferences se guardan los datos del Email del usuario, así como si está o no logeado.
    ~~~
    session.setUserLoginState(true)
    session.setUserLoginEmail(email)
    ~~~
    Con ellos es posible lograr que si el usuario esta logeado, inicie la aplicación con PostsActivity, y con el email guardar cada nuevo Post con el email del usuario que lo ha creado.



* Debe incluir una tabla en una base de datos (SQLite) para almacenar y gestionar datos relevantes para la aplicación. --> 2.0 pts

    Esta aplicación contiene 2 tablas realcionadas en una base de datos de SQLite, donde se almacena la información tanto de los Post, como de los usuarios:

    * ##### Tabla de los Usuarios #####
    ~~~
    object UserTable {
        const val TABLE_NAME = "UserTable"
        const val COLUMN_NAME_ID = "_id"
        const val COLUMN_NAME = "Name"
        const val COLUMN_LASTNAME = "Lastname"
        const val COLUMN_EMAIL = "Email"
        const val COLUMN_PASSWORD = "Password"
    ~~~

    * ##### Tabla de los Post #####
    ~~~
    object PostTable {
        const val TABLE_NAME = "PostTable"
        const val COLUMN_NAME_ID = "_id"
        const val COLUMN_TITLE = "Title"
        const val COLUMN_BODY = "Body"
        const val COLUMN_USER_POST = "UserPost"
        const val COLUMN_TAGS = "Tags"
        const val COLUMN_REACTIONS = "Reactions"
        const val COLUMN_DATE = "Date"
        const val COLUMN_LIKE = "Like"
        private const val TABLE_USER = UserTable.COLUMN_NAME_ID
    ~~~



* Debe realizar llamadas a un API Rest para obtener datos. --> 2.0 pts

  Se realiza una llamada a un API Rest *API_BASE_URL = "https://dummyjson.com/"* desde el cual se obtienen los datos usando Retrofit. Posteriormente estos datos se emplean para rellenar la base de datos de Post:

  ##### Método para obtener datos del API Rest #####
  ~~~
  // Fetch data from the API and fill the DB
  private fun fetchData()
    ~~~

  ##### Método para llenar la DB con datos del API Rest #####
  ~~~
  // Fill the DB with data from the API
  private fun fillDatabase(list:List<PostItemResponse>)
  ~~~
  


* Debe utilizar un RecyclerView para mostrar una lista de elementos, y capturar al menos un evento de clic en cada elemento con una función lambda. --> 1.5 pts

  Se emplea un RecyclerView en el layout de PostActivity, el cual muestra la lista de cada uno de los Post dentro de la DB. Cada uno contiene 2 eventos click:

  ##### Se inicializa el adapter con los datos necesarios para cargar el RecyclerView #####
  ~~~
  postList = postDAO.findAll()
  adapter = PostAdapter(postList, loggedEmail,{
  onPostClickListener(it)
  }, {
  onReactFABListener(it)
  })
  recyclerView = binding.recyclerView
  recyclerView.layoutManager = LinearLayoutManager(this)
  recyclerView.adapter = adapter
  ~~~
  
  Las funciones lambda ***onPostClickListener()*** y ***onReactFABListener()*** se emplean para mostrar un cuadro de diálogo con cada uno de los Post
  y para mostrar una reacción a cada Post, respectivamente.



* Mostrar un menú en la AppBar (barra superior) --> 0.5 pts

  Se crea un menú en la AppBar con opciones para: Hacer la Query al API Rest y llenar la BD, Cerrar sesión y Acerca del autor de la app:

  ~~~
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
  menuInflater.inflate(R.menu.main_menu, menu)
  return true
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
                    Toast.makeText(this, R.string.updatingPostTM, Toast.LENGTH_LONG).show()
                }
            }
            //Logout option
            logOutOpt->{
                isLogged = !isLogged
                session.setUserLoginState(isLogged)

                intent = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)

                Toast.makeText(this, R.string.logoutPostTM, Toast.LENGTH_LONG).show()
            }
            //About option
            aboutOpt ->{
                Toast.makeText(this, R.string.toastAbout, Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
  }
  ~~~
  


* Internacionalización (un idioma es suficiente) --> 0.5 pts

  Se aplica la internacionalización en cada uno de los apartados de texto de la aplicación, usando el archivo ***strings.xml***
  y su respectiva aplicación en cada uno de los elementos de texto:

  ##### Archivo strings.xml de la carpeta ***res*** #####
  ~~~
  <!-- For Toast Messages -->
  *English*
  <string name="updatingPostTM">Updating Database…"</string>
  
  *Spanish*
  <string name="updatingPostTM">Actualizando la Base de Datos…</string>
  ~~~
  
  ##### Ejemplo de aplicación de internacionalización #####
  ~~~
  Toast.makeText(this, R.string.updatingPostTM, Toast.LENGTH_LONG).show()
  ~~~



* Usar ViewBinding en vez de findViewById(resId: Int) --> 0.5 pts

  La referencia a cada elemento gráfico dentro de los Activity, se realiza empleando ViewBinding:

  ##### Empleo de ViewBinding #####
  ~~~
  *Declaración*
  private lateinit var binding: ActivityPostsBinding
  
  *Inicialización*
  binding = ActivityPostsBinding.inflate(layoutInflater)
  setContentView(binding.root)
  
  *Empleo en un elemento gráfico*
  newPostButton = binding.newPostFloatingActionButton
  ~~~
  


* Investigar y usar TextField de Material Design --> 0.5 pts

  En todos los campos de texto de la aplicación de emplean EditText de los TextField de Material Design:

  ##### Empleo de TextField #####
  ~~~
  *En el layout*
  <com.google.android.material.textfield.TextInputLayout
      android:id="@+id/titleTextField"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:layout_margin="5dp"
      android:hint="@string/titleTextField"
      style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox">

      <com.google.android.material.textfield.TextInputEditText
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:selectAllOnFocus="true"
          android:inputType="textCapWords" />
  </com.google.android.material.textfield.TextInputLayout>
  
  *Inicialización*
  //EditText from AlertDialog layout
  val titleEditText:EditText = bindingAlert.titleTextField.editText!!
  
  *Empleo en un elemento gráfico*
  val postTitle:String = titleEditText.text.toString()
  ~~~