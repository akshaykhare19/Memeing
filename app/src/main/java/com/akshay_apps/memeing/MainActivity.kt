package com.akshay_apps.memeing

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    var englishMeme: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadEnglishMeme()

    }


    private fun loadEnglishMeme(){
        // Instantiate the RequestQueue.

        val loader: ProgressBar = findViewById(R.id.progressBar)
        loader.visibility = View.VISIBLE

        val url = "https://meme-api.herokuapp.com/gimme"
        val memeImage: ImageView = findViewById(R.id.meme)

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    englishMeme = response.getString("url")
                    Glide.with(this).load(englishMeme).listener(object: RequestListener<Drawable> {

                        override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?, target: Target<Drawable>?,
                                isFirstResource: Boolean): Boolean {
                            loader.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                                resource: Drawable?, model: Any?,
                                target: Target<Drawable>?, dataSource: DataSource?,
                                isFirstResource: Boolean): Boolean {
                            loader.visibility = View.GONE
                            return false
                        }
                    }).into(memeImage)
                },
                {
                    Toast.makeText(this, "Something went wrong!!!", Toast.LENGTH_LONG).show()
                })

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    fun nextMeme(view: View) {
        loadEnglishMeme()
    }

    fun shareMeme(view: View) {
        val intent: Intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey guys, checkout this meme I found on this amazing app Memeing!!! $englishMeme")

        val chooser = Intent.createChooser(intent, "Share this meme using....")
        startActivity(chooser)

    }
}