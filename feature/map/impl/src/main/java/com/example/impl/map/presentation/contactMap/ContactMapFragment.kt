package com.example.impl.map.presentation.contactMap

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.api.map.entity.ContactMapArguments
import com.example.common.address.domain.entity.ContactMap
import com.example.di.dependency.findFeatureExternalDeps
import com.example.impl.map.databinding.FragmentMapBinding
import com.example.impl.map.presentation.MapComponentDependenciesProvider
import com.example.impl.map.presentation.MapComponentViewModel
import com.example.impl.map.presentation.contactMapRoutePicker.ContactMapException
import com.example.mvvm.getComponentViewModel
import com.example.mvvm.viewModel
import com.example.ui.R
import com.example.utils.delegate.unsafeLazy
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.transport.TransportFactory
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.launch
import com.example.impl.map.R as FeatureRes

internal class ContactMapFragment : Fragment(FeatureRes.layout.fragment_map) {

    @Inject
    lateinit var viewModelProvider: Provider<ContactMapViewModel>

    @Inject
    lateinit var toastRouter: ToastRouter

    @Inject
    lateinit var mapDelegateFactoryProvider: Provider<MapDelegate.Factory>

    private var mapDelegate: MapDelegate? = null

    private val binding by viewBinding(FragmentMapBinding::bind)

    private val viewModel by unsafeLazy { viewModel { viewModelProvider.get() } }

    private var contactArgument: ContactMapArguments? = null

    private var chosenPoint = Point()

    private var inputListener: InputListener? = null

    override fun onAttach(context: Context) {
        MapComponentDependenciesProvider.mapExternalDependencies = findFeatureExternalDeps()
        getComponentViewModel<MapComponentViewModel>().mapComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.also { contextNotNull ->
            TransportFactory.initialize(contextNotNull)
            DirectionsFactory.initialize(contextNotNull)
            MapKitFactory.initialize(contextNotNull)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.contact_map_toolbar)
        contactArgument = if (Build.VERSION.SDK_INT >= TIRAMISU) {
            arguments?.getParcelable(ARG, ContactMapArguments::class.java)
        } else {
            arguments?.getParcelable(ARG)
        }
        viewModel.exceptionState.observe(viewLifecycleOwner) { exception ->
            val exceptionMessage = when (exception) {
                ContactMapException.SERVER_EXCEPTION -> getString(R.string.server_exception_toast)
                ContactMapException.NETWORK_EXCEPTION -> getString(R.string.network_exception_toast)
                ContactMapException.FATAL_EXCEPTION -> getString(R.string.exception_toast)
            }
            toastRouter.showToast(exceptionMessage)
        }
        mapDelegate = mapDelegateFactoryProvider.get().create(binding.mapView.map)
        if (contactArgument != null) {
            doActionForSingleContact()
        } else {
            doActionForContacts()
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        super.onPause()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        mapDelegate?.destroyMap()
        mapDelegate = null
        super.onDestroyView()
    }

    private fun doActionForSingleContact() {
        contactArgument?.id?.also(viewModel::getContactMapById)
        viewModel.contactMap.observe(viewLifecycleOwner) { contactMap ->
            mapDelegate?.updateMap(contactMap)
        }
        binding.deleteContactMapImageView.visibility = View.VISIBLE
        binding.deleteContactMapImageView.setOnClickListener {
            contactArgument?.id?.also(viewModel::deleteContactMap)
            binding.mapView.map.mapObjects.clear()
        }
        viewModel.contactAddress.observe(viewLifecycleOwner) { address ->
            address?.also { contactAddress ->
                toastRouter.showToast(contactAddress.address)
                val contactMap = ContactMap(
                    name = contactArgument?.name.orEmpty(),
                    address = contactAddress.address,
                    latitude = chosenPoint.latitude,
                    longitude = chosenPoint.longitude,
                    id = contactArgument?.id.orEmpty()
                )
                viewModel.updateContactMap(contactMap)
            }
        }
        inputListener = object : InputListener {
            override fun onMapTap(map: Map, point: Point) {
                binding.mapView.map.mapObjects.clear()
                chosenPoint = point
                viewModel.fetchAddress(
                    point.latitude.toString(),
                    point.longitude.toString()
                )
            }

            override fun onMapLongTap(map: Map, p1: Point) = Unit
        }
        inputListener?.also { listener ->
            binding.mapView.map.addInputListener(listener)
        }
    }

    private fun doActionForContacts() {
        binding.routeImageView.visibility = View.VISIBLE
        binding.routeImageView.setOnClickListener {
            viewModel.navigateToMapPickerFragment()
        }
        viewModel.getAllContactMaps()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contactMapList.collect { contactMapList ->
                    val pointSouthWest =
                        viewModel.contactMapList.value?.firstOrNull()?.let { point ->
                            Point(point.latitude, point.longitude)
                        }

                    val pointNorthEast =
                        viewModel.contactMapList.value?.firstOrNull()?.let { point ->
                            Point(point.latitude, point.longitude)
                        }
                    mapDelegate?.paintAllContactsMap(
                        initialPointNorthEast = pointNorthEast,
                        initialPointSouthWest = pointSouthWest,
                        contactMapList = contactMapList
                    )
                }
            }
        }
        parentFragmentManager.setFragmentResultListener(
            ROUTE_MAP_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val firstContact = viewModel.contactMapList.value?.find { contactMap ->
                contactMap.id == bundle.getString(FIRST_CONTACT_BUNDLE_KEY)
            }
            val secondContact = viewModel.contactMapList.value?.find { contactMap ->
                contactMap.id == bundle.getString(SECOND_CONTACT_BUNDLE_KEY)
            }
            if (firstContact != null && secondContact != null) {
                val pointFirstContact = Point(firstContact.latitude, firstContact.longitude)
                val pointSecondContact = Point(secondContact.latitude, secondContact.longitude)
                bundle.getString(ROUTE_MAP_BUNDLE_KEY)?.also { chosenMethod ->
                    mapDelegate?.plotRoute(chosenMethod, pointFirstContact, pointSecondContact)
                }
            }
        }
    }

    companion object {
        private const val ARG: String = "arg"
        const val ROUTE_MAP_KEY = "routeMapKey"
        const val ROUTE_MAP_BUNDLE_KEY = "routeMapBundleKey"
        const val BUS_BUNDLE_PAIR = "bus"
        const val CAR_BUNDLE_PAIR = "car"
        const val FOOT_BUNDLE_PAIR = "foot"
        const val UNDERGROUND_BUNDLE_PAIR = "underground"
        const val MIXED_FORMAT_BUNDLE_PAIR = "mixedFormat"
        const val FIRST_CONTACT_BUNDLE_KEY = "firstContact"
        const val SECOND_CONTACT_BUNDLE_KEY = "secondContact"
        fun newInstance(contactMapDto: ContactMapArguments?) =
            ContactMapFragment().apply {
                arguments = bundleOf(
                    ARG to contactMapDto
                )
            }
    }
}
