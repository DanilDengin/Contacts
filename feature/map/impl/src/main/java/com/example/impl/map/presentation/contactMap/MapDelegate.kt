package com.example.impl.map.presentation.contactMap

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import com.example.common.address.domain.entity.ContactMap
import com.example.impl.map.R
import com.example.utils.delegate.unsafeLazy
import com.yandex.mapkit.Animation
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.BoundingBoxHelper
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.geometry.SubpolylineHelper
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.FilterVehicleTypes
import com.yandex.mapkit.transport.masstransit.MasstransitRouter
import com.yandex.mapkit.transport.masstransit.PedestrianRouter
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.SectionMetadata
import com.yandex.mapkit.transport.masstransit.Session
import com.yandex.mapkit.transport.masstransit.TimeOptions
import com.yandex.mapkit.transport.masstransit.TransitOptions
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlin.math.pow
import kotlin.math.sqrt

internal class MapDelegate @AssistedInject constructor(
    @Assisted private val map: Map,
    private val context: Context,
    private val toastRouter: ToastRouter
) {

    private val mapObjects: MapObjectCollection by unsafeLazy { map.mapObjects }

    private val contactMapImage by unsafeLazy {
        ImageProvider.fromResource(
            context,
            R.drawable.ic_person_pin
        )
    }

    private val networkErrorMessage by unsafeLazy {
        context.getString(com.example.ui.R.string.network_exception_toast)
    }

    private val roadNotFountMessage by unsafeLazy {
        context.getString(com.example.ui.R.string.road_not_found_toast)
    }

    private var inputListener: InputListener? = null

    private var routeListener: Session.RouteListener? = null

    private var masstransitRouter: MasstransitRouter? = null

    private var pedestrianRouter: PedestrianRouter? = null

    private var drivingRouteListener: DrivingSession.DrivingRouteListener? = null

    private var drivingRouter: DrivingRouter? = null

    fun destroyMap() {
        inputListener = null
        routeListener = null
        drivingRouteListener = null
        masstransitRouter = null
        pedestrianRouter = null
        drivingRouter = null
    }

    fun updateMap(contactMap: ContactMap?) {
        contactMap?.also {
            drawContact(contactMap = contactMap)
            val point = Point(
                contactMap.latitude,
                contactMap.longitude
            )
            map.move(
                CameraPosition(point, ZOOM, AZIMUTH, TILT),
                Animation(Animation.Type.SMOOTH, ZOOM_DURATION),
                null
            )
        }
    }

    private fun drawContact(contactMap: ContactMap) {
        val point = Point(
            contactMap.latitude,
            contactMap.longitude
        )
        mapObjects.addCollection().addPlacemark(point, contactMapImage)
        val description = "${contactMap.name} [${contactMap.address}]"
        val textView = TextView(context)
        val params: ViewGroup.LayoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        textView.apply {
            layoutParams = params
            setTextColor(Color.BLACK)
            textSize = CONTACT_DESCRIPTION_TEXT_SIZE
            text = description
        }
        val viewProvider = ViewProvider(textView)
        mapObjects.addCollection().addPlacemark(point, viewProvider)
    }

    fun paintAllContactsMap(
        initialPointSouthWest: Point?,
        initialPointNorthEast: Point?,
        contactMapList: List<ContactMap>?
    ) {
        var pointSouthWest = initialPointSouthWest
        var pointNorthEast = initialPointNorthEast
        var distanceSouthWest = pointSouthWest?.let { point ->
            sqrt(point.latitude.pow(2) + point.longitude.pow(2))
        }
        var distanceNorthEast = pointNorthEast?.let { point ->
            sqrt(point.latitude.pow(2) + point.longitude.pow(2))
        }
        contactMapList?.forEach { contactMap ->
            drawContact(contactMap)
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
            val boundingBox = BoundingBox(
                requireNotNull(pointSouthWest),
                requireNotNull(pointNorthEast)
            )
            map.also { currentMap ->
                var cameraPosition = currentMap.cameraPosition(boundingBox)
                cameraPosition = CameraPosition(
                    cameraPosition.target,
                    cameraPosition.zoom - ROUTE_STRONG_ZOOM,
                    cameraPosition.azimuth,
                    cameraPosition.tilt
                )
                currentMap.move(
                    cameraPosition,
                    Animation(Animation.Type.SMOOTH, ZOOM_DURATION),
                    null
                )
            }
        }
    }

    fun plotRoute(
        chosenMethod: String,
        pointFirstContact: Point,
        pointSecondContact: Point
    ) {
        when (chosenMethod) {
            ContactMapFragment.BUS_BUNDLE_PAIR -> {
                buildPath(
                    TransitOptions(FilterVehicleTypes.TROLLEYBUS.value, TimeOptions()),
                    pointFirstContact,
                    pointSecondContact
                )
            }
            ContactMapFragment.CAR_BUNDLE_PAIR -> {
                buildCarPath(
                    pointFirstContact,
                    pointSecondContact
                )
            }
            ContactMapFragment.FOOT_BUNDLE_PAIR -> {
                buildFootPath(
                    pointFirstContact,
                    pointSecondContact
                )
            }
            ContactMapFragment.UNDERGROUND_BUNDLE_PAIR -> {
                buildPath(
                    TransitOptions(FilterVehicleTypes.RAILWAY.value, TimeOptions()),
                    pointFirstContact,
                    pointSecondContact
                )
            }
            ContactMapFragment.MIXED_FORMAT_BUNDLE_PAIR -> {
                buildPath(
                    TransitOptions(FilterVehicleTypes.NONE.value, TimeOptions()),
                    pointFirstContact,
                    pointSecondContact
                )
            }
        }
    }

    private fun buildPath(transitOptions: TransitOptions, startPoint: Point, endPoint: Point) {
        initRouteListener()
        val points: MutableList<RequestPoint> = ArrayList()
        points.add(RequestPoint(startPoint, RequestPointType.WAYPOINT, null))
        points.add(RequestPoint(endPoint, RequestPointType.WAYPOINT, null))
        masstransitRouter = TransportFactory.getInstance().createMasstransitRouter()
        routeListener?.also { listener ->
            masstransitRouter?.requestRoutes(points, transitOptions, listener)
        }
    }

    private fun buildFootPath(startPoint: Point, endPoint: Point) {
        initRouteListener()
        val points: MutableList<RequestPoint> = ArrayList()
        points.add(RequestPoint(startPoint, RequestPointType.WAYPOINT, null))
        points.add(RequestPoint(endPoint, RequestPointType.WAYPOINT, null))
        pedestrianRouter = TransportFactory.getInstance().createPedestrianRouter()
        routeListener?.also { listener ->
            pedestrianRouter?.requestRoutes(points, TimeOptions(), listener)
        }
    }

    private fun buildCarPath(startPoint: Point, endPoint: Point) {
        drivingRouteListener = object : DrivingSession.DrivingRouteListener {
            override fun onDrivingRoutes(routes: List<DrivingRoute>) {
                if (routes.isNotEmpty()) {
                    val route = routes.first()
                    mapObjects.addPolyline(route.geometry)
                    val boundingBox = BoundingBoxHelper.getBounds(route.geometry)
                    var cameraPosition = map.cameraPosition(boundingBox)
                    cameraPosition = CameraPosition(
                        cameraPosition.target,
                        cameraPosition.zoom - ROUTE_SMALL_ZOOM,
                        cameraPosition.azimuth,
                        cameraPosition.tilt
                    )
                    map.move(
                        cameraPosition,
                        Animation(Animation.Type.SMOOTH, ZOOM_DURATION),
                        null
                    )
                } else {
                    toastRouter.showToast(roadNotFountMessage)
                }
            }

            override fun onDrivingRoutesError(error: Error) {
                toastRouter.showToast(networkErrorMessage)
            }
        }
        val requestPoints: ArrayList<RequestPoint> = ArrayList()
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
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
        drivingRouteListener?.also { listener ->
            drivingRouter?.requestRoutes(
                requestPoints,
                DrivingOptions(),
                VehicleOptions(),
                listener
            )
        }
    }

    private fun initRouteListener() {
        routeListener = object : Session.RouteListener {
            override fun onMasstransitRoutes(routesList: MutableList<Route>) {
                if (routesList.size > 0) {
                    val boundingBox =
                        BoundingBoxHelper.getBounds(routesList.firstOrNull()?.geometry)
                    var cameraPosition = map.cameraPosition(boundingBox)
                    cameraPosition = CameraPosition(
                        cameraPosition.target,
                        cameraPosition.zoom - ROUTE_SMALL_ZOOM,
                        cameraPosition.azimuth,
                        cameraPosition.tilt
                    )
                    map.move(
                        cameraPosition,
                        Animation(Animation.Type.SMOOTH, ZOOM_DURATION),
                        null
                    )
                    routesList.firstOrNull()?.sections?.forEach { section ->
                        drawSection(
                            section.metadata.data,
                            SubpolylineHelper.subpolyline(
                                routesList.firstOrNull()?.geometry,
                                section.geometry
                            )
                        )
                    }
                } else {
                    toastRouter.showToast(roadNotFountMessage)
                }
            }

            override fun onMasstransitRoutesError(error: Error) {
                toastRouter.showToast(networkErrorMessage)
            }
        }
    }

    private fun drawSection(
        data: SectionMetadata.SectionData,
        geometry: Polyline
    ) {
        val polylineMapObject = mapObjects.addPolyline(geometry)
        val dataTransport = data.transports
        dataTransport?.forEach { transport ->
            transport.line.style?.color?.also { color ->
                polylineMapObject.setStrokeColor(
                    color.or(Color.BLACK)
                )
            }
            val knownVehicleTypes: HashSet<String> = HashSet()
            knownVehicleTypes.add(VEHICLE_TYPE_BUS)
            knownVehicleTypes.add(VEHICLE_TYPE_TRAMWAY)
            var vehicleTypes: String? = null
            transport.line.vehicleTypes.forEach { type ->
                vehicleTypes = knownVehicleTypes.find { it == type }
            }
            when (vehicleTypes.orEmpty()) {
                VEHICLE_TYPE_BUS -> polylineMapObject.setStrokeColor(Color.GREEN)
                VEHICLE_TYPE_TRAMWAY -> polylineMapObject.setStrokeColor(Color.RED)
                else -> polylineMapObject.setStrokeColor(Color.BLUE)
            }
        }
        if (dataTransport == null) {
            polylineMapObject.setStrokeColor(Color.BLACK)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted map: Map): MapDelegate
    }

    private companion object {
        const val ZOOM_DURATION = 1f
        const val ROUTE_SMALL_ZOOM = 0.4f
        const val ROUTE_STRONG_ZOOM = 0.8f
        const val CONTACT_DESCRIPTION_TEXT_SIZE = 8f
        const val ZOOM = 17.4f
        const val TILT = 0.0f
        const val AZIMUTH = 0.0f
        const val VEHICLE_TYPE_BUS = "bus"
        const val VEHICLE_TYPE_TRAMWAY = "tramway"
    }
}
