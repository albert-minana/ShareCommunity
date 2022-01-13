package cat.fib.sharecommunity

import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri

import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import cat.fib.sharecommunity.dataclasses.Product
import cat.fib.sharecommunity.dataclasses.Resource
import cat.fib.sharecommunity.viewmodels.ProductViewModel
import cat.fib.sharecommunity.viewmodels.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_crear_perfil.*
import kotlinx.android.synthetic.main.activity_crear_perfil.CrearProducteButton
import kotlinx.android.synthetic.main.activity_crear_perfil.textInputLayoutNom
import kotlinx.android.synthetic.main.activity_crear_producte.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/** Classe CrearProducte
 *
 *  Classe on es crea un producte amb els camps principals necessaris.
 *
 *  @constructor Crea un producte amb tots els camps amb valor nul.
 *  @author Daniel Cárdenas.
 */
@AndroidEntryPoint
class CrearProducteActivity : AppCompatActivity() {
    //Our variables
    private var mImageView: ImageView? = null
    private var mUri: Uri? = null
    var Nom: EditText? = null
    var Descripcio: EditText? = null
    var Ubicacio: EditText? = null
    var Tipus_Roba: RadioButton? = null
    var Tipus_Material: RadioButton? = null
    var Tipus_Joguines: RadioButton? = null
    var Tipus_Altre: RadioButton? = null

    //Our widgets
    private lateinit var btnCapture: Button
    private lateinit var btnChoose : Button
    //Our constants
    private val OPERATION_CAPTURE_PHOTO = 1
    private val OPERATION_CHOOSE_PHOTO = 2

    private val viewModel by viewModels<ProductViewModel>()


    /** Funció inicialitzadora
     *
     *  Funció que fa que es mostri la pantalla a l'usuari i que inicialitza totes les variables.
     *
     *  @param savedInstanceState
     *  @author Daniel Cárdenas.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_producte)

        btnCapture = findViewById(R.id.btnCapture)
        btnChoose = findViewById(R.id.btnChoose)
        mImageView = findViewById(R.id.mImageView)

        Nom = findViewById(R.id.Nom)
        Descripcio = findViewById(R.id.Descripcio)
        Ubicacio = findViewById(R.id.Ubicacio)
        Tipus_Roba = findViewById(R.id.Tipus_Roba)
        Tipus_Material = findViewById(R.id.Tipus_Material)
        Tipus_Joguines = findViewById(R.id.Tipus_Joguines)
        Tipus_Altre = findViewById(R.id.Tipus_Altre)

        btnCapture.setOnClickListener{capturePhoto()}
        btnChoose.setOnClickListener{
            //check permission at runtime
            val checkSelfPermission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
                //Requests permissions to be granted to this application at runtime
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
            else{
                openGallery()
            }
        }
        setupSendButton()
    }

    /** Function setupSendButton
     *
     *  Funció que comprova si els camps són correctes per crear un producte.
     *
     *  @author Daniel Cárdenas Rafael
     */
    fun setupSendButton() {
        CrearProducteButton.setOnClickListener {
            val validateName = validateName()
            val validateDescription = validateDescription()
            val validateUbication = validateUbication()
            val validateType = validateType()
            if (validateName && validateDescription && validateUbication && validateType) {
                val name = Nom?.text.toString()
                val descripcio = Descripcio?.text.toString()
                val ubicacio = Ubicacio?.text.toString()
                var type: String
                when {
                    Tipus_Roba?.isChecked == true -> type = "Roba"
                    Tipus_Material?.isChecked == true -> type = "Material"
                    Tipus_Joguines?.isChecked == true -> type = "Joguina"
                    else -> type = "Altre"
                }
                val photo = mUri?.path.toString()


                val sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
                val publishDate = sdf.format(Date())

                val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                val userEmail = prefs.getString("email", null)
                val product = Product(name, descripcio, ubicacio, type, photo, publishDate, userEmail!!)
                viewModel.createProduct(product)
                viewModel.product.observe(this, Observer {
                    if (it.status == Resource.Status.SUCCESS) {
                        showHome()
                    }
                    else if (it.status == Resource.Status.ERROR) {
                        Toast.makeText(this, "ERROR!", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    /** Function validateName
     *
     *  Funció que comprova si el camp Nom és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Daniel Cárdenas Rafael
     */
    private fun validateName(): Boolean {
        val name = Nom?.text.toString()
        if (name.isEmpty()) {
            textInputLayoutNom?.setError("El camp no pot ser buit")
            return false
        }
        else {
            textInputLayoutNom?.setError(null)
            return true
        }
    }

    /** Function validateDescription
     *
     *  Funció que comprova si el camp Descripcio és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Daniel Cárdenas Rafael
     */
    private fun validateDescription(): Boolean {
        val descripcio = Descripcio?.text.toString()
        if (descripcio.isEmpty()) {
            textInputLayoutDescripcio?.setError("El camp no pot ser buit")
            return false
        }
        else {
            textInputLayoutDescripcio?.setError(null)
            return true
        }
    }

    /** Function validateUbication
     *
     *  Funció que comprova si el camp Ubicacio és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Daniel Cárdenas Rafael
     */
    private fun validateUbication(): Boolean {
        val ubicacio = Ubicacio?.text.toString()
        if (ubicacio.isEmpty()) {
            textInputLayoutUbicacio?.setError("El camp no pot ser buit")
            return false
        }
        else {
            textInputLayoutUbicacio?.setError(null)
            return true
        }
    }

    /** Function validateType
     *
     *  Funció que comprova si el camp Tipus és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Daniel Cárdenas Rafael
     */
    private fun validateType(): Boolean {
        val TypeNotChecked = (Tipus_Roba?.isChecked == false && Tipus_Material?.isChecked == false && Tipus_Joguines?.isChecked == false && Tipus_Altre?.isChecked == false)
        if (TypeNotChecked) {
            textInputLayoutType?.setError("El camp no està seleccionat")
            Tipus_Altre?.setError("El camp no està seleccionat")
            return false
        }
        else {
            textInputLayoutType?.setError(null)
            Tipus_Altre?.setError(null)
            return true
        }
    }

        private fun show(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    private fun capturePhoto(){
        val capturedImage = File(externalCacheDir, "My_Captured_Photo.jpg")
        if(capturedImage.exists()) {
            capturedImage.delete()
        }
        capturedImage.createNewFile()
        mUri = if(Build.VERSION.SDK_INT >= 24){
            FileProvider.getUriForFile(this, "cat.fib.sharecommunity.fileprovider",
                capturedImage)
        } else {
            Uri.fromFile(capturedImage)
        }

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)
    }

    private fun openGallery(){
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
    }

    private fun renderImage(imagePath: String?){
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            mImageView?.setImageBitmap(bitmap)
        }
        else {
            show("ImagePath is null")
        }
    }

    private fun getImagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        val cursor = contentResolver.query(uri!!, null, selection, null, null )
        if (cursor != null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }
    @TargetApi(19)
    private fun handleImageOnKitkat(data: Intent?) {
        var imagePath: String? = null
        val uri = data!!.data
        //DocumentsContract defines the contract between a documents provider and the platform.
        if (DocumentsContract.isDocumentUri(this, uri)){
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri?.authority){
                val id = docId.split(":")[1]
                val selsetion = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    selsetion)
            }
            else if ("com.android.providers.downloads.documents" == uri?.authority){
                val contentUri = ContentUris.withAppendedId(Uri.parse(
                    "content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
                imagePath = getImagePath(contentUri, null)
            }
        }
        else if ("content".equals(uri?.scheme, ignoreCase = true)){
            imagePath = getImagePath(uri, null)
        }
        else if ("file".equals(uri?.scheme, ignoreCase = true)){
            imagePath = uri?.path
        }
        mUri = imagePath?.toUri()
        renderImage(imagePath)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>
                                            , grantedResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantedResults)
        when(requestCode){
            1 ->
                if (grantedResults.isNotEmpty() && grantedResults.get(0) ==
                    PackageManager.PERMISSION_GRANTED){
                    openGallery()
                }else {
                    show("Unfortunately You are Denied Permission to Perform this Operataion.")
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            OPERATION_CAPTURE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(mUri!!)
                    )
                    mImageView!!.setImageBitmap(bitmap)
                }
            OPERATION_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitkat(data)
                    }
                }
        }
    }

    /** Function showHome
     *
     *  Funció encarregada de canviar a l'activitat principal.
     *
     *  @author Daniel Cárdenas Rafael
     */
    private fun showHome(){
        val homeIntent = Intent(this, MainActivity::class.java)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(homeIntent)
    }

}