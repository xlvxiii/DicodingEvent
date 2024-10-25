package com.example.dicodingevent.ui.event_detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.repositories.Result
import com.example.dicodingevent.databinding.ActivityEventDetailBinding
import com.example.dicodingevent.ui.FailDialogFragment

class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding
    private val args: EventDetailActivityArgs by navArgs()
    private val eventDetailViewModel: EventDetailViewModel by viewModels<EventDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Event Detail"

//        eventDetailViewModel.fetchEventDetail(args.id)
//
//        eventDetailViewModel.eventDetail.observe(this) { eventDetail ->
//            Glide.with(binding.root.context).load(eventDetail?.imageLogo).into(binding.imgEventPhoto)
//            binding.tvEventName.text = eventDetail?.name
//            binding.tvEventOwner.text = eventDetail?.ownerName
//            binding.tvEventDate.text = eventDetail?.beginTime
//            binding.tvEventQuota.text = eventDetail?.quota.toString()
//            binding.tvDesc.text = HtmlCompat.fromHtml(eventDetail?.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
//            setRegisterButton(eventDetail?.link.toString())
//        }
//
//        eventDetailViewModel.isLoading.observe(this) {
//            showLoading(it)
//        }
//
//        eventDetailViewModel.isLoadSuccess.observe(this) {
//            showDialog(it)
//        }
//
//        binding.fabFavorite.setOnClickListener {
//            eventDetailViewModel.saveFavoriteEvent()
//        }

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: EventDetailViewModel by viewModels {
            factory
        }

        val fabFavorite = binding.fabFavorite
//        fabFavorite.setImageDrawable(ContextCompat.getDrawable(fabFavorite.context, R.drawable.baseline_favorite_border_24))

        viewModel.fetchEventDetail(args.id).observe(this, Observer { eventDetail ->
            binding.progressBar.visibility = View.GONE
            if (eventDetail != null) {
                when (eventDetail) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE

                        Glide.with(binding.root.context).load(eventDetail.data?.imageLogo).into(binding.imgEventPhoto)
                        binding.tvEventName.text = eventDetail.data?.name
                        binding.tvEventOwner.text = eventDetail.data?.ownerName
                        binding.tvEventDate.text = eventDetail.data?.beginTime
                        binding.tvEventQuota.text = eventDetail.data?.quota.toString()
                        binding.tvDesc.text = HtmlCompat.fromHtml(eventDetail.data?.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
                        setRegisterButton(eventDetail.data?.link.toString())

                        viewModel.isFavoriteEvent(args.id).observe(this, Observer {
                            if (it) {
                                fabFavorite.setImageDrawable(ContextCompat.getDrawable(fabFavorite.context, R.drawable.baseline_favorite_24))
                                fabFavorite.setOnClickListener {
                                    viewModel.deleteFavoriteEvent(eventDetail.data?.id)
                                    Toast.makeText(this, "Event removed from favorite", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                fabFavorite.setImageDrawable(ContextCompat.getDrawable(fabFavorite.context, R.drawable.baseline_favorite_border_24))
                                fabFavorite.setOnClickListener {
                                    viewModel.saveFavoriteEvent(eventDetail.data)
                                    Toast.makeText(this, "Event added to favorite", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan" + eventDetail.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setRegisterButton(url: String) {
        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun showDialog(isLoadSuccess: Boolean) {
        if (!isLoadSuccess) {
            FailDialogFragment().show(supportFragmentManager, "FailDialogFragment")
        }
    }

}