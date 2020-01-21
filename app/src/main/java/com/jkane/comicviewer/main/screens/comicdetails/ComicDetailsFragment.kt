package com.jkane.comicviewer.main.screens.comicdetails

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.jkane.comicviewer.BuildConfig
import com.jkane.comicviewer.R
import com.jkane.comicviewer.application.Application
import com.jkane.comicviewer.application.database.databases.ComicDatabase
import com.jkane.comicviewer.application.network.services.MarvelService
import com.jkane.comicviewer.main.MainActivity
import kotlinx.android.synthetic.main.comic_details_fragment.*
import javax.inject.Inject

class ComicDetailsFragment : Fragment() {
    // Initial comic id to display for base functionality.
    private val initialComicId = BuildConfig.INITIAL_COMIC_ID

    @Inject
    lateinit var marvelService: MarvelService
    @Inject
    lateinit var comicDatabase: ComicDatabase

    companion object {
        fun newInstance() = ComicDetailsFragment()
    }

    private lateinit var viewModel: ComicDetailsViewModel
    private lateinit var comicDetailsCoordinator: ComicDetailsCoordinator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as Application)
            .getAppComponent()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.comic_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ComicDetailsViewModel::class.java)
        viewModel.setInitialState()
        bindLiveData()
        bindButtons()

        comicDetailsCoordinator = ComicDetailsCoordinator(
            comicDatabase,
            marvelService,
            viewModel,
            requireContext()
        )
        comicDetailsCoordinator.getComicDetails(initialComicId)
    }

    private fun bindButtons() {
        refresh_btn.setOnClickListener {
            comicDetailsCoordinator.getComicDetails((1..20000).random().toString())
        }
        read_cta.setOnClickListener { comingSoonToast() }
        more_in_series_cta.setOnClickListener { comingSoonToast() }
        recommended_series_cta.setOnClickListener { comingSoonToast() }
    }

    private fun comingSoonToast() = Toast.makeText(
        context,
        R.string.coming_soon,
        Toast.LENGTH_SHORT
    ).show()

    /**
     * Binds live data observers for all viewModel variables
     *
     * When a viewModel variable is updated, the observer will update the associated UI component.
     */
    private fun bindLiveData() {
        bindIsLoading()
        bindCoverImageUrl()
        bindTitle()
        bindDescription()
        bindCoverCreators()
        bindInteriorCreators()
        bindErrorMessage()
        bindPublishDate()
    }

    /**
     * Binds publishDate viewmodel attribute to the publish_date ui element
     *
     * If it is null or blank, it will use a generic 'data is not available' message.
     */
    private fun bindPublishDate() {
        viewModel.publishDate.observe(this, Observer<String> {
            publish_date.text =
                if (it.isNullOrBlank()) getString(R.string.data_not_available)
                else getString(R.string.publish_date).format(it)
        })
    }

    /**
     * Binds an error Snackbar on the parent activity to the errorMessage viewModel attribute.
     *
     * In a more robust application there would be a shared BaseActivity that would contain the
     * showError message to make it more global rather than tightly coupled to this specific
     * activity.
     */
    private fun bindErrorMessage() {
        viewModel.errorMessage.observe(this, Observer<String> {
            (activity as MainActivity).showError(it)
        })
    }

    /**
     * Binds interiorCreator viewmodel attribute to the interior_text ui element
     *
     * If it is null or blank, it will use a generic 'data is not available' message.
     */
    private fun bindInteriorCreators() {
        viewModel.interiorCreators.observe(
            this,
            Observer<String> {
                interior_text.text =
                    if (it.isNullOrBlank()) getString(R.string.data_not_available) else it
            })
    }

    /**
     * Binds coverCreators viewmodel attribute to the cover_text ui element
     *
     * If it is null or blank, it will use a generic 'data is not available' message.
     */
    private fun bindCoverCreators() {
        viewModel.coverCreators.observe(
            this,
            Observer<String> {
                cover_text.text =
                    if (it.isNullOrBlank()) getString(R.string.data_not_available) else it
            })
    }

    /**
     * Binds description viewmodel attribute to the description_text ui element
     *
     * If it is null or blank, it will use a generic 'data is not available' message.
     */
    private fun bindDescription() {
        viewModel.description.observe(
            this,
            Observer<String> {
                description_text.text = it ?: getString(R.string.data_not_available)
            })
    }

    /**
     * Binds title viewmodel attribute to the title_text ui element
     *
     * If it is null or blank, it will use a generic 'data is not available' message.
     */
    private fun bindTitle() {
        viewModel.title.observe(
            this,
            Observer<String> {
                title_text.text = it ?: getString(R.string.data_not_available)
            })
    }

    /**
     * Binds coverImageUrl viewmodel attribute to the featured_image ui element
     * Uses glide to load the url into the ImageView.
     */
    private fun bindCoverImageUrl() {
        viewModel.coverImageUrl.observe(
            this,
            Observer<String> {
                it?.let {
                    Glide
                        .with(this)
                        .load(it)
                        .into(featured_image)
                }
            }
        )
    }

    /**
     * Binds the viewModel isLoading attribute to the activity's showLoading function to display
     * a loading spinner overlay (and block user input).
     *
     * In a more robust application there would be a shared BaseActivity that would contain the
     * showLoading function to make it more global rather than tightly coupled to this specific
     * activity.
     */
    private fun bindIsLoading() {
        viewModel.isLoading.observe(
            this,
            Observer<Boolean> {
                // TODO - casting like this would prevent using this fragment in another activity.
                (activity as MainActivity).showLoading(it)
            }
        )
    }
}
