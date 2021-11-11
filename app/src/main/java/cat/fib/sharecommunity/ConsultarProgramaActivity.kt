package cat.fib.sharecommunity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.fib.sharecommunity.ui.FragmentConsultarPrograma
import dagger.hilt.android.AndroidEntryPoint

/** Activity ConsultarPrograma
 *
 *  Activitat encarregada de consultar la informació completa d'un programa.
 *
 *  @constructor Crea l'Activity ConsultarPrograma
 *  @author Daniel Cárdenas
 */
@AndroidEntryPoint
class ConsultarProgramaActivity : AppCompatActivity() {

    /** Function onCreate
     *
     *  Funció encarregada de crear, configurar i mostrar el contingut de l'activitat mitjançant el Fragment ConsultarPrograma
     *
     *  @param savedInstanceState
     *  @author Daniel Cárdenas
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultar_programa)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragment = FragmentConsultarPrograma()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}