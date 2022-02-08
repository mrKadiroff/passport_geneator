package com.example.passport.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.passport.R
import com.example.passport.databinding.FragmentCitizensBinding
import com.example.passport.databinding.FragmentResultBinding
import com.example.passport.entity.Malumotlar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultFragment : Fragment() {
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
    lateinit var binding: FragmentResultBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(inflater,container,false)



        setValue()



        return binding.root
    }

    private fun setValue() {
        val valuee = arguments?.getSerializable("malum") as Malumotlar
        binding.teks.text = "${valuee.name} ${valuee.surname}"
        binding.rasm.setImageURI(Uri.parse(valuee.image))
        binding.ism.text = "${valuee.surname} ${valuee.name} ${valuee.fathername}"
        binding.vil.text = "Viloyati: ${valuee.region}"
        binding.shahri.text = "Shahri: ${valuee.city}"
        binding.timee.text = "Passport olgan vaqti: ${valuee.timePass}"
        binding.muddati.text = "Passport muddati: ${valuee.durationpass}"
        binding.pol.text = "Jinsi: ${valuee.gender}"
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}