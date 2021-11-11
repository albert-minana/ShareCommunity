package cat.fib.sharecommunity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cat.fib.sharecommunity.ui.FragmentConsultarColeccio
import dagger.hilt.android.AndroidEntryPoint


/** Activity ConsultarColeccio
 *
 *  Activitat encarregada de consultar la informació completa d'una col·lecció.
 *
 *  @constructor Crea l'Activity ConsultarColeccio
 *  @author Daniel Cárdenas
 */
@AndroidEntryPoint
class ConsultarColeccioActivity : AppCompatActivity()  {

    /** Function onCreate
     *
     *  Funció encarregada de crear, configurar i mostrar el contingut de l'activitat mitjançant el Fragment ConsultarColeccio
     *
     *  @param savedInstanceState
     *  @author Daniel Cárdenas
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultar_coleccio)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragment = FragmentConsultarColeccio()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}