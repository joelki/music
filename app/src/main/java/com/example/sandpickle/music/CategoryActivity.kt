package com.example.sandpickle.music
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.sandpickle.music.viewmodel.FavouriteViewModel
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.list_item2.view.*
import java.util.*

class CategoryActivity : AppCompatActivity() {

    private var adapter: CategoriesAdapter = CategoriesAdapter()
    private lateinit var viewModel: FavouriteViewModel
    private var categoriesList: ArrayList<String> = ArrayList()
    var video_Id_saver = ""
    var title_Id_saver = ""
    var url_Id_saver = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        fun String.toColor(): Int = Color.parseColor(this)
        screen2.setBackgroundColor("#D3F0E2".toColor())

        viewModel = ViewModelProviders.of(this).get(FavouriteViewModel::class.java)

        if (intent.hasExtra("videoId")){
           video_Id_saver = intent.getStringExtra("videoId")
            title_Id_saver = intent.getStringExtra("titleId")
            url_Id_saver = intent.getStringExtra("urlId")
            fab2.setOnClickListener {
                val linearLayout = LinearLayout(this@CategoryActivity)
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                val categoryEditText = EditText(this@CategoryActivity)
                val dueDateTextView = TextView(this@CategoryActivity)
                categoryEditText.setHint("Playlist Name")
                linearLayout.addView(categoryEditText)

                val dialog = AlertDialog.Builder(this@CategoryActivity)
                        .setTitle("Create a Playlist!")
                        .setView(linearLayout)
                        .setPositiveButton("Add") { _, _ ->
                            val category = categoryEditText.text.toString()
                            viewModel.addFavourite(title_Id_saver, video_Id_saver,category,0, url_Id_saver,1, 0)
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("category_sender", category);
                            startActivityForResult(intent, 0)
                        }
                        .setNegativeButton("Cancel", null)
                        .create()
                dialog.show()
            }
        }
        else{
            fab2.hide()
        }
        recyclerView2.layoutManager = LinearLayoutManager(this)
        val observer = Observer<ArrayList<String>> {
            recyclerView2.adapter = adapter
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return categoriesList.size
                }

                override fun getNewListSize(): Int {
                    return it!!.size
                }

                override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
                    return categoriesList[p0] == it!![p1]
                }

                override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
                    return categoriesList[p0] == it!![p1]
                }
            })
            result.dispatchUpdatesTo(adapter) // update the adapter
            categoriesList = it!!
        }


        val prefs = getSharedPreferences("$packageName.${SharedPrefKeys.SORT_PREFERENCES_FILE}", Context.MODE_PRIVATE)

        viewModel.getCategories().observe(this, observer) //without this, won't update til you restart it
    }

    //when something comes back, updating categories
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        video_Id_saver = ""
        title_Id_saver = ""
        url_Id_saver = ""
        fab2.hide()
        viewModel.getCategories()
    }

    // inner here gives this class to variables in the MainActivity class
    inner class CategoriesAdapter: RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CategoriesAdapter.CategoriesViewHolder {
            val itemView = LayoutInflater.from(p0.context).inflate(R.layout.list_item2, p0, false)
            return CategoriesViewHolder(itemView)
        }

        override fun onBindViewHolder(p0: CategoriesViewHolder, p1: Int) {
            val category = categoriesList[p1]
            p0.categoryTextView.text = category
        }

        override fun getItemCount(): Int {
            return categoriesList.size
        }

        inner class CategoriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var categoryTextView: TextView = itemView.categoriesURL

            init {
                itemView.setOnClickListener {
                    if (video_Id_saver != ""){
                        val intent = Intent(itemView.context, MainActivity::class.java)
                        intent.putExtra("category_sender", it.categoriesURL.text.toString());
                        intent.putExtra("videoId", video_Id_saver)
                        intent.putExtra("titleId", title_Id_saver)
                        intent.putExtra("urlId", url_Id_saver)
                        startActivityForResult(intent, 0)
                    }
                    else{
                        val intent = Intent(itemView.context, MainActivity::class.java)
                        intent.putExtra("category_sender", it.categoriesURL.text.toString());
                        startActivityForResult(intent, 0)
                    }

                }
            }
        }
    }
}
