package com.example.impl.map.presentation.contactMap

import com.example.impl.map.R as FeatureRes
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.di.dependency.findFeatureExternalDeps
import com.example.impl.map.databinding.FragmentMapBinding
import com.example.impl.map.domain.entity.ContactAddress
import com.example.impl.map.domain.entity.ContactMap
import com.example.impl.map.presentation.MapComponentDependenciesProvider
import com.example.impl.map.presentation.MapComponentViewModel
import com.example.impl.map.presentation.contactMapRoutePicker.ContactMapException
import com.example.mvvm.getRootViewModel
import com.example.mvvm.viewModel
import com.example.ui.R
import com.example.utils.delegate.unsafeLazy
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingSession.DrivingRouteListener
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.BoundingBoxHelper
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.geometry.SubpolylineHelper
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.FilterVehicleTypes
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.SectionMetadata.SectionData
import com.yandex.mapkit.transport.masstransit.Session.RouteListener
import com.yandex.mapkit.transport.masstransit.TimeOptions
import com.yandex.mapkit.transport.masstransit.TransitOptions
import com.yandex.mapkit.transport.masstransit.Transport
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import javax.inject.Inject
import javax.inject.Provider
import kotlin.math.pow
import kotlin.math.sqrt
import kotlinx.coroutines.launch

class ContactMapFragment : Fragment(FeatureRes.layout.fragment_map), DrivingRouteListener,
    RouteListener {

    @Inject
    lateinit var viewModelProvider: Provider<ContactMapViewModel>

    private val contactMapImage by unsafeLazy {
        ImageProvider.fromResource(
            requireContext(),
            FeatureRes.drawable.ic_person_pin
        )
    }

    private val binding by viewBinding(FragmentMapBinding::bind)

    private val viewModel by unsafeLazy { viewModel { viewModelProvider.get() } }

    private val mapObjects by unsafeLazy { binding.mapView.map.mapObjects }

    private var contactArgument: com.example.entity.ContactMapArguments? = null

    private var chosenPoint = Point()

    private val networkErrorMessage by unsafeLazy { getString(R.string.network_exception_toast) }

    private val roadNotFountMessage by unsafeLazy { getString(R.string.road_not_found_toast) }

    override fun onAttach(context: Context) {
        MapComponentDependenciesProvider.featureDependencies = findFeatureExternalDeps()
        getRootViewModel<MapComponentViewModel>().component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contextNotNull = requireContext()
        TransportFactory.initialize(contextNotNull)
        DirectionsFactory.initialize(contextNotNull)
        MapKitFactory.initialize(contextNotNull)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.contact_map_toolbar)
        contactArgument = if (Build.VERSION.SDK_INT >= TIRAMISU) {
            arguments?.getParcelable(ARG, com.example.entity.ContactMapArguments::class.java)
        } else {
            arguments?.getParcelable(ARG)
        }
        viewModel.exceptionState.observe(viewLifecycleOwner, ::showExceptionToast)

        if (contactArgument != null) {
            doActionForSingleContact()
        } else {
            doActionForContacts()
        }
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDrivingRoutes(routes: List<DrivingRoute>) {
        if (routes.isNotEmpty()) {
            val route = routes.first()
            mapObjects.addPolyline(route.geometry)
            val boundingBox = BoundingBoxHelper.getBounds(route.geometry)
            var cameraPosition = binding.mapView.map.cameraPosition(boundingBox)
            cameraPosition = CameraPosition(
                cameraPosition.target,
                cameraPosition.zoom - ROUTE_SMALL_ZOOM,
                cameraPosition.azimuth,
                cameraPosition.tilt
            )
            binding.mapView.map.move(
                cameraPosition,
                Animation(Animation.Type.SMOOTH, ZOOM_DURATION),
                null
            )
        } else {
            Toast.makeText(requireContext(), roadNotFountMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDrivingRoutesError(error: Error) {
        Toast.makeText(requireContext(), networkErrorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onMasstransitRoutes(routesList: MutableList<Route>) {
        if (routesList.size > 0) {
            val boundingBox = BoundingBoxHelper.getBounds(routesList.firstOrNull()?.geometry)
            var cameraPosition = binding.mapView.map.cameraPosition(boundingBox)
            cameraPosition = CameraPosition(
                cameraPosition.target,
                cameraPosition.zoom - ROUTE_SMALL_ZOOM,
                cameraPosition.azimuth,
                cameraPosition.tilt
            )
            binding.mapView.map.move(
                cameraPosition,
                Animation(Animation.Type.SMOOTH, ZOOM_DURATION),
                null
            )
            routesList.firstOrNull()?.sections?.also { sectionList ->
                for (section in sectionList) {
                    drawSection(
                        section.metadata.data,
                        SubpolylineHelper.subpolyline(
                            routesList.firstOrNull()?.geometry, section.geometry
                        )
                    )
                }
            }
        } else {
            Toast.makeText(requireContext(), roadNotFountMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMasstransitRoutesError(error: Error) {
        Toast.makeText(requireContext(), networkErrorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun doActionForSingleContact() {
        binding.deleteContactMapImageView.visibility = View.VISIBLE
        binding.deleteContactMapImageView.setOnClickListener {
            contactArgument?.id?.also(viewModel::deleteContactMap)
            mapObjects.clear()
        }
        viewModel.contactMap.observe(viewLifecycleOwner, ::updateMap)
        viewModel.contactAddress.observe(viewLifecycleOwner, ::updateContactMap)
        contactArgument?.id?.also(viewModel::getContactMapById)
        binding.mapView.map.addInputListener(object : InputListener {
            override fun onMapTap(map: Map, point: Point) {
                mapObjects.clear()
                chosenPoint = point
                viewModel.fetchAddress(
                    point.latitude.toString(),
                    point.longitude.toString()
                )
            }

            override fun onMapLongTap(map: Map, p1: Point) = Unit
        })
    }

    private fun doActionForContacts() {
        binding.routeImageView.visibility = View.VISIBLE
        binding.routeImageView.setOnClickListener {
            navigateToMapPickerFragment()
        }
        viewModel.getAllContactMaps()
        paintAllContactsMap()
        getFragmentResult()
    }

    private fun getFragmentResult() {
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
                when (bundle.getString(ROUTE_MAP_BUNDLE_KEY)) {
                    BUS_BUNDLE_PAIR -> {
                        plotRoute(
                            TransitOptions(FilterVehicleTypes.TROLLEYBUS.value, TimeOptions()),
                            pointFirstContact,
                            pointSecondContact
                        )
                    }
                    CAR_BUNDLE_PAIR -> {
                        plotRouteByCar(
                            pointFirstContact,
                            pointSecondContact
                        )
                    }
                    FOOT_BUNDLE_PAIR -> {
                        plotRouteByFoot(
                            pointFirstContact,
                            pointSecondContact
                        )
                    }
                    UNDERGROUND_BUNDLE_PAIR -> {
                        plotRoute(
                            TransitOptions(FilterVehicleTypes.RAILWAY.value, TimeOptions()),
                            pointFirstContact,
                            pointSecondContact
                        )
                    }
                    MIXED_FORMAT_BUNDLE_PAIR -> {
                        plotRoute(
                            TransitOptions(FilterVehicleTypes.NONE.value, TimeOptions()),
                            pointFirstContact,
                            pointSecondContact
                        )
                    }
                }
            }
        }
    }

    private fun updateContactMap(address: ContactAddress?) {
        if (address != null) {
            showAddressToast(address = address)
            val contactMap = ContactMap(
                name = contactArgument?.name.orEmpty(),
                address = address.address,
                latitude = chosenPoint.latitude,
                longitude = chosenPoint.longitude,
                id = contactArgument?.id.orEmpty()
            )
            viewModel.updateContactMap(contactMap)
        }
    }

    private fun navigateToMapPickerFragment() {
        viewModel.navigateToMapPickerFragment()
    }

    private fun paintAllContactsMap() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contactMapList.collect { contactMapList ->
                    var pointSouthWest =
                        viewModel.contactMapList.value?.firstOrNull()?.let { point ->
                            Point(point.latitude, point.longitude)
                        }
                    var distanceSouthWest = pointSouthWest?.let { point ->
                        sqrt(point.latitude.pow(2) + point.longitude.pow(2))
                    }

                    var pointNorthEast =
                        viewModel.contactMapList.value?.firstOrNull()?.let { point ->
                            Point(point.latitude, point.longitude)
                        }
                    var distanceNorthEast = pointNorthEast?.let { point ->
                        sqrt(point.latitude.pow(2) + point.longitude.pow(2))
                    }
                    contactMapList?.forEach { contactMap ->
                        drawPoint(contactMap)
                        val distance = contactMap.let { point ->
                            sqrt(point.latitude.pow(2) + point.longitude.pow(2))
                        }
                        distanceSouthWest?.also { distanceSouthWestComparable ->
                            distanceNorthEast?.also { distanceNorthEastComparable ->
                                if (distance < distanceSouthWestComparable) {
                                    pointSouthWest = Point(
                                        contactMap.latitude,
                                        contactMap.longitude
                                    )
                                    distanceSouthWest = distance
                                }
                                if (distance > distanceNorthEastComparable) {
                                    pointNorthEast = Point(
                                        contactMap.latitude,
                                        contactMap.longitude
                                    )
                                    distanceNorthEast = distance
                                }
                            }
                        }
                    }
                    if (pointSouthWest != null && pointNorthEast != null) {
                        moveCameraToContactMapList(
                            requireNotNull(pointSouthWest),
                            requireNotNull(pointNorthEast)
                        )
                    }
                }
            }
        }
    }

    private fun moveCameraToContactMapList(pointSouthWest: Point, pointNorthEast: Point) {
        val boundingBox = BoundingBox(pointSouthWest, pointNorthEast)
        var cameraPosition = binding.mapView.map.cameraPosition(boundingBox)
        cameraPosition = CameraPosition(
            cameraPosition.target,
            cameraPosition.zoom - ROUTE_STRONG_ZOOM,
            cameraPosition.azimuth,
            cameraPosition.tilt
        )
        binding.mapView.map.move(
            cameraPosition,
            Animation(Animation.Type.SMOOTH, ZOOM_DURATION),
            null
        )
    }

    private fun updateMap(contactMap: ContactMap?) {
        if (contactMap != null) {
            drawPoint(contactMap = contactMap)
            val point = Point(
                contactMap.latitude,
                contactMap.longitude
            )
            binding.mapView.map.move(
                CameraPosition(point, ZOOM, AZIMUTH, TILT),
                Animation(Animation.Type.SMOOTH, ZOOM_DURATION),
                null
            )
        }
    }

    private fun drawPoint(contactMap: ContactMap) {
        val point = Point(
            contactMap.latitude,
            contactMap.longitude
        )
        mapObjects.addCollection().addPlacemark(point, contactMapImage)
        val description = "${contactMap.name} [${contactMap.address}]"
        showContactAddress(point, description)
    }

    private fun showContactAddress(point: Point, description: String) {
        val textView = TextView(requireContext())
        val params: ViewGroup.LayoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        textView.apply {
            layoutParams = params
            setTextColor(Color.BLACK)
            textSize = 8f
            text = description
        }
        val viewProvider = ViewProvider(textView)
        mapObjects.addCollection().addPlacemark(point, viewProvider)
    }

    private fun showAddressToast(address: ContactAddress) {
        Toast.makeText(
            requireContext(),
            address.address,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showExceptionToast(contactMapException: ContactMapException) {
        val exceptionMessage = when (contactMapException) {
            ContactMapException.SERVER_EXCEPTION -> getString(R.string.server_exception_toast)
            ContactMapException.NETWORK_EXCEPTION -> getString(R.string.network_exception_toast)
            ContactMapException.FATAL_EXCEPTION -> getString(R.string.exception_toast)
        }
        Toast.makeText(requireContext(), exceptionMessage, Toast.LENGTH_LONG).show()
    }

    private fun drawSection(
        data: SectionData,
        geometry: Polyline
    ) {
        val polylineMapObject = mapObjects.addPolyline(geometry)
        val dataTransport = data.transports
        if (dataTransport != null) {
            for (transport in dataTransport) {
                if (transport.line.style != null) {
                    transport.line.style?.color?.let { color ->
                        polylineMapObject.setStrokeColor(
                            color.or(Color.BLACK)
                        )
                    }
                    return
                }
            }

            val knownVehicleTypes: HashSet<String> = HashSet()
            knownVehicleTypes.add(VEHICLE_TYPE_BUS)
            knownVehicleTypes.add(VEHICLE_TYPE_TRAMWAY)
            for (transport in requireNotNull(data.transports)) {
                val sectionVehicleType: String =
                    getVehicleType(transport, knownVehicleTypes).orEmpty()
                if (sectionVehicleType == VEHICLE_TYPE_BUS) {
                    polylineMapObject.setStrokeColor(Color.GREEN)
                    return
                } else if (sectionVehicleType == VEHICLE_TYPE_TRAMWAY) {
                    polylineMapObject.setStrokeColor(Color.RED)
                    return
                }
            }
            polylineMapObject.setStrokeColor(Color.BLUE)
        } else {
            polylineMapObject.setStrokeColor(Color.BLACK)
        }
    }

    private fun getVehicleType(transport: Transport, knownVehicleTypes: HashSet<String>): String? {
        for (type in transport.line.vehicleTypes) {
            if (knownVehicleTypes.contains(type)) {
                return type
            }
        }
        return null
    }

    private fun plotRoute(transitOptions: TransitOptions, startPoint: Point, endPoint: Point) {
        val points: MutableList<RequestPoint> = ArrayList()
        points.add(RequestPoint(startPoint, RequestPointType.WAYPOINT, null))
        points.add(RequestPoint(endPoint, RequestPointType.WAYPOINT, null))
        val mtRouter = TransportFactory.getInstance().createMasstransitRouter()
        mtRouter.requestRoutes(points, transitOptions, this)
    }

    private fun plotRouteByCar(startPoint: Point, endPoint: Point) {
        val requestPoints: ArrayList<RequestPoint> = ArrayList()
        val drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        requestPoints.add(
            RequestPoint(
                startPoint,
                RequestPointType.WAYPOINT,
                null
            )
        )
        requestPoints.add(
            RequestPoint(
                endPoint,
                RequestPointType.WAYPOINT,
                null
            )
        )
        drivingRouter.requestRoutes(requestPoints, DrivingOptions(), VehicleOptions(), this)
    }

    private fun plotRouteByFoot(startPoint: Point, endPoint: Point) {
        val points: MutableList<RequestPoint> = ArrayList()
        points.add(RequestPoint(startPoint, RequestPointType.WAYPOINT, null))
        points.add(RequestPoint(endPoint, RequestPointType.WAYPOINT, null))
        val mtRouter = TransportFactory.getInstance().createPedestrianRouter()
        mtRouter.requestRoutes(points, TimeOptions(), this)
    }

    companion object {
        private const val ROUTE_SMALL_ZOOM = 0.4f
        private const val ROUTE_STRONG_ZOOM = 0.8f
        private const val ZOOM_DURATION = 1f
        private const val VEHICLE_TYPE_BUS = "bus"
        private const val VEHICLE_TYPE_TRAMWAY = "tramway"
        private const val ZOOM = 17.4f
        private const val TILT = 0.0f
        private const val AZIMUTH = 0.0f
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
        fun newInstance(contactMapDto: com.example.entity.ContactMapArguments?) =
            ContactMapFragment().apply {
                arguments = bundleOf(
                    ARG to contactMapDto
                )
            }
    }
}
