package com.example.itunesfinder

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*


class Detail : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        supportActionBar!!.hide()



        //collecting intent parameters
        val intent = intent
        val aArtistName = intent.getStringExtra("iArtistName")
        val aTrackName = intent.getStringExtra("iTrackName")
        val aCollectionName = intent.getStringExtra("iCollectionName")
        val aTrackPrice = intent.getStringExtra("iTrackPrice")
        val aReleaseDate = intent.getStringExtra("iReleaseDate")
        val aImage = intent.getStringExtra("iImage")

        //show data into layout
        showData(aArtistName, aTrackName, aCollectionName, aTrackPrice, aReleaseDate, aImage)
    }

    @SuppressLint("NewApi")
    private fun showData(
        artist: String,
        track: String,
        collection: String,
        price: String,
        release: String,
        aImage: String
    ){
        val artistName : TextView = findViewById(R.id.artist_name)
        artistName.text = artist
        val trackName : TextView = findViewById(R.id.track_name)
        trackName.text = track
        val collectionName : TextView = findViewById(R.id.collection)
        collectionName.text = collection
        val priceValue : TextView = findViewById(R.id.price)
        priceValue.text = "$price USD"
        val releaseDate : TextView = findViewById(R.id.release)
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date: Date = inputFormat.parse(release)
        releaseDate.text = date.toString()
        val image : ImageView = findViewById(R.id.ImageView)
        Glide.with(this).load(aImage).circleCrop().into(image)

    }
}