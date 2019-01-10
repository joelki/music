package com.example.sandpickle.music

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.sandpickle.music.ApiFiles.Main2ActivityAPI
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_default.*

class defaultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_default)
        super.onCreate(savedInstanceState)
        fun String.toColor(): Int = Color.parseColor(this)
        screen.setBackgroundColor("#D3F0E2".toColor())
        office.setBackgroundColor("#2F8A5F".toColor())
        office.setTextColor(Color.WHITE)
        browse.setBackgroundColor("#D3F0E2".toColor())
        Picasso.get().load("https://contentpl-a.akamaihd.net/images/genre_moods/image/small/Compilation.jpg").fit().centerInside().into(playlist)
        Picasso.get().load(        "https://cdn2.iconfinder.com/data/icons/media-and-navigation-buttons-square/512/Button_15-512.png").fit().centerInside().into(browse)
        textView.setTextColor(Color.WHITE)
        playlist.setOnClickListener{
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }
        search.setHint("search here")
       // search.setBackgroundColor("#D3F0E2".toColor())
        browse.setOnClickListener{
            val searcher: String = search.text.toString()
            if(searcher.contains(" ")){
                searcher.replace(" ", "_")}
            val intent = Intent(this, Main2ActivityAPI::class.java)
            intent.putExtra("Text", searcher)
            intent.putExtra("theOffice", false)
            startActivity(intent)
        }
        office.setOnClickListener{
            val intent = Intent(this, Main2ActivityAPI::class.java)
            intent.putExtra("theOffice", true)
            intent.putExtra("officeVideoId", "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCa90xqK2odw1KV5wHU9WRhg&maxResults=25&key= ")//enter youtube api key after key=
            startActivity(intent)
        }
    }


}
