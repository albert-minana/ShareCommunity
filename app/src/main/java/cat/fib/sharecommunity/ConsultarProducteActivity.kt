package cat.fib.sharecommunity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cat.fib.sharecommunity.ui.ConsultarProducteFragment
import dagger.hilt.android.AndroidEntryPoint

/** Activity ConsultarProducte
 *
 *  Activitat encarregada de consultar la informació completa d'un producte
 *
 *  @constructor Crea l'Activity ConsultarProducte
 *  @author Daniel Cárdenas Rafael
 */
@AndroidEntryPoint
class ConsultarProducteActivity : AppCompatActivity() {

    /** Function onCreate
     *
     *  Funció encarregada de crear, configurar i mostrar el contingut de l'activitat mitjançant el Fragment ConsultarProducte
     *
     *  @param savedInstanceState
     *  @author Daniel Cárdenas Rafael
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultar_producte)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragment = ConsultarProducteFragment()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}
