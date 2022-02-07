package com.example.passport.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.passport.BuildConfig
import com.example.passport.R
import com.example.passport.database.AppDatabase
import com.example.passport.databinding.FragmentAddBinding
import com.example.passport.databinding.MyDialogBinding
import com.example.passport.entity.Malumotlar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var binding: FragmentAddBinding
    lateinit var appDatabase: AppDatabase
    lateinit var list: ArrayList<Malumotlar>

    lateinit var photoURI: Uri
    lateinit var currentImagePath: String
    private var fileAbsolutePath: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater,container,false)
        appDatabase = AppDatabase.getInstance(binding.root.context)

        list = ArrayList()
        list.addAll(appDatabase.passportDao().getAllPassport())

        binding.back.setOnClickListener {
            findNavController().popBackStack()

        }

        setSpinner()

        binding.rasm.setOnClickListener {
            setPhoto()

        }



        return binding.root
    }

    private fun setPhoto() {
        val picturesDialog = AlertDialog.Builder(binding.root.context)
        val dialog = picturesDialog.create()
        picturesDialog.setNegativeButton("Bekor qilish",{ dialogInterFace: DialogInterface, i: Int ->
            dialog.dismiss()
        })
        picturesDialog.setTitle("Kamera yoki Gallerreyani tanlang")
        val DialogItems = arrayOf("Galereyadan rasm tanlash", "Kamera orqali rasmga olish")
        picturesDialog.setItems(DialogItems){
                dialog, which ->
            when(which){
                0 -> permission_gallery()
                1 -> permission_camera()
            }
        }
        picturesDialog.show()
    }

    private fun permission_camera() {
        Dexter.withContext(binding.root.context)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    /* ... */
                    Toast.makeText(binding.root.context, "Granted", Toast.LENGTH_SHORT).show()
                    val imageFile = createImageFile()
                    photoURI = FileProvider.getUriForFile(binding.root.context,
                        BuildConfig.APPLICATION_ID,imageFile)
                    getTakeImageContent.launch(photoURI)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) { /* ... */
                    Toast.makeText(binding.root.context, "Denied", Toast.LENGTH_LONG).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) { /* ... */
                    AlertDialog.Builder(binding.root.context)
                        .setTitle("Permission")
                        .setMessage("Oka qabul qivoraqoling iltimos")
                        .setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener {
                                dialogInterface, i ->
                            dialogInterface.dismiss()
                            token?.cancelPermissionRequest()
                        })
                        .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener {
                                dialogInterface, i ->
                            dialogInterface.dismiss()
                            token?.continuePermissionRequest()
                        })
                        .show()
                }
            }).check()
    }
    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()){

            if (it) {
                binding.rasm.setImageURI(photoURI)
                val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())
                val filesDir = binding.root.context.filesDir
                val contentResolver = activity?.contentResolver
                val openInputStream = contentResolver?.openInputStream(photoURI)
                val file = File(filesDir,"image.jpg$format")
                val fileOutputStream = FileOutputStream(file)
                openInputStream?.copyTo(fileOutputStream)
                openInputStream?.close()
                fileOutputStream.close()
                fileAbsolutePath = file.absolutePath


                binding.add.setOnClickListener {
                    val alertDialog = AlertDialog.Builder(binding.root.context)
                    val dialog = alertDialog.create()
                    val dialogView = MyDialogBinding.inflate(
                        LayoutInflater.from(binding.root.context),
                        null,
                        false)

                    dialogView.ha.setOnClickListener {
                        val malumotlar = Malumotlar()
                        var name = binding.name.text.toString().trim()
                        var surname = binding.surname.text.toString().trim()
                        var fathername = binding.fatherName.text.toString().trim()
                        var region  = binding.vilSpinner.selectedItem.toString()
                        var city = binding.city.text.toString().trim()
                        var h_adress = binding.homeAddres.text.toString().trim()
                        var timepass = binding.timePass.text.toString().trim()
                        var dr_pass = binding.durationPass.text.toString().trim()
                        var gender = binding.gender.selectedItem.toString()

                        if (name.isNotEmpty() && surname.isNotEmpty() && fathername.isNotEmpty() && region != "Viloyati" && city.isNotEmpty() && h_adress.isNotEmpty() && timepass.isNotEmpty() && dr_pass.isNotEmpty() && gender != "Jinsi"){
                            malumotlar.name = name
                            malumotlar.surname = surname
                            malumotlar.fathername = fathername
                            malumotlar.region = region
                            malumotlar.city = city
                            malumotlar.homeAdress = h_adress
                            malumotlar.timePass = timepass
                            malumotlar.durationpass = dr_pass
                            malumotlar.gender = gender
                            malumotlar.image = fileAbsolutePath
                            appDatabase.passportDao().addPassport(malumotlar)
                            dialog.dismiss()
                        } else{
                            Toast.makeText(binding.root.context, "Malumotlarni aniq va to'liq kiritganingizga ishonch hosil qiling", Toast.LENGTH_SHORT).show()
                        }



                    }

                    dialogView.yoq.setOnClickListener {
                        dialog.dismiss()
                    }

                    dialog.setView(dialogView.root)
                    dialog.show()
                }


            }
        }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())
        val externalFilesDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("JPEG_$format",".jpg",externalFilesDir).apply {
            currentImagePath = absolutePath
        }
    }

    private fun permission_gallery() {
        Dexter.withContext(binding.root.context)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                /* ... */
                    Toast.makeText(binding.root.context, "Granted", Toast.LENGTH_SHORT).show()
                    pickImageFromGallery()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) { /* ... */
                    Toast.makeText(binding.root.context, "Denied", Toast.LENGTH_LONG).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) { /* ... */
                    AlertDialog.Builder(binding.root.context)
                        .setTitle("Permission")
                        .setMessage("Oka qabul qivoraqoling iltimos")
                        .setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener {
                                dialogInterface, i ->
                            dialogInterface.dismiss()
                            token?.cancelPermissionRequest()
                        })
                        .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener {
                                dialogInterface, i ->
                            dialogInterface.dismiss()
                            token?.continuePermissionRequest()
                        })
                        .show()
                }
            }).check()
    }

    private fun pickImageFromGallery() {
        getImageContent.launch("image/*")
    }
    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri ?: return@registerForActivityResult
            binding.rasm.setImageURI(uri)
            val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())

            val filesDir = binding.root.context.filesDir
            val contentResolver = activity?.contentResolver
            val openInputStream = contentResolver?.openInputStream(uri)
            val file = File(filesDir, "image.jp$format")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()




            val fileAbsolutePath = file.absolutePath
            val fileInputStream = FileInputStream(file)

            binding.add.setOnClickListener {
                val alertDialog = AlertDialog.Builder(binding.root.context)
                val dialog = alertDialog.create()
                val dialogView = MyDialogBinding.inflate(
                    LayoutInflater.from(binding.root.context),
                    null,
                    false)

                dialogView.ha.setOnClickListener {
                    val malumotlar = Malumotlar()
                    var name = binding.name.text.toString().trim()
                    var surname = binding.surname.text.toString().trim()
                    var fathername = binding.fatherName.text.toString().trim()
                    var region  = binding.vilSpinner.selectedItem.toString()
                    var city = binding.city.text.toString().trim()
                    var h_adress = binding.homeAddres.text.toString().trim()
                    var timepass = binding.timePass.text.toString().trim()
                    var dr_pass = binding.durationPass.text.toString().trim()
                    var gender = binding.gender.selectedItem.toString()

                    if (name.isNotEmpty() && surname.isNotEmpty() && fathername.isNotEmpty() && region != "Viloyati" && city.isNotEmpty() && h_adress.isNotEmpty() && timepass.isNotEmpty() && dr_pass.isNotEmpty() && gender != "Jinsi"){
                        malumotlar.name = name
                        malumotlar.surname = surname
                        malumotlar.fathername = fathername
                        malumotlar.region = region
                        malumotlar.city = city
                        malumotlar.homeAdress = h_adress
                        malumotlar.timePass = timepass
                        malumotlar.durationpass = dr_pass
                        malumotlar.gender = gender
                        malumotlar.image = fileAbsolutePath
                        appDatabase.passportDao().addPassport(malumotlar)
                        dialog.dismiss()
                    } else{
                        Toast.makeText(binding.root.context, "Malumotlarni aniq va to'liq kiritganingizga ishonch hosil qiling", Toast.LENGTH_SHORT).show()
                    }



                }

                dialogView.yoq.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.setView(dialogView.root)
                dialog.show()
            }



        }






    private fun setSpinner() {
        val region = arrayOf("Viloyati","Toshkent shaxri","Xorazm","Qashqadaryo","Surhondaryo","Buxoro","Namangan","Qoraqalpog'iston","Jizzah")
        val jins = arrayOf("Jinsi","Erkak","Ayol")

        val regionAdapter = ArrayAdapter(binding.root.context,android.R.layout.simple_spinner_dropdown_item,region)
        val genderAdapter = ArrayAdapter(binding.root.context,R.layout.spinner_item,jins)
        genderAdapter.setDropDownViewResource(R.layout.spinner_item)

        binding.vilSpinner.adapter = regionAdapter
        binding.gender.adapter = genderAdapter
    }

    companion object {
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