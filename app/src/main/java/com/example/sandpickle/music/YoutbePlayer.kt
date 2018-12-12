package com.example.sandpickle.music

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_youtbe_player.*

class YoutbePlayer : YouTubeBaseActivity() {
    companion object {
        var VIDEO_ID : String = ""
        var TITLE_ID: String = ""
        var URL_ID: String = ""
        val YOUTUBE_API_KEY : String = "AIzaSyA4x6FRqW7xyM6aVhWVK2KNcyibJbJoviA"
    }
    lateinit var youtubePlayerInit: YouTubePlayer.OnInitializedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtbe_player)
        screen.setBackgroundColor(Color.DKGRAY)
        btnPlaylist.setBackgroundColor(Color.DKGRAY)
        btnPlaylist.setTextColor(Color.WHITE)
        VIDEO_ID = intent.getStringExtra("videoId")
        TITLE_ID = intent.getStringExtra("titleId")
        URL_ID = intent.getStringExtra("urlId")
        val fromWhere = intent.getBooleanExtra("comingFromApi", true)


        initUI()
        if (fromWhere == true){
            btnPlaylist.setOnClickListener{
                val intent = Intent(this, CategoryActivity::class.java)
                intent.putExtra("videoId", VIDEO_ID)
                intent.putExtra("titleId",TITLE_ID)
                intent.putExtra("urlId",URL_ID)
                startActivity(intent)
            }
        }
        else{
            btnPlaylist.visibility = View.INVISIBLE
        }
        btnPlaylist.setOnClickListener{
            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra("videoId", VIDEO_ID)
            intent.putExtra("titleId",TITLE_ID)
            intent.putExtra("urlId",URL_ID)
            startActivity(intent)
        }
    }

    private fun initUI() {
        youtubePlayerInit = object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youtubePlayer: YouTubePlayer?, p2: Boolean) {
                youtubePlayer?.loadVideo(VIDEO_ID)
            }
            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                Toast.makeText(applicationContext, "Something went wrong!!", Toast.LENGTH_SHORT).show()
            }
        }
           youtubePlayer.initialize(YOUTUBE_API_KEY ,youtubePlayerInit)
    }
}
