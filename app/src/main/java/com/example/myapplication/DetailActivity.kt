package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val article = intent.getSerializableExtra("article") as? Article

        article?.let {
            binding.detailTitle.text = it.title
            binding.detailSource.text = it.source.name
            binding.detailDate.text = it.publishedAt
            binding.detailDescription.text = it.description
            binding.detailContent.text = it.content

            Glide.with(this)
                .load(it.image)
                .placeholder(android.R.drawable.progress_horizontal)
                .into(binding.detailImage)

            binding.btnReadFull.setOnClickListener { _ ->
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
                startActivity(browserIntent)
            }
        }
    }
}
