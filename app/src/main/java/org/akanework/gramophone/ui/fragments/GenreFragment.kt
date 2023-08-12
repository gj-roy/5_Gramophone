package org.akanework.gramophone.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import org.akanework.gramophone.R
import org.akanework.gramophone.logic.utils.MediaStoreUtils
import org.akanework.gramophone.ui.adapters.GenreAdapter
import org.akanework.gramophone.ui.adapters.GenreDecorAdapter
import org.akanework.gramophone.ui.viewmodels.LibraryViewModel

/**
 * [GenreFragment] displays information about your song's genres.
 */
class GenreFragment : Fragment() {

    private val libraryViewModel: LibraryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_genre, container, false)
        val genreRecyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerview)

        genreRecyclerView.layoutManager = LinearLayoutManager(activity)
        val genreList = mutableListOf<MediaStoreUtils.Genre>()
        genreList.addAll(libraryViewModel.genreItemList.value!!)
        val genreAdapter = GenreAdapter(genreList, requireContext(), requireActivity().supportFragmentManager)
        val genreDecorAdapter = GenreDecorAdapter(requireContext(),
            libraryViewModel.genreItemList.value!!.size,
            genreAdapter)
        val concatAdapter = ConcatAdapter(genreDecorAdapter, genreAdapter)

        if (!libraryViewModel.genreItemList.hasActiveObservers()) {
            libraryViewModel.genreItemList.observe(viewLifecycleOwner) { mediaItems ->
                if (mediaItems.isNotEmpty()) {
                    if (mediaItems.size != genreList.size || genreDecorAdapter.isCounterEmpty()) {
                        genreDecorAdapter.updateSongCounter(mediaItems.size)
                    }
                    genreAdapter.updateList(mediaItems)
                }
            }
        }

        genreRecyclerView.adapter = concatAdapter

        FastScrollerBuilder(genreRecyclerView).apply {
            useMd2Style()
            build()
        }
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}