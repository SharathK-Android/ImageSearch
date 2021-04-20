package com.test.imagesearch.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.test.imagesearch.R
import com.test.imagesearch.data.database.ImageContent
import com.test.imagesearch.data.database.ImageDatabase
import com.test.imagesearch.data.database.Thumbnail
import com.test.imagesearch.databinding.MainFragmentBinding
import com.test.imagesearch.ui.main.adapter.ImageItemsAdapter
import com.test.imagesearch.ui.main.viewmodel.ImageListViewModel
import com.test.imagesearch.utils.Status.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ImageListFragment : Fragment() {

    companion object {
        fun newInstance() = ImageListFragment()

        const val JSON_PARAM_QUERY = "query"
        const val JSON_PARAM_PAGES = "pages"
        const val JSON_PARAM_SOURCE = "source"
        const val JSON_PARAM_THUMBNAIL = "thumbnail"
        const val JSON_PARAM_TITLE = "title"
        const val EMPTY = ""
    }

    private lateinit var viewModel: ImageListViewModel
    private lateinit var binding: MainFragmentBinding
    private var searchQuery: String = EMPTY
    private var itemsAdapter: ImageItemsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ImageListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.main_fragment, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.search_image)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setUpObserver()
        binding.imageSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchQuery = query
                viewModel.getImageContents(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                binding.progressBar.visibility = View.GONE
                viewModel.cancelJob()
                return false
            }
        })
    }

    private fun setUpObserver() {
        viewModel.liveData.observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let {
                            parseResponse(it)
                        }
                    }
                    ERROR -> {
                        binding.imageItemsView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        view?.let { view ->
                            Snackbar.make(
                                view,
                                getString(R.string.something_went_wrong),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                    LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.imageItemsView.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun parseResponse(responseObj: JsonObject) {
        val pagesObj: JsonObject? =
            responseObj.get(JSON_PARAM_QUERY)?.asJsonObject?.get(JSON_PARAM_PAGES)?.asJsonObject
        if (pagesObj != null) {
            val thumbnailList = ArrayList<Thumbnail>()
            for (key in pagesObj.keySet()) {
                val thumbnailObj =
                    pagesObj.get(key).asJsonObject.get(JSON_PARAM_THUMBNAIL)?.asJsonObject
                thumbnailObj?.get(JSON_PARAM_SOURCE)?.let { source ->
                    thumbnailList.add(
                        Thumbnail(
                            title = pagesObj.get(key).asJsonObject.get(JSON_PARAM_TITLE)?.asString
                                ?: EMPTY,
                            imageUrl = source.asString ?: EMPTY
                        )
                    )
                }
            }
            setupItemsAdapter(thumbnailList)
            GlobalScope.launch {
                ImageDatabase.getInstance(requireContext()).imageContentDao()
                    .saveImageContent(ImageContent(searchQuery, thumbnailList))
            }
        }
    }

    private fun setupItemsAdapter(thumbnailList: ArrayList<Thumbnail>) {
        with(binding) {
            if (itemsAdapter != null) {
                itemsAdapter?.updateDataSet(thumbnailList)
            } else {
                itemsAdapter = ImageItemsAdapter(thumbnailList) {
                    activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                        ?.replace(R.id.container, ImageDetailsFragment.newInstance(it))
                        ?.commit()
                }
            }
            imageItemsView.apply {
                visibility = View.VISIBLE
                adapter = itemsAdapter
                layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            }
        }
    }
}