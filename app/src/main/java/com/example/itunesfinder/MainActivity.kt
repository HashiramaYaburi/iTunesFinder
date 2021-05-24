package com.example.itunesfinder


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itunesfinder.databinding.ActivityMainBinding
import com.example.itunesfinder.model.HomeFeed
import com.example.itunesfinder.model.Item
import com.example.itunesfinder.recyclerview.ItemCallback
import com.example.itunesfinder.recyclerview.RecyclerViewAdapter
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity(), ItemCallback {

    private lateinit var binding: ActivityMainBinding

    var recyclerView: RecyclerView? = null
    var displayArrayList: ArrayList<Item?> = ArrayList()
    //index of last loaded data
    var offset = 0
    //multiplier for last loaded data
    var index = 1
    var isLoading = false
    //term of research on the API
    var term = "jack+johnson"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        recyclerView = binding.rvRecyclerView

        populateData(term)
        initListener()
        initAdapter()
    }


    private fun populateData(term: String) {
        //25 rows at times are loaded
        //off contains the information of which "page" are we visualizing
        val off = index * offset
        val url ="https://itunes.apple.com/search?term=$term&offset=$off&limit=25"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient();
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    Toast.makeText(applicationContext,"Could not load the data. Please check your connection and try again",Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                val gson = GsonBuilder().create()

                val homeFeed = gson.fromJson(body, HomeFeed::class.java)

                displayArrayList.addAll(homeFeed.results)

                runOnUiThread {
                    recyclerView!!.adapter!!.notifyDataSetChanged()
                }

            }
        })
    }

    private fun initAdapter() {
        val myAdapter = RecyclerViewAdapter(displayArrayList, this)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = myAdapter
    }

    private fun initScrollListener() {
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == displayArrayList.size - 1) {
                        offset += 25
                        loadMore()
                        index += 1
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore() {
        //while progressBar is showed an Handler manage the search for other elements
        val handler = Handler()
        handler.postDelayed(Runnable {
            populateData(term)
            isLoading = false
        }, 2500)
    }

    private fun initListener(){
        //initialize UI interactions

        val textInputLayout : TextInputLayout = findViewById(R.id.outlinedTextField)
        val editText : EditText = findViewById(R.id.editText)

        textInputLayout.setEndIconOnClickListener {
            term = editText.text.toString()
            offset = 0
            index = 1
            displayArrayList.clear()
            populateData(term)
        }

        initScrollListener()
    }

    override fun onItemClick(pos: Int, imgItem: ImageView?, artist: TextView?, track: TextView?) {
        val mItem = displayArrayList[pos]
        val gArtistName : String = mItem!!.artistName
        val gTrackName : String = mItem.trackName
        val gCollectionName : String = mItem.collectionName
        val gTrackPrice : String = mItem.trackPrice
        val gReleaseDate : String = mItem.releaseDate
        val gImage : String = mItem.artworkUrl100

        val intent = Intent(this, Detail::class.java)

        //info to show on the other Activity
        intent.putExtra("iArtistName", gArtistName)
        intent.putExtra("iTrackName", gTrackName)
        intent.putExtra("iCollectionName", gCollectionName)
        intent.putExtra("iTrackPrice", gTrackPrice)
        intent.putExtra("iReleaseDate", gReleaseDate)
        intent.putExtra("iImage", gImage)


        //Animations
        val p1 : Pair<View, String> = Pair.create(artist as View?, "artistTN")
        val p2 : Pair<View, String> = Pair.create(track as View?, "trackTN")
        val p3 : Pair<View, String> = Pair.create(imgItem as View?, "ImageTN")

        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2, p3)
        startActivity(intent,optionsCompat.toBundle());
    }

}
