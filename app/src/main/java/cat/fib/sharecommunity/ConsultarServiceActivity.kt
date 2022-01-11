package cat.fib.sharecommunity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cat.fib.sharecommunity.ui.ConsultarServiceFragment
import dagger.hilt.android.AndroidEntryPoint

/** Activity ConsultarService
 *
 *  Activitat encarregada de consultar la informació completa d'un servei
 *
 *  @constructor Crea l'Activity ConsultarProducte
 *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello Bayarri
 */
@AndroidEntryPoint
class ConsultarServiceActivity : AppCompatActivity() {

    /** Function onCreate
     *
     *  Funció encarregada de crear, configurar i mostrar el contingut de l'activitat mitjançant el Fragment ConsultarService
     *
     *  @param savedInstanceState
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello Bayarri
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultar_service)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragment = ConsultarServiceFragment()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}