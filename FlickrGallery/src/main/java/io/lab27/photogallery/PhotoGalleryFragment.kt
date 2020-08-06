package io.lab27.photogallery

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.bumptech.glide.Glide
import java.util.concurrent.TimeUnit

private const val TAG = "PhotoGalleryFragment"
private const val POLL_WORK = "POLL_WORK"

class PhotoGalleryFragment : VisibleFragment() {
    private val photoGalleryViewModel by lazy {
        ViewModelProviders.of(this).get(PhotoGalleryViewModel::class.java)
    }
    lateinit var photoRecyclerView: RecyclerView
//    lateinit var thumbnailDownloader: ThumbnailDownloader<PhotoHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)

//        val responseHandler = Handler()
//        thumbnailDownloader = ThumbnailDownloader(responseHandler) { photoHolder, bitmap ->
//            val drawable = BitmapDrawable(resources, bitmap)
//            photoHolder.bindDrawable(drawable)
//        }
//        lifecycle.addObserver(thumbnailDownloader.fragmentLifecycleObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_gallery, menu)

        val searchItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    Log.d(TAG, "onQueryTextSubmit : $query")
                    photoGalleryViewModel.fetchPhotos(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d(TAG, "onQueryTextChange : $newText")
                    return false
                }

            })

            setOnSearchClickListener {
                searchView.setQuery(photoGalleryViewModel.searchTerm, false)
            }

            val toggleItem = menu.findItem(R.id.menu_item_toggle_polling)
            val isPolling = QueryPreferences.isPolling(requireContext())
            val toggleItemTitle = if (isPolling) {
                R.string.stop_polling
            } else {
                R.string.start_polling
            }
            toggleItem.setTitle(toggleItemTitle)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_clear -> {
                photoGalleryViewModel.fetchPhotos("")
                true
            }
            R.id.menu_item_toggle_polling -> {
                val isPolling = QueryPreferences.isPolling(requireContext())
                if (isPolling) {
                    WorkManager.getInstance().cancelUniqueWork(POLL_WORK)
                    QueryPreferences.setPoling(requireContext(), false)
                } else {
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .build()

                    val periodicRequest = PeriodicWorkRequest
                        .Builder(PollWorker::class.java, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build()
                    WorkManager
                        .getInstance()
                        .enqueueUniquePeriodicWork(
                            POLL_WORK,
                            ExistingPeriodicWorkPolicy.KEEP,
                            periodicRequest
                        )
                    QueryPreferences.setPoling(requireContext(), true)
                }
                activity?.invalidateOptionsMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewLifecycleOwner.lifecycle.addObserver(
//            thumbnailDownloader.viewLifecycleObserver
//        )
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)
        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)
        photoRecyclerView.layoutManager = GridLayoutManager(context, 3)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        photoGalleryViewModel.galleryItemLiveData.observe(
            viewLifecycleOwner,
            Observer {
                photoRecyclerView.adapter = PhotoAdapter(it)
            }
        )
    }

    override fun onDestroy() {
//        lifecycle.removeObserver(thumbnailDownloader.fragmentLifecycleObserver)
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        viewLifecycleOwner.lifecycle.removeObserver(thumbnailDownloader.viewLifecycleObserver)
    }

    inner class PhotoHolder(val itemImageView: ImageView) :
        RecyclerView.ViewHolder(itemImageView) {
        //        val bindTitle: (CharSequence) -> Unit = itemTextView::setText
//        val bindDrawable: (Drawable) -> Unit = itemImageView::setImageDrawable

        fun onBind(galleryItem: GalleryItem) {
            Glide.with(context!!).load(galleryItem.url).into(itemImageView)
        }
    }

    private inner class PhotoAdapter(private val galleryItems: List<GalleryItem>) :
        RecyclerView.Adapter<PhotoHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
//            val textView = TextView(parent.context)
            val view = layoutInflater.inflate(
                R.layout.list_item_gallery,
                parent,
                false
            ) as ImageView
            return PhotoHolder(view)
        }

        override fun getItemCount(): Int {
            return galleryItems.size
        }

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
//            holder.bindTitle(galleryItem.title)
            holder.onBind(galleryItem)
//            val placeholder: Drawable = ContextCompat.getDrawable(
//                requireContext(),
//                R.drawable.ic_launcher_foreground
//            ) ?: ColorDrawable()
//            holder.bindDrawable(placeholder)

//            thumbnailDownloader.queueThumbnail(holder, galleryItem.url)
        }

    }

    companion object {
        fun newInstance(): PhotoGalleryFragment {
            return PhotoGalleryFragment()
        }
    }
}
