package com.example.a651

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.a651.Adapters.RvAdapter
import com.example.a651.DB.MyDB
import com.example.a651.DB.MyDBservis
import com.example.a651.databinding.FragmentSevBinding
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SevFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SevFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentSevBinding
    lateinit var myDB: MyDB
    lateinit var adapter: RvAdapter
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

        binding = FragmentSevBinding.inflate(inflater,container,false)


        myDB = MyDB(binding.root.context)

        var list =listadd()

        adapter = RvAdapter(object : RvAdapter.OnClik{

            override fun edit(contact: Usercontact, position:Int, view: View) {
                super.edit(contact, position,view)
                var bundle = Bundle()
                bundle.putSerializable("key", contact)
                findNavController().navigate(R.id.addFragment,bundle)
            }

            override fun delet(contact: Usercontact, position:Int) {
                super.delet(contact,position)
                myDB.deletypx(contact)
                list?.remove(contact)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position,list!!.size)
            }

            override fun save(contact: Usercontact, position:Int) {
                super.save(contact,position)
                if (contact.save == 1){
                    myDB.editypxsave(contact,0)
                    list?.get(position)?.save = 0
                    adapter.notifyItemChanged(position)

                }else{
                    myDB.editypxsave(contact,1)
                    list?.get(position)?.save = 1
                    adapter.notifyItemChanged(position)


                }

            }
        })
        binding.rvView.adapter = adapter

        adapter.submitList(list)


        binding.btnHom.setOnClickListener {
            findNavController().navigate(R.id.homFragment)
        }
        binding.btnSev.setOnClickListener {
            findNavController().navigate(R.id.sevFragment)
        }
        binding.btnInfo.setOnClickListener {
            findNavController().navigate(R.id.infoFragment)
        }


        return binding.root
    }

    private fun listadd(): ArrayList<Usercontact> {
        var list = ArrayList<Usercontact>()

        for (i in myDB.getallsaveypx()){
            if (i.save==1){
                list.add(i!!)
            }
        }

        return list


    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SevFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SevFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}