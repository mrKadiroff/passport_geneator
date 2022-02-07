package com.example.passport.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.passport.R
import com.example.passport.adapters.PassportAdapter
import com.example.passport.database.AppDatabase
import com.example.passport.databinding.FragmentCitizensBinding
import com.example.passport.entity.Malumotlar
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CitizensFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CitizensFragment : Fragment() {
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
    lateinit var binding: FragmentCitizensBinding
    lateinit var appDatabase: AppDatabase
    lateinit var list: ArrayList<Malumotlar>
    private lateinit var tempArrayList: ArrayList<Malumotlar>
    lateinit var passportAdapter: PassportAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCitizensBinding.inflate(inflater,container,false)

        setHasOptionsMenu(true)
        appDatabase = AppDatabase.getInstance(binding.root.context)

        list = ArrayList()
        list.addAll(appDatabase.passportDao().getAllPassport())

        tempArrayList = ArrayList()
        tempArrayList.addAll(list)

        passportAdapter = PassportAdapter(tempArrayList,object : PassportAdapter.OnItemClickListener{
            override fun onItemClick(malumotlar: Malumotlar) {
                var bundle = Bundle()
                bundle.putSerializable("malum",malumotlar)
                findNavController().navigate(R.id.resultFragment,bundle)
            }

        })
        binding.pasportRv.adapter = passportAdapter

//        binding.tooolbaradd.inflateMenu(R.menu.menu_item)




        return binding.root
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item,menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                tempArrayList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()){
                    list.forEach {
                        if (it.name?.toLowerCase(Locale.getDefault())!!.contains(searchText)){

                            tempArrayList.add(it)
                        }
                    }
                    binding.pasportRv.adapter!!.notifyDataSetChanged()
                }else{
                    tempArrayList.clear()
                    tempArrayList.addAll(list)
                    binding.pasportRv.adapter!!.notifyDataSetChanged()
                }


                return false

            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CitizensFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CitizensFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}