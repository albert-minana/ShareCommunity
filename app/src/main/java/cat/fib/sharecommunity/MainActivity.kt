package cat.fib.sharecommunity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity()  {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        setNameAndEmail()

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_products,  R.id.nav_services, R.id.nav_personalspace), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setupLogOutButton()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /** Function setNameAndEmail
     *
     *  Funci?? encarregada d'establir el nom i el correu en la cap??alera del men?? desplegable.
     *
     *  @author Albert Mi??ana Montecino i Adri?? Espinola Garcia
     */
    public fun setNameAndEmail() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navView.getHeaderView(0)
        val userName: TextView = headerView.findViewById(R.id.textViewName)
        val userEmail: TextView = headerView.findViewById(R.id.textViewEmail)

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        userName.text = prefs.getString("name", null)
        userEmail.text = prefs.getString("email", null)
    }

    /** Function setupLogOutButton
     *
     *  Funci?? encarregada de configurar el bot?? de tancar sessi??.
     *
     *  @author Albert Mi??ana Montecino i Adri?? Espinola Garcia
     */
    private fun setupLogOutButton() {
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        navigationView.menu!!.findItem(R.id.nav_logout).setOnMenuItemClickListener { menuItem: MenuItem? ->
            logOut()
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            true
        }
    }

    /** Function logOut
     *
     *  Funci?? encarregada d'eliminar les dades de la sessi?? i canviar a l'activitat d'autenticaci??.
     *
     *  @author Albert Mi??ana Montecino i Adri?? Espinola Garcia
     */
    private fun logOut() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()
        val authenticationIntent = Intent(applicationContext, AuthenticationProviders::class.java)
        authenticationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(authenticationIntent)
    }

    public fun showCreateProduct(view: View) {
        val createProductIntent = Intent(applicationContext, CrearProducteActivity::class.java)
        startActivity(createProductIntent)
    }

    public fun showCreateService(view: View) {
        val createServiceIntent = Intent(applicationContext, CrearServiceActivity::class.java)
        startActivity(createServiceIntent)

    }


}