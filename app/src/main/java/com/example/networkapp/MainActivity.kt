package com.example.networkapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException

// TODO (1: Fix any bugs)
// TODO (2: Add function saveComic(...) to save and load comic info automatically when app starts)

private const val COMIC_URL_KEY = "comic_url"
private const val DESCRIPTION_KEY = "description_url"
private const val TITLE_KEY = "title_comic"
class MainActivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var requestQueue: RequestQueue
    lateinit var titleTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var numberEditText: EditText
    lateinit var showButton: Button
    lateinit var comicImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferences = getPreferences(MODE_PRIVATE)
        requestQueue = Volley.newRequestQueue(this)

        titleTextView = findViewById<TextView>(R.id.comicTitleTextView)
        descriptionTextView = findViewById<TextView>(R.id.comicDescriptionTextView)
        numberEditText = findViewById<EditText>(R.id.comicNumberEditText)
        showButton = findViewById<Button>(R.id.showComicButton)
        comicImageView = findViewById<ImageView>(R.id.comicImageView)
        loadComic()
        showButton.setOnClickListener {
            downloadComic(numberEditText.text.toString())
        }


    }

    private fun downloadComic (comicId: String) {
        val url = "https://xkcd.com/$comicId/info.0.json"
        requestQueue.add (
            JsonObjectRequest(url, {showComic(it)}, {
            })
        )
    }

    private fun showComic (comicObject: JSONObject) {
        titleTextView.text = comicObject.getString("title")
        descriptionTextView.text = comicObject.getString("alt")
        Picasso.get().load(comicObject.getString("img")).into(comicImageView)
        saveComic(
            comicObject.getString("title"),
            comicObject.getString("img"),
            comicObject.getString("alt")
        )

    }
    private fun loadComic(){
        with(preferences){
            titleTextView.text = preferences.getString(TITLE_KEY, "empty")
            descriptionTextView.text = preferences.getString(DESCRIPTION_KEY, "empty")
            Picasso.get().load(preferences.getString(COMIC_URL_KEY, "empty")).into(comicImageView)
        }
    }

    private fun saveComic(comicTitle: String, comicURL: String, comicDescription: String){
        with(preferences.edit()){
            putString(TITLE_KEY, comicTitle)
            putString(COMIC_URL_KEY, comicURL)
            putString(DESCRIPTION_KEY, comicDescription)
        }
    }


}