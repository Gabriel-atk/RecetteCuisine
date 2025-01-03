package tg.eplcoursandroid.recettecuisine

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.realm.Realm
import io.realm.RealmConfiguration
import tg.eplcoursandroid.recettecuisine.models.Utilisateur
import java.io.File

class ConnexionActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLinkTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)

        // Initialiser Realm
        Realm.init(this)
        val realm = Realm.getDefaultInstance()

        // Liaison avec les vues
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerLinkTextView = findViewById<TextView>(R.id.registerLinkTextView)

        // Désactiver le bouton de connexion au départ
        loginButton.isEnabled = false
        loginButton.setBackgroundColor(Color.GRAY)

        // Activer le bouton de connexion si les champs sont valides
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateInputs()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        usernameEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)

        // Gestion du clic sur le bouton "Se connecter"
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val user = realm.where(Utilisateur::class.java)
                .equalTo("nomUtilisateur", username)
                .equalTo("motDePasse", password)
                .findFirst()

            if (user != null) {
                Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()

                // Passer à la page principale
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show()
            }
        }
        // Gestion du clic sur le lien d'inscription
        registerLinkTextView.setOnClickListener {
            val intent = Intent(this, InscriptionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInputs() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if ( username.isNotEmpty() && password.isNotEmpty()) {
            loginButton.isEnabled = true
            loginButton.setBackgroundColor(Color.parseColor("#4CAF50"))
        } else {
            loginButton.isEnabled = false
            loginButton.setBackgroundColor(Color.GRAY)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Realm.getDefaultInstance().close()
    }
}
