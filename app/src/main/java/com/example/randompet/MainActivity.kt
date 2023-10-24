package com.example.randompet

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

data class NasaItem(val imageUrl: String, val title: String, val date: String)
class MainActivity : AppCompatActivity() {
    private lateinit var nasaList: MutableList<NasaItem>
    private lateinit var rvNasa: RecyclerView
    var astronomyphotourl = ""
    var currentPage = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvNasa = findViewById(R.id.nasa_list)
        nasaList = mutableListOf()

        val adapter = NasaAdapter(nasaList)
        rvNasa.adapter = adapter
        rvNasa.layoutManager = LinearLayoutManager(this@MainActivity)

        // Load initial data
        getnasaphoto()

        rvNasa.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (totalItemCount <= lastVisibleItem + 2) { // Load more when reaching the second last item
                    getnasaphoto()
                }
            }
        })

    }

    private fun getnasaphoto() {
        val apikey = "dfI2HLc5Ualx533RaMAiwVJdTP4TT6pZlnLx1cGR"
        val client = AsyncHttpClient()

        client["https://images-api.nasa.gov/search?q=moon&page=$currentPage", object :
            JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("DEBUG OBJECT", json.jsonObject.toString())

                val collection = json.jsonObject.getJSONObject("collection")
                val items = collection.getJSONArray("items")
                val newImages = mutableListOf<NasaItem>()
                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    var href: String? = null
                    if (item.has("links")) {  // Check if the key "links" exists
                        val links = item.getJSONArray("links")
                        for (j in 0 until links.length()) {
                            val linkObj = links.getJSONObject(j)
                            if (linkObj.getString("rel") == "preview") {
                                href = linkObj.getString("href")
                                break
                            }
                        }
                    }


                    val data = item.getJSONArray("data").getJSONObject(0)
                    val title = data.getString("title")
                    val dateCreated = data.getString("date_created")

                    if (href != null) {
                        val nasaItem = NasaItem(href, title, dateCreated)
                        newImages.add(nasaItem)
                    }
                }


                nasaList.addAll(newImages)
                rvNasa.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String,
                throwable: Throwable?
            ) {
                Log.e("API Request", "Failed with status code $statusCode")
            }
        }]

        // Increment the page number for the next API call
        currentPage++
    }

//        val button = findViewById<Button>(R.id.button)
//        val image = findViewById<ImageView>(R.id.imageView)
//        val titleView = findViewById<TextView>(R.id.textView2)
//        val dateView = findViewById<TextView>(R.id.textView3)
//        getNextImage(button,image,titleView,dateView)
//        Log.d("astronomyphotourl", "pet image URL set")
//    }

//    private fun getNextImage(button: Button, imageView: ImageView, titleView: TextView, dateView: TextView) {
//        button.setOnClickListener {
//            currentPage++
//            getnasaphoto { astronomyphotourl, astro_title, astro_date ->
//                Log.d("AstronomyPhotoURL", astronomyphotourl)
//                Glide.with(this)
//                    .load(astronomyphotourl)
//                    .override(100, 100)
//                    .centerCrop()
//                    .fitCenter()
//                    .into(imageView)
//
//                titleView.text = astro_title
//                dateView.text = astro_date
//
//
//            }
//        }

//    }



//    private fun getnasaphoto(){
//        val apikey = "dfI2HLc5Ualx533RaMAiwVJdTP4TT6pZlnLx1cGR"
//        val client = AsyncHttpClient()
//
//
//
////        client["https://api.nasa.gov/planetary/apod?api_key=$apikey", object :
////            JsonHttpResponseHandler() {
////            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
////
////                // Access a JSON object response with `json.jsonObject`
////                Log.d("DEBUG OBJECT", json.jsonObject.toString())
////                val astronomyphotourl = json.jsonObject.getString("url")
////                val astro_title = json.jsonObject.getString("title")
////                val astro_date = json.jsonObject.getString("date")
////                onSuccess(astronomyphotourl, astro_title, astro_date)
////            }
//        client["https://images-api.nasa.gov/search?q=moon&page=$currentPage", object :
//            JsonHttpResponseHandler() {
//            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
//
//                // Access a JSON object response with `json.jsonObject`
//                Log.d("DEBUG OBJECT", json.jsonObject.toString())
//                val nasaImageArray = json.jsonObject.getJSONArray("message")
//                for (i in 0 until nasaImageArray.length()) {
//                    nasaList.add(nasaImageArray.getString(i))
//                }
//
//                val adapter = NasaAdapter(nasaList)
//                rvNasa.adapter = adapter
//                rvNasa.layoutManager = LinearLayoutManager(this@MainActivity)
//
//                val collection = json.jsonObject.getJSONObject("collection")
//                val items = collection.getJSONArray("items")
//                val newImages = mutableListOf<String>()
//                for (i in 0 until items.length()) {
//                    val item = items.getJSONObject(i)
//                    val links = item.getJSONArray("links")
//                    for (j in 0 until links.length()) {
//                        val linkObj = links.getJSONObject(j)
//                        if (linkObj.getString("rel") == "preview") {
//                            newImages.add(linkObj.getString("href"))
//                            break
//                        }
//                    }
//                }
//
//
//                nasaList.addAll(newImages)
//                rvNasa.adapter?.notifyDataSetChanged()
//                // Assuming the first item in the collection is what you want
////                val collection = json.jsonObject.getJSONObject("collection")
////                val items = collection.getJSONArray("items")
////                val firstItem = items.getJSONObject(0)
////
////                val data = firstItem.getJSONArray("data").getJSONObject(0)
////                val title = data.getString("title")
////                val dateCreated = data.getString("date_created")
////
////                val links = firstItem.getJSONArray("links")
////                var href = ""
////                for (i in 0 until links.length()) {
////                    val linkObj = links.getJSONObject(i)
////                    if (linkObj.getString("rel") == "preview") {
////                        href = linkObj.getString("href")
////                        break
////                    }
////                }
////
////                onSuccess(href,title, dateCreated)
//            }
//            override fun onFailure(
//                statusCode: Int,
//                headers: Headers?,
//                response: String,
//                throwable: Throwable?
//            ) {
//                Log.e("API Request", "Failed with status code $statusCode")
//            }
//        }]
//    }

}
