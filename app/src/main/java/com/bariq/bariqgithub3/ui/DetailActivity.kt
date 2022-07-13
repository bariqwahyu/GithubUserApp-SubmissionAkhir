package com.bariq.bariqgithub3.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bariq.bariqgithub3.R
import com.bariq.bariqgithub3.data.Result
import com.bariq.bariqgithub3.data.local.entity.UserEntity
import com.bariq.bariqgithub3.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = resources.getString(R.string.app_nameDetail)

        val factory: UserViewModelFactory = UserViewModelFactory.getInstance(this)
        val detailUserViewModel: DetailUserViewModel by viewModels {
            factory
        }

        username = intent.getStringExtra(EXTRA_USER) as String
        detailUserViewModel.getUser(username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter
        supportActionBar?.elevation = 0f

        detailUserViewModel.getUser(username).observe(this) { result ->
            if (result != null){
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val user = result.data
                        setData(user)
                        binding.btnFavorite.setOnClickListener {
                            if (user.isFavorite) {
                                detailUserViewModel.deleteUserFavorite(user)
                            } else {
                                detailUserViewModel.saveUserFavorite(user)
                            }
                        }
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
            detailUserViewModel.isLoading.observe(this){
                showLoading(it)
            }

            detailUserViewModel.toastText.observe(this) {
                it.getContentIfNotHandled()?.let { toastText ->
                    Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://github.com/${binding.tvUsername.text}")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun setData(user: UserEntity) {
        binding.apply {
            tvName.text = user.name
            tvUsername.text = user.username
            tvRepo.text = resources.getString(R.string.repo, user.repository)
            tvCompany.text = user.company
            tvLocation.text = user.location
            if (user.isFavorite) {
                btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context, R.drawable.ic_star_white_24dp))
            } else {
                btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context, R.drawable.ic_star_outline_white_24dp))
            }
        }
        Glide.with(this)
            .load(user.avatar_url)
            .circleCrop()
            .into(binding.imgProfile)

        val countFollow = arrayOf(user.followers, user.following)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position], countFollow[position])
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }
}