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
import com.example.lessons.contactMap.data.address.remote.model.AddressItem
import com.example.lessons.contactMap.di.DaggerContactMapComponent
import com.example.lessons.presentation.MainActivity
import com.example.lessons.utils.viewModel.viewModel
import com.example.library.R
import com.example.library.databinding.FragmentMapBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
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

internal class ContactMapFragment : Fragment(R.layout.fragment_map), InputListener, SearchListener,
    MenuProvider, CameraListener {

    @Inject
    lateinit var viewModelProvider: Provider<ContactMapViewModel>

    private val binding by viewBinding(FragmentMapBinding::bind)

    private var searchManager: SearchManager? = null

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) { viewModel { viewModelProvider.get() } }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerContactMapComponent.builder()
            .build()
            .also { it.inject(this) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
        SearchFactory.initialize(requireContext())
        SearchFactory.initialize(requireContext())
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.STARTED)
        mainActivity.supportActionBar?.setTitle(R.string.contact_map_toolbar)
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.searchEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                submitQuery(binding.searchEdit.text.toString())
            }
            return@setOnEditorActionListener false
        }
        viewModel.addressItem.observe(viewLifecycleOwner) { address -> makeToast(address) }
        viewModel.networkExceptionState.observe(viewLifecycleOwner) { showNetworkExceptionToast() }
        viewModel.fatalExceptionState.observe(viewLifecycleOwner) { showFatalExceptionToast() }
        binding.mapView.map.addInputListener(this)
        binding.mapView.map.addCameraListener(this)
    }

    private fun makeToast(addressItem: AddressItem?) {
        Toast.makeText(
            requireContext(),
            addressItem?.response?.geoObjectCollection?.featureMember?.get(0)?.geoObject?.metaDataProperty?.geocoderMetaData?.text,
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

    override fun onMapTap(p0: Map, point: Point) {
        binding.mapView.map.mapObjects.addPlacemark(point)
        viewModel.fetchAddress(point.latitude.toString(), point.longitude.toString())
    }

    override fun onMapLongTap(p0: Map, p1: Point) = Unit

    override fun onSearchResponse(response: Response) {
        val mapObjects: MapObjectCollection = binding.mapView.map.mapObjects
        mapObjects.clear()
        for (searchResult in response.collection.children) {
            val resultLocation = searchResult.obj?.geometry?.get(0)?.point
            if (resultLocation != null) {
                mapObjects.addPlacemark(
                    resultLocation,
                    ImageProvider.fromResource(requireContext(), R.drawable.founded_place)
                )
            }
        }
    }

    override fun onSearchError(error: Error) {
        var errorMessage = getString(R.string.unknown_error_message)
        if (error is RemoteError) {
            errorMessage = getString(R.string.remote_error_message)
        } else if (error is NetworkError) {
            errorMessage = getString(R.string.network_error_message)
        }
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        cameraUpdateReason: CameraUpdateReason,
        finished: Boolean
    ) {
        if (finished) {
            submitQuery(binding.searchEdit.text.toString())
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.backstack_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            android.R.id.home -> {
                parentFragmentManager.popBackStack()
                true
            }
            else -> false
        }
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

    private fun showFatalExceptionToast() {
        val contextNotNull = requireContext()
        Toast.makeText(
            contextNotNull,
            contextNotNull.getText(R.string.exception_toast),
            Toast.LENGTH_LONG
        ).show()
    }
}
