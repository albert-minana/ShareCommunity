package cat.fib.sharecommunity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.fib.sharecommunity.ui.ConsultarRutinaPredefinidaFragment
import dagger.hilt.android.AndroidEntryPoint

/** Activity ConsultarRutinaPredefinida
 *
 *  Activitat encarregada de consultar la informació completa d'una rutina predefinida
 *
 *  @constructor Crea l'Activity ConsultarRutinaPredefinida
 *  @author Albert Miñana Montecino
 */
@AndroidEntryPoint
class ConsultarRutinaPredefinidaActivity : AppCompatActivity() {

    /** Function onCreate
     *
     *  Funció encarregada de crear, configurar i mostrar el contingut de l'activitat mitjançant el Fragment ConsultarRutinaPredefinida
     *
     *  @param savedInstanceState
     *  @author Albert Miñana Montecino
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultar_rutina_predefinida)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragment = ConsultarRutinaPredefinidaFragment()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}