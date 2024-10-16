package com.example.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.adapter.CategoryAdapter
import com.example.musicplayer.adapter.SectionSongListAdapter
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.models.CategoryModel
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategories("category")
        setUpSection("section_1", binding.section1MainLayout, binding.section1Title, binding.section1RecyclerView)
        setUpSection("section_2", binding.section2MainLayout, binding.section2Title, binding.section2RecyclerView)
        setUpSection("section_3", binding.section3MainLayout, binding.section3Title, binding.section3RecyclerView)
        getSingers("singers")
    }

    override fun onResume() {
        super.onResume()
        showPlayerView()
    }

    private fun showPlayerView() {
        binding.playerView.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }
        MyExoplayer.getCurrentSong()?.let {
            binding.playerView.visibility = View.VISIBLE
            binding.songTitleTextView.text = "Now Playing : ${it.title}"
            Glide.with(binding.songCoverImageView).load(it.coverUrl)
                .apply(RequestOptions().transform(RoundedCorners(32)))
                .into(binding.songCoverImageView)
        } ?: run {
            binding.playerView.visibility = View.GONE
        }
    }

    private fun getCategories(id: String) {
        FirebaseFirestore.getInstance().collection(id)
            .get().addOnSuccessListener {
                val categoryList = it.toObjects(CategoryModel::class.java)
                setupCategoryRecyclerView(categoryList)
            }
    }

    private fun setupCategoryRecyclerView(categoryList: List<CategoryModel>) {
        categoryAdapter = CategoryAdapter(categoryList, false)
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRecyclerView.adapter = categoryAdapter
    }

    private fun getSingers(id: String) {
        FirebaseFirestore.getInstance().collection(id)
            .get().addOnSuccessListener {
                val categoryList = it.toObjects(CategoryModel::class.java)
                setupSingerRecyclerView(categoryList)
            }
    }

    private fun setupSingerRecyclerView(categoryList: List<CategoryModel>) {
        categoryAdapter = CategoryAdapter(categoryList, true)
        binding.singerRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.singerRecyclerView.adapter = categoryAdapter
    }

    private fun setUpSection(id: String, mainLayout: RelativeLayout, titleView: TextView, recyclerView: RecyclerView) {
        FirebaseFirestore.getInstance().collection("sections")
            .document(id)
            .get().addOnSuccessListener {
                val section = it.toObject(CategoryModel::class.java)
                section?.apply {
                    mainLayout.visibility = View.VISIBLE
                    titleView.text = name
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                    recyclerView.adapter = SectionSongListAdapter(songs)
                    mainLayout.setOnClickListener {
                        SongsListActivity.category = section
                        startActivity(Intent(this@MainActivity, SongsListActivity::class.java))
                    }
                }
            }
    }
}