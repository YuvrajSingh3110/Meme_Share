package com.example.memeshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {

    private var currentImageUrl: String? = null
    private val apiUrl: String = "https://meme-api.com/gimme/memes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
        val shareButton = findViewById<Button>(R.id.shareButton)
        val nextButton = findViewById<Button>(R.id.nextButton)

        shareButton.setOnClickListener{
            shareMeme()
        }

        nextButton.setOnClickListener{
            loadMeme()
        }

    }

    private fun loadMeme() {
        // Instantiate the RequestQueue.
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(apiUrl,
            { response ->
                currentImageUrl = response.getString("url")
                val memeImage = findViewById<ImageView>(R.id.memeImage)
                progressBar.visibility = View.INVISIBLE
                Glide.with(this).load(currentImageUrl).into(memeImage)
            },
            {
                 Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
             })
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)

    }

    private fun shareMeme() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, check this out $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share this meme using...")
        startActivity(chooser)
    }
}
