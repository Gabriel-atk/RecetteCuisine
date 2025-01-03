package tg.eplcoursandroid.recettecuisine

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where
import tg.eplcoursandroid.recettecuisine.models.Utilisateur

class InscriptionActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscription)

        // Initialiser Realm
        Realm.init(this)
        val realm = Realm.getDefaultInstance()

        // Liaison avec les vues
        usernameEditText = findViewById(R.id.fullNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        registerButton = findViewById(R.id.registerButton)

        // Désactiver le bouton au départ
        registerButton.isEnabled = false
        registerButton.setBackgroundColor(Color.GRAY)

        // TextWatcher pour activer le bouton
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateInputs()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
        confirmPasswordEditText.addTextChangedListener(textWatcher)

        // Gestion du clic sur le bouton "Valider"
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val username = usernameEditText.text.toString()

            if (password == confirmPassword) {
                // Vérifiez si l'utilisateur existe déjà
                val existingUser = realm.where<Utilisateur>()
                    .equalTo("email", email)
                    .findFirst()

                if (existingUser != null) {
                    Toast.makeText(this, "Cet email est déjà utilisé", Toast.LENGTH_SHORT).show()
                } else {
                    // Ajouter l'utilisateur dans Realm sur un thread d'arrière-plan

                    realm.executeTransactionAsync({ bgRealm ->
                        val newUser = bgRealm.createObject(Utilisateur::class.java, generateUserId(bgRealm))
                        newUser.email = email
                        newUser.nomUtilisateur = username
                        newUser.motDePasse = password
                    }, {
                        // Succès
                        Log.d("RealmTransaction", "Utilisateur ajouté avec succès")
                        runOnUiThread {
                            val utilisateurs = realm.where(Utilisateur::class.java).findAll()
                            Log.d("Register", "Utilisateurs: $utilisateurs")
                            Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show()

                            // Passer à la page de connexion
                            val intent = Intent(this, ConnexionActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }, { error ->
                        // Échec
                        Log.e("RealmTransaction", "Erreur lors de l'ajout : ${error.message}")
                        runOnUiThread {
                            Toast.makeText(this, "Une erreur s'est produite", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            } else {
                Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun validateInputs() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        if (isValidEmail(email) && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword) {
            registerButton.isEnabled = true
            registerButton.setBackgroundColor(Color.parseColor("#4CAF50"))
        } else {
            registerButton.isEnabled = false
            registerButton.setBackgroundColor(Color.GRAY)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun generateUserId(realm: Realm): Int {
        val maxId = realm.where<Utilisateur>().max("id")
        return if (maxId == null) 1 else maxId.toInt() + 1
    }

    override fun onDestroy() {
        super.onDestroy()
        Realm.getDefaultInstance().close()
    }
}