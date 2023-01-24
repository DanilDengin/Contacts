package com.example.lessons.contactMap.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lessons.contactMap.di.DaggerContactMapComponent
import com.example.lessons.contactMap.di.MapComponentDependenciesProvider
import com.example.lessons.contacts.domain.entity.Address
import com.example.lessons.presentation.MainActivity
import com.example.lessons.utils.delegate.unsafeLazy
import com.example.lessons.utils.viewModel.viewModel
import com.example.library.R
import com.example.library.databinding.FragmentMapBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session.SearchListener
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import javax.inject.Inject
import javax.inject.Provider

internal class ContactMapFragment : Fragment(R.layout.fragment_map), SearchListener {

    @Inject
    lateinit var viewModelProvider: Provider<ContactMapViewModel>

    private val foundedPlacesImage by unsafeLazy {
        ImageProvider.fromResource(
            requireContext(),
            R.drawable.founded_place
        )
    }

    private val binding by viewBinding(FragmentMapBinding::bind)

    private var searchManager: SearchManager? = null

    private val viewModel by unsafeLazy { viewModel { viewModelProvider.get() } }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerContactMapComponent.builder()
            .mapComponentDependencies(
                (requireContext().applicationContext as MapComponentDependenciesProvider)
                    .getMapComponentDependencies()
            )
            .build()
            .also { it.inject(this) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
        SearchFactory.initialize(requireContext())
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        initListener()
        viewModel.contactAddress.observe(viewLifecycleOwner, ::makeToast)
        viewModel.networkExceptionState.observe(viewLifecycleOwner) { showNetworkExceptionToast() }
        binding.mapView.map.addInputListener(object : InputListener {
            override fun onMapTap(map: Map, point: Point) {
                binding.mapView.map.mapObjects.addPlacemark(point)
                viewModel.fetchAddress(point.latitude.toString(), point.longitude.toString())
            }

            override fun onMapLongTap(map: Map, p1: Point) = Unit
        })

    }

    private fun initListener() {
        binding.searchEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                submitQuery(binding.searchEdit.text.toString())
            }
            return@setOnEditorActionListener false
        }
        binding.mapView.map.addCameraListener { _, _, _, finished ->
            if (finished) {
                submitQuery(binding.searchEdit.text.toString())
            }
        }
    }

    private fun initActionBar() {
        (activity as? MainActivity)?.also { activity ->
            activity.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.backstack_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return if (menuItem.itemId == android.R.id.home) {
                        parentFragmentManager.popBackStack()
                        true
                    } else {
                        false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.STARTED)
            activity.supportActionBar?.also { actionBar ->
                actionBar.setTitle(R.string.contact_map_toolbar)
                actionBar.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun makeToast(address: Address?) {
        Toast.makeText(
            requireContext(),
            address?.address,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onSearchResponse(response: Response) {
        val mapObjects: MapObjectCollection = binding.mapView.map.mapObjects
        mapObjects.clear()
        for (searchResult in response.collection.children) {
            val resultLocation = searchResult.obj?.geometry?.firstOrNull()?.point
            if (resultLocation != null) {
                mapObjects.addPlacemark(
                    resultLocation,
                    foundedPlacesImage
                )
            }
        }
    }

    override fun onSearchError(error: Error) {
        var errorMessage = getString(R.string.exception_toast)
        when (error) {
            is RemoteError -> errorMessage = getString(R.string.server_exception_toast)
            is NetworkError -> errorMessage = getString(R.string.network_exception_toast)
        }
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun submitQuery(query: String) {
        if (query.isNotBlank()) {
            searchManager?.submit(
                query,
                VisibleRegionUtils.toPolygon(binding.mapView.map.visibleRegion),
                SearchOptions(),
                this
            )
        }
    }

    private fun showNetworkExceptionToast() {
        val contextNotNull = requireContext()
        Toast.makeText(
            contextNotNull,
            contextNotNull.getText(R.string.network_exception_toast),
            Toast.LENGTH_LONG
        ).show()
    }
}
