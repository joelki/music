package com.example.sandpickle.music.ApiFiles

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.sandpickle.music.R
import com.example.sandpickle.music.YoutbePlayer
import com.squareup.picasso.Picasso
import java.util.*

class Main2ActivityAPI : AppCompatActivity() {

    lateinit var videoIdList: ArrayList<Measurement>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_api)
        var officeOrNot = intent.getBooleanExtra("theOffice", false)
        if (officeOrNot == true) {
            var theOfficeText = intent.getStringExtra("officeVideoId")
            object : Thread() {
                override fun run() {
                    val resultList = QueryUtilsOffice.fetchAirQualityData(theOfficeText)
                    if (resultList != null) {
                        videoIdList = resultList
                    }
                }
            }.start()
            fun Random.nextInt(range: IntRange): Int {
                return range.start + nextInt(range.last - range.start)
            }

            Handler().postDelayed({
                val random = Random()
                val randomNumber = random.nextInt(0..45)
                val intent = Intent(this, YoutbePlayer::class.java)
                intent.putExtra("videoId", videoIdList[randomNumber].videoId)
                intent.putExtra("titleId", videoIdList[randomNumber].titleId)
                intent.putExtra("urlId", videoIdList[randomNumber].imageURL)
                intent.putExtra("comingFromApi", true)
                startActivity(intent)
            }, 5500) //keeping it longer to play it safe
    }
        else{
            var searchText = intent.getStringExtra("Text")
            object : Thread() {
                override fun run() {
                    val resultList = QueryUtils.fetchAirQualityData(searchText)

                    if (resultList != null) {
                        this@Main2ActivityAPI.videoIdList = resultList
                    }
                }

            }.start()
        }
        Handler().postDelayed({
            if (this@Main2ActivityAPI.videoIdList != null) {
                val recyclerView = findViewById(R.id.recyclerView) as RecyclerView
                recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
                val adapter = VideoModel(videoIdList)
                recyclerView.adapter = adapter
            }
        }, 3500)


    }

    inner class VideoModel(val videoFiles: ArrayList<Measurement>) : RecyclerView.Adapter<VideoModel.ViewHolder>() {
        override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
            val vFiles: Measurement = videoFiles[p1]

            p0.textViewCounter.text = (p1 + 1).toString()
            p0.textViewTitle?.text = vFiles.titleId
            p0.textViewVideoId?.text = vFiles.videoId
            Picasso.get().load(vFiles.imageURL).into(p0.textViewThumbnail)
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            val v = LayoutInflater.from(p0.context).inflate(R.layout.list_videos, p0, false)
            return ViewHolder(v)
        }

        override fun getItemCount(): Int {
            return videoFiles.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textViewTitle = itemView.findViewById(R.id.title) as TextView?
            val textViewThumbnail = itemView.findViewById(R.id.thumbnail) as ImageView?
            val textViewVideoId = itemView.findViewById(R.id.empty) as TextView
            val textViewCounter = itemView.findViewById(R.id.counter) as TextView

            init {
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, YoutbePlayer::class.java)
                    intent.putExtra("videoId", videoIdList[adapterPosition].videoId)
                    intent.putExtra("titleId", videoIdList[adapterPosition].titleId)
                    intent.putExtra("urlId", videoIdList[adapterPosition].imageURL)
                    intent.putExtra("comingFromApi", true)
                    startActivity(intent)
                }
            }
        }
    }

}

