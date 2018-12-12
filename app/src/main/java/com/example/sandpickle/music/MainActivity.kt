package com.example.sandpickle.music
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import com.example.sandpickle.music.model.Favourite
import com.example.sandpickle.music.viewmodel.FavouriteViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var adapter: FavouritesAdapter = FavouritesAdapter()
    private lateinit var viewModel: FavouriteViewModel
    private var favouritesList: ArrayList<Favourite> = ArrayList()
    private lateinit var category: String
    private lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fun String.toColor(): Int = Color.parseColor(this)
        screen1.setBackgroundColor("#D3F0E2".toColor())
        viewModel = ViewModelProviders.of(this).get(FavouriteViewModel::class.java)
        if (intent.hasExtra("videoId")){
            var video_Id_database = intent.getStringExtra("videoId")
            var video_Title_database = intent.getStringExtra("titleId")
            var video_Url_database = intent.getStringExtra("urlId")
            var category_saved = intent.getStringExtra("category_sender")
            viewModel.addFavourite(video_Title_database, video_Id_database, category_saved,0,video_Url_database,1, 0)
        }

        prefs = getSharedPreferences("$packageName.${SharedPrefKeys.SORT_PREFERENCES_FILE}", Context.MODE_PRIVATE)
        category = intent.getStringExtra("category_sender")
        //setSupportActionBar(toolbar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // it below is the updated favourites list; observer fires when data has changed
        val observer = Observer<ArrayList<Favourite>> {
            recyclerView.adapter = adapter
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return favouritesList.size
                }

                override fun getNewListSize(): Int {
                    return it!!.size
                }

                override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
                    return favouritesList[p0].id == it!![p1].id
                }

                override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
                    return favouritesList[p0] == it!![p1]
                }
            })
            result.dispatchUpdatesTo(adapter) // update the adapter
            favouritesList = it!!
        }

        fab.setOnClickListener {
            val intent = Intent(this, defaultActivity::class.java)

            startActivityForResult(intent, 0)
        }

        viewModel.getFavourites(prefs, category).observe(this, observer) //without this, won't update til you restart it

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_url -> {
                setPreference(0)
                true
            }

            R.id.action_important ->{
                setPreference(2)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setPreference(sortByUrl: Int) {
        val sharedPreferences = getSharedPreferences("$packageName.${SharedPrefKeys.SORT_PREFERENCES_FILE}", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt(SharedPrefKeys.sortOption, sortByUrl)

        editor.apply()
        viewModel.getFavourites(sharedPreferences,category)
    }

    // inner here gives this class to variables in the MainActivity class
    inner class FavouritesAdapter: RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FavouritesViewHolder {
            val itemView = LayoutInflater.from(p0.context).inflate(R.layout.list_item, p0, false)
            return FavouritesViewHolder(itemView)
        }

        override fun onBindViewHolder(p0: FavouritesViewHolder, p1: Int) {
            val favourite = favouritesList[p1]
            p0.urlTextView.text = favourite.title
            p0.idTextView.text = favourite.videoId
            Picasso.get().load(favourite.complete).into(p0.thumbnailImageView)

            if (favourite.important == 0){
                p0.importantCheckBox.isChecked = true
            }
            else{
                p0.importantCheckBox.isChecked = false
            }
        }

        override fun getItemCount(): Int {
            return favouritesList.size
        }

        inner class FavouritesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var urlTextView: TextView = itemView.tvUrl  //title of vid
            var idTextView: TextView = itemView.idSave  //video id
            var importantCheckBox: CheckBox = itemView.important  //is it important
            var thumbnailImageView: ImageView = itemView.thumbnail  //image
            init {

                itemView.btnDelete.setOnClickListener {
                    val favourite = favouritesList[adapterPosition]
                    viewModel.removeFavourite(favourite.id)
                }


                itemView.important.setOnClickListener {
                    val favourite = favouritesList[adapterPosition]
                    viewModel.removeFavourite(favourite.id)
                    var checker: Int
                    if (it.important.isChecked == true){
                        checker = 0
                        favourite.important = checker
                        viewModel.addFavourite(favourite.title, favourite.videoId, favourite.category, 0, favourite.complete, checker, 0)
                    }
                    else if(it.important.isChecked == false){
                        checker = 1
                        favourite.important = checker
                        viewModel.addFavourite(favourite.title, favourite.videoId, favourite.category, 0, favourite.complete, checker, 0)
                    }
                    viewModel.getFavourites(prefs,category)
                }
                    itemView.setOnClickListener {
                        val favourite = favouritesList[adapterPosition]
                        val intent = Intent(itemView.context, YoutbePlayer::class.java)
                        intent.putExtra("videoId", favourite.videoId)
                        intent.putExtra("titleId", favourite.title)
                        intent.putExtra("urlId", favourite.complete)
                        intent.putExtra("comingFromApi", false)
                        startActivity(intent)
                    }
            }
        }
    }
}
