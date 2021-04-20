package com.test.imagesearch.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.test.imagesearch.R
import com.test.imagesearch.data.database.Thumbnail
import com.test.imagesearch.databinding.FragmentImageDetailsBinding

class ImageDetailsFragment : Fragment() {

    private var thumbnail: Thumbnail? = null
    private lateinit var binding: FragmentImageDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            thumbnail = it.getParcelable(IMAGE_THUMBNAIL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.title = getString(R.string.image_details)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_image_details, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageTitle = thumbnail?.title
        binding.imageUrl = thumbnail?.imageUrl
    }

    companion object {
        const val IMAGE_THUMBNAIL = "image_thumbnail"

        fun newInstance(thumbnail: Thumbnail) =
            ImageDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(IMAGE_THUMBNAIL, thumbnail)
                }
            }
    }

}