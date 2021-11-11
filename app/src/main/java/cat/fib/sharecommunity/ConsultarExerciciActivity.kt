package cat.fib.sharecommunity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.fib.sharecommunity.ui.ConsultarExerciciFragment
import dagger.hilt.android.AndroidEntryPoint

/** Activity ConsultarExercici
 *
 *  Activitat encarregada de consultar la informació completa d'un exercici
 *
 *  @constructor Crea l'Activity ConsultarExercici
 *  @author Albert Miñana Montecino
 */
@AndroidEntryPoint
class ConsultarExerciciActivity : AppCompatActivity() {

    /** Function onCreate
     *
     *  Funció encarregada de crear, configurar i mostrar el contingut de l'activitat mitjançant el Fragment ConsultarExercici
     *
     *  @param savedInstanceState
     *  @author Albert Miñana Montecino
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultar_exercici)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragment = ConsultarExerciciFragment()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

}