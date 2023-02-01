package com.example.fithpath.ui.map

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.fithpath.R
import com.example.fithpath.StopWatch
import com.example.fithpath.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import java.io.*


class MapFragment : Fragment(), LocationListener {

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private var userLocation: Location? = null
    private val locationPermissionCode = 2
    private var isLocationOn: Boolean = false
    private var _binding: FragmentMapBinding? = null

    //request codes
    private val cameraRequest = 112

    //camera
    private var frame: ImageView? = null
    private var imageUri: Uri? = null
    private val resultloadimage = 123
    private val imagecapturecode = 654

    //Kms percorridos
    private lateinit var distanceTraveled: TextView
    private var dkms: Float = 0f

    //stopwatch
    private lateinit var stopWatch: StopWatch
    private lateinit var stopWatchText: TextView

    //booleans
    private var runStarted: Boolean = false

    //botões
    private lateinit var start: AppCompatButton
    private lateinit var stop: AppCompatButton
    private lateinit var currentLocation: AppCompatButton
    private lateinit var save: AppCompatButton
    private lateinit var delete: AppCompatButton
    private lateinit var ok: AppCompatButton

    //layouts
    private lateinit var saveRun: RelativeLayout
    private lateinit var saveRunName: RelativeLayout

    //editText
    private lateinit var runName: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_map, container, false)

        //inicializar variaveis
        start = rootView.findViewById(R.id.startBtn)
        stop = rootView.findViewById(R.id.stopBtn)
        currentLocation = rootView.findViewById(R.id.currentLocBtn)
        stopWatchText = rootView.findViewById(R.id.timer)
        distanceTraveled = rootView.findViewById(R.id.kms)
        save = rootView.findViewById(R.id.saveBtn)
        delete = rootView.findViewById(R.id.deleteBtn)
        ok = rootView.findViewById(R.id.okBtn)
        runName = rootView.findViewById(R.id.runNameText)

        currentLocation.setOnClickListener {
            //pede permissões de localização
            locationPermissions()
        }

        start.setOnClickListener {
            runStarted = true
            //pede permissões de localização
            locationPermissions()

            if (isLocationOn) {
                //começa a registar o caminho da corrida
                start.isEnabled = false
                stop.isEnabled = true
                //mostra o botão stop e esconde o start
                start.visibility = View.INVISIBLE
                stop.visibility = View.VISIBLE

                startRun()

                //inicializar a classe stopWatch
                stopWatch = StopWatch(stopWatchText)
                stopWatch.start()
            }
        }

        stop.setOnClickListener {
            runStarted = false
            //para o locationListener
            locationManager.removeUpdates(this)
            start.isEnabled = true
            stop.isEnabled = false
            //mostra o start e esconde o stop
            stop.visibility = View.INVISIBLE
            start.visibility = View.VISIBLE
            //para o stopwatch
            stopWatch.stop()
            //iniciar a activity saveRun
            saveRun = rootView.findViewById(R.id.saveRun)
            saveRun.isVisible = true
        }

        save.setOnClickListener {
            //guarda a corrida com um nome
            //esconde a activity saveRun
            saveRun.isVisible = false
            //iniciar a activity runName
            saveRunName = rootView.findViewById(R.id.saveRunName)
            saveRunName.isVisible = true
        }

        ok.setOnClickListener {
            saveRunName.isVisible = false
            //guardar os valores num arraylist
            var arraylist = ArrayList<String>()
            arraylist.add("Nome:" + runName.text.toString())
            arraylist.add("Distância: " + dkms.toInt().toString())
            arraylist.add("Tempo: " + stopWatchText.text.toString())
            //guardar a arraylist num ficheiro
            val directory: File = activity!!.applicationContext.filesDir
            val file = File(directory, "runsData.txt")
            val fo = FileOutputStream(file, true)
            fo.write(arraylist.toString().toByteArray())
            fo.close()
            //reset à UI da corrida
            stopWatch.reset()
            distanceTraveled.text = "0m"
            cameraPermissions()
        }

        delete.setOnClickListener {
            //não guarda a corrida
            saveRun.isVisible = false
            //reset à UI da corrida
            stopWatch.reset()
            distanceTraveled.text = "0m"
        }

        return rootView
    }

    private fun openCamera() {
        val path = (Environment.getExternalStorageDirectory().toString())
        val appDirectory = File("$path/DCIM/FitPath")
        if (!appDirectory.isDirectory) {
            appDirectory.mkdirs()
        }
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, runName.text.toString() + ".jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_DCIM + File.separator + "FitPath"
            )
        }

        imageUri = activity!!.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, imagecapturecode)

    }

    private fun cameraPermissions() {
        val permission =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermissions(permission, cameraRequest)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraRequest) {
            openCamera()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == imagecapturecode && resultCode == Activity.RESULT_OK) {
            val bitmap = uriToBitmap(imageUri!!)
            frame?.setImageBitmap(bitmap)
        }
        if (requestCode == resultloadimage && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            val bitmap = uriToBitmap(imageUri!!)
            frame?.setImageBitmap(bitmap)
        }
    }

    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor =
                activity!!.contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun startRun() {

        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun locationPermissions() {
        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //se não tiver permissões para aceder à localização, pede ao utilizador
        if ((ContextCompat.checkSelfPermission(
                activity!!, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED)
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode
            )
        }else{
            //obtem a localização atual e faz zoom na mesma
            enableLocation()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun enableLocation() {
        // Verifica se o utilizador forneceu o acesso à localização
        if (mMap.isMyLocationEnabled) {
            //verificar se o gps está desligado
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //liga o gps
                turnOnGPS()
                isLocationOn = false
            } else {
                // Obtem a localização atual
                if (mMap.myLocation != null) {
                    userLocation = mMap.myLocation
                    isLocationOn = true
                    if (!runStarted) {
                        isLocationOn = true
                        // Faz zoom na localização atual do utilizador
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    userLocation!!.latitude, userLocation!!.longitude
                                ), 16f
                            )
                        )
                    }
                }
            }
        }
    }

    private fun turnOnGPS() {
        //Pede ao utilizador para ligar o GPS
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Gps desativado!")
        builder.setMessage("Por favor ative o GPS")
        builder.setPositiveButton(
            "OK"
        ) { _, _ -> // Abre as definições de localixação quando o utilizador clicar em ok
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        val alertDialog: Dialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        }

        mMap.uiSettings.isMyLocationButtonEnabled = false
        mMap.uiSettings.isCompassEnabled = false
    }

    override fun onLocationChanged(location: Location) {

        if (userLocation == null) {
            userLocation = location
        }

        val lastLatLng = LatLng(userLocation!!.latitude, userLocation!!.longitude)
        val thisLatLng = LatLng(location.latitude, location.longitude)

        if (userLocation != location) {
            mMap.addPolyline(
                PolylineOptions().add(lastLatLng).add(thisLatLng).jointType(2).width(25f)
                    .color(Color.RED)
            )
            //calcular distância
            dkms += userLocation!!.distanceTo(location)
            distanceTraveled.text = dkms.toInt().toString() + 'm'

            userLocation = location

            val updatedCameraPosition = CameraPosition.Builder()
                .target(thisLatLng)
                .zoom(20f)
                .bearing(location.bearing)
                .build()

            // Animate the camera to the updated position.
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(updatedCameraPosition))
        }
    }
}