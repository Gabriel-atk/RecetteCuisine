package tg.eplcoursandroid.recettecuisine

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import io.realm.Realm
import tg.eplcoursandroid.recettecuisine.databinding.ActivityAjouterRectteBinding
import tg.eplcoursandroid.recettecuisine.models.Etape
import tg.eplcoursandroid.recettecuisine.models.Ingredient
import tg.eplcoursandroid.recettecuisine.models.Recette

class AjouterRecetteActivity : AppCompatActivity() {

    private lateinit var recipeImageView: ImageView
    private lateinit var recipeTitleEditText: EditText
    private lateinit var recipeDescriptionEditText: EditText
    private lateinit var recipeCategorySpinner: Spinner
    private lateinit var ingredientsCheckboxContainer: LinearLayout
    private lateinit var stepsContainer: LinearLayout
    private lateinit var addStepButton: Button
    private lateinit var saveRecipeButton: Button

    private var imagePath: String? = null
    lateinit var ui: ActivityAjouterRectteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        ui = ActivityAjouterRectteBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(ui.root)
        setSupportActionBar(ui.toolbar)

        // Initialiser Realm
        Realm.init(this)

        // Initialiser les vues
        recipeImageView = findViewById(R.id.recipeImageView)
        recipeTitleEditText = findViewById(R.id.recipeTitleEditText)
        recipeDescriptionEditText = findViewById(R.id.recipeDescriptionEditText)
        recipeCategorySpinner = findViewById(R.id.recipeCategorySpinner)
        ingredientsCheckboxContainer = findViewById(R.id.ingredientsCheckboxContainer)
        stepsContainer = findViewById(R.id.stepsContainer)
        addStepButton = findViewById(R.id.addStepButton)
        saveRecipeButton = findViewById(R.id.saveRecipeButton)

        // Charger les ingrédients disponibles depuis Realm
        loadIngredients()

        // Bouton pour ajouter une étape
        addStepButton.setOnClickListener { addStepField() }

        // Bouton pour choisir une image
        findViewById<Button>(R.id.chooseImageButton).setOnClickListener { chooseImage() }

        // Bouton pour enregistrer la recette
        saveRecipeButton.setOnClickListener { saveRecipe() }
    }

    private fun loadIngredients() {
        val realm = Realm.getDefaultInstance()
        val ingredients = realm.where(Ingredient::class.java).findAll()
        ingredients.forEach { ingredient ->
            val checkBox = CheckBox(this)
            checkBox.text = ingredient.nom
            checkBox.tag = ingredient.id
            ingredientsCheckboxContainer.addView(checkBox)
        }
        realm.close()
    }

    private fun addStepField() {
        val editText = EditText(this)
        editText.hint = "Description de l'étape"
        stepsContainer.addView(editText)
    }

    private fun chooseImage() {
        // Utiliser une activité de sélection d'image (par exemple, Intent.ACTION_PICK)
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        //startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            imagePath = data.data.toString()
            //Glide.with(this).load(imagePath).into(recipeImageView)
        }
    }

    private fun saveRecipe() {
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction { transactionRealm ->
            val recipeId = transactionRealm.where(Recette::class.java).max("id")?.toInt()?.plus(1) ?: 1
            val recette = transactionRealm.createObject(Recette::class.java, recipeId)
            recette.titre = recipeTitleEditText.text.toString()
            recette.description = recipeDescriptionEditText.text.toString()
            recette.categorie = recipeCategorySpinner.selectedItem.toString()
            recette.imagePath = imagePath ?: ""

            // Ajouter les ingrédients sélectionnés
            ingredientsCheckboxContainer.children.filterIsInstance<CheckBox>().forEach { checkBox ->
                if (checkBox.isChecked) {
                    val ingredientId = checkBox.tag as Int
                    val ingredient = transactionRealm.where(Ingredient::class.java).equalTo("id", ingredientId).findFirst()
                    if (ingredient != null) {
                        recette.ingredients.add(ingredient)
                    }
                }
            }

            // Ajouter les étapes
            stepsContainer.children.filterIsInstance<EditText>().forEach { editText ->
                val stepDescription = editText.text.toString()
                if (stepDescription.isNotBlank()) {
                    val stepId = transactionRealm.where(Etape::class.java).max("id")?.toInt()?.plus(1) ?: 1
                    val step = transactionRealm.createObject(Etape::class.java, stepId)
                    step.description = stepDescription
                    recette.etapes.add(step)
                }
            }
        }

        realm.close()
        Toast.makeText(this, "Recette ajoutée avec succès !", Toast.LENGTH_SHORT).show()
        finish()
    }
}
