package com.example.a651


import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.concurrent.futures.await
import androidx.core.content.ContextCompat
import androidx.core.text.set
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.a651.DB.MyDB
import com.example.a651.databinding.CustomdialogBinding
import com.example.a651.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.Exception
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentAddBinding
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    lateinit var myDb:MyDB
    private var imageCapture:ImageCapture?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       binding = FragmentAddBinding.inflate(inflater,container,false)

        myDb = MyDB(binding.root.context)
        var serializable:Usercontact? = null

        try {
            serializable = arguments?.getSerializable("key") as Usercontact
        }catch (e:Exception){
            serializable = null
        }


        if (serializable != null){
            binding.name.setText(serializable.name)
            binding.tavsifi.setText(serializable.tavsif)
            var listtab = ArrayList<String>()
            for (i in myDb.getalltab()){
                listtab.add(i.tab_name!!)
            }
            var adapterspin = ArrayAdapter<String>(binding.root.context,android.R.layout.simple_spinner_item,listtab)
            binding.tabName.adapter = adapterspin
            binding.tabName.setSelection(adapterspin.getPosition(listtab[serializable.tableId!!]))
            binding.addImage.setImageURI(serializable.image)

        }else spinnerAdapter()

        binding.addImage.setOnClickListener {

            if (!checkReadContactsPermission(binding.root.context, android.Manifest.permission.CAMERA) &&
                !checkReadContactsPermission(binding.root.context, multiplePermissionsLisName)
                ) {
                requestPermissions()
            }else{

                custdialog()


            }


        }

        binding.btnAddBolm.setOnClickListener {
            binding.addBolim.visibility = View.VISIBLE
            binding.spinnir.visibility = View.GONE
        }

        binding.btnClocBolm.setOnClickListener {
            binding.addBolim.visibility = View.GONE
            binding.spinnir.visibility = View.VISIBLE
        }

        binding.btnSave.setOnClickListener {
            var tabid = -1
            if (binding.addBolim.isVisible&&binding.addBolimName.text.trim()!=""){
                myDb.addtab(binding.addBolimName.text.trim().toString())
                tabid = myDb.getalltab().size-1
            }else if (binding.spinnir.isVisible){
                tabid = binding.tabName.selectedItemPosition-1
            }

            var name = binding.name.text.toString().trim()
            var tavsifi = binding.tavsifi.text.toString().trim()

            var image = imageUri



        if (tabid>=0&&image!=null&&name!=""&&tavsifi!=""&&serializable==null){

                myDb.addypx(Usercontact(null,name,tavsifi,image,0,tabid))
                Toast.makeText(binding.root.context, "Ma'lumot saqlandi", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_homFragment)

            }else if (name!=""&&tavsifi!=""&&serializable!=null){
                if (image==null){
                    image = serializable.image
                }
            myDb.editypx(Usercontact(serializable.id,name,tavsifi,image,serializable.save,tabid+1))
            Toast.makeText(binding.root.context, "Ma'lumot o'zgartirildi", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_homFragment)

            }else {
                Toast.makeText(
                    binding.root.context,
                    "Barcha maydonlarni to'ldirish shart",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val navController = findNavController()
        val appBarConfig = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfig)


        super.onViewCreated(view, savedInstanceState)
    }


    fun spinnerAdapter(){
        var tablist = ArrayList<String>()
        tablist.add("Tanlash")
        for (i in myDb.getalltab()){
            tablist.add(i.tab_name!!)
        }
        var adapterspin = ArrayAdapter<String>(binding.root.context,android.R.layout.simple_spinner_item,tablist)
        binding.tabName.adapter = adapterspin
    }




    fun custdialog() {



        myDb = MyDB(binding.root.context)
        var dialog = BottomSheetDialog(binding.root.context)
        var layoutview = CustomdialogBinding.inflate(layoutInflater)
        dialog.setContentView(layoutview.root)

        if (!checkReadContactsPermission(binding.root.context, android.Manifest.permission.CAMERA) &&
            !checkReadContactsPermission(binding.root.context, multiplePermissionsLisName)
            ) {
            requestPermissions()
        }else{

            lifecycleScope.launch {
                startCamera(layoutview.previewView,CameraSelector.DEFAULT_BACK_CAMERA)
            }

        }

        dialog.show()

        layoutview.previewView.setOnClickListener {
            dialog.dismiss()
            var count = false

            if (!checkReadContactsPermission(binding.root.context, android.Manifest.permission.CAMERA) &&
                !checkReadContactsPermission(binding.root.context, multiplePermissionsLisName)
                ) {
                requestPermissions()
            }else{

                lifecycleScope.launch {
                    startCamera(binding.previewViewCamera,CameraSelector.DEFAULT_BACK_CAMERA)
                }

            }

            binding.layout1.visibility = View.GONE
            binding.layout2.visibility = View.VISIBLE

            binding.btnCameraFront.setOnClickListener {

                if (count){
                    lifecycleScope.launch {
                        startCamera(binding.previewViewCamera,CameraSelector.DEFAULT_BACK_CAMERA)
                    }
                    count = false
                }else{
                    lifecycleScope.launch {
                        startCamera(binding.previewViewCamera,CameraSelector.DEFAULT_FRONT_CAMERA)
                    }
                    count = true
                }


            }

            binding.btnCamera.setOnClickListener {
                takePhoto()
                binding.layout1.visibility = View.VISIBLE
                binding.layout2.visibility = View.GONE
            }

            binding.btnCameraGallery.setOnClickListener {
                openGallery()
                binding.layout1.visibility = View.VISIBLE
                binding.layout2.visibility = View.GONE
            }
        }


        layoutview.btnGallery.setOnClickListener {
            openGallery()
            dialog.dismiss()
        }



    }


    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Handle the selected image URI here
            val selectedImageUri = data.data
            // Do something with the URI, such as displaying it in an ImageView
            binding.addImage.setImageURI(data.data)
            imageUri = selectedImageUri
        }


    }



    fun checkReadContactsPermission(context: Context,permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    private val multiplePermissionsLisName = if (Build.VERSION.SDK_INT>=33){
            android.Manifest.permission.READ_MEDIA_IMAGES
    }else android.Manifest.permission.READ_EXTERNAL_STORAGE
    private fun requestPermissions() {
        // pastdagi satr joriy faoliyatda ruxsat so'rash uchun ishlatiladi.
        // bu usul ish vaqti ruxsatnomalarida xatoliklarni hal qilish uchun ishlatiladi
        Dexter.withContext(binding.root.context)
            // pastdagi satr ilovamizda talab qilinadigan ruxsatlar sonini so'rash uchun ishlatiladi.
            .withPermissions(
                multiplePermissionsLisName,
                android.Manifest.permission.CAMERA
        )
            // ruxsatlarni qo'shgandan so'ng biz tinglovchi bilan usulni chaqiramiz.
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    // bu usul barcha ruxsatlar berilganda chaqiriladi
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        // hozir ishlayapsizmi
                        Toast.makeText(binding.root.context, "Barcha ruxsatlar berilgan..", Toast.LENGTH_SHORT).show()

                        if (checkReadContactsPermission(binding.root.context,android.Manifest.permission.CAMERA)) {
                           custdialog()
                            // Ruxsat berilgan, kontakt ma'lumotlariga kirishingiz mumkin
                            // Kerakli harakatlar tugallanishi uchun shu yerga kod yozing
                        }

                    }
                    // har qanday ruxsatni doimiy ravishda rad etishni tekshiring
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                        // ruxsat butunlay rad etilgan, biz foydalanuvchiga dialog xabarini ko'rsatamiz.
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(list: List<PermissionRequest>, permissionToken: PermissionToken) {
                    // foydalanuvchi ba'zi ruxsatlarni berib, ba'zilarini rad qilganda bu usul chaqiriladi.
                    permissionToken.continuePermissionRequest()
                }
            }).withErrorListener {
                // biz xato xabari uchun tost xabarini ko'rsatmoqdamiz.
                Toast.makeText(binding.root.context, "Error occurred! ", Toast.LENGTH_SHORT).show()
            }
            // pastdagi satr bir xil mavzudagi ruxsatlarni ishga tushirish va ruxsatlarni tekshirish uchun ishlatiladi
            .onSameThread().check()

    }

    // quyida poyabzal sozlash dialog usuli
    // dialog xabarini ko'rsatish uchun ishlatiladi.
    private fun showSettingsDialog() {
        // biz ruxsatlar uchun ogohlantirish dialogini ko'rsatmoqdamiz
        val builder = AlertDialog.Builder(binding.root.context)

        // pastdagi satr ogohlantirish dialogining sarlavhasidir.
        builder.setTitle("Need Permissions")

        // pastdagi satr bizning muloqotimiz uchun xabardir
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            // bu usul musbat tugmani bosganda va shit tugmasini bosganda chaqiriladi
            // biz foydalanuvchini ilovamizdan ilovamiz sozlamalari sahifasiga yo'naltirmoqdamiz.
            dialog.cancel()
            // quyida biz foydalanuvchini qayta yo'naltirish niyatimiz.
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", null, "AddFragment")
            intent.data = uri
            startActivityForResult(intent, 101)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            // bu usul foydalanuvchi salbiy tugmani bosganda chaqiriladi.
            dialog.cancel()
        }
        // dialog oynamizni ko'rsatish uchun quyidagi qatordan foydalaniladi
        builder.show()
    }



    private suspend fun startCamera(previewViewCamera:PreviewView,cameraSelector:CameraSelector){
        val cameraProvider = ProcessCameraProvider.getInstance(binding.root.context).await()

        val preview = Preview.Builder().build()

        preview.setSurfaceProvider(previewViewCamera.surfaceProvider) // TODO: 111111

        imageCapture = ImageCapture.Builder().build()


        try {

            cameraProvider.unbindAll()
            var camera = cameraProvider.bindToLifecycle(
                this,cameraSelector,preview,imageCapture
            )

        } catch (e:Exception){
            Toast.makeText(binding.root.context, "UseCase-ni ulash amalga oshmadi", Toast.LENGTH_SHORT).show()
        }

    }





    fun takePhoto(){

        val name = SimpleDateFormat(FILNAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())


        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME,name)
            put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg")
            if (Build.VERSION.SDK_INT>Build.VERSION_CODES.P){
                put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/CameraX-Image")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(binding.root.context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()


        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(binding.root.context),
            object : ImageCapture.OnImageSavedCallback{

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "suratga olishdagi xatolik: ${exception.message}",exception )
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val msg = "Suratga olish muvaffaqiyatli yakunlandi${outputFileResults.savedUri}"
                    imageUri = outputFileResults.savedUri                                                // TODO: 222222
                    binding.addImage.setImageURI(outputFileResults.savedUri)
                    Toast.makeText(binding.root.context, "$msg", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "$msg")
                }



            }
        )

    }


    companion object {


        private const val TAG = "CameaXApp"
        private const val FILNAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS = mutableListOf(
            android.Manifest.permission.CAMERA
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }.toTypedArray()
        fun hasPermissions(context: Context) = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED
        }



        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}