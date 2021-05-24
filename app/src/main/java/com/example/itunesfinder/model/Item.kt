package com.example.itunesfinder.model

import java.io.Serializable

class Item(val artistName: String, val collectionName: String, val trackName: String, val artworkUrl100: String, val trackPrice: String, val releaseDate: String) :
    Serializable