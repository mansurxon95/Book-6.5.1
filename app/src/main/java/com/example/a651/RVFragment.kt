package com.example.a651

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.a651.Adapters.RvAdapter
import com.example.a651.DB.MyDB
import com.example.a651.databinding.FragmentRVBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RVFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RVFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: ArrayList<Usercontact>? = null
    private var param2: String? = null
    lateinit var binding: FragmentRVBinding
    lateinit var myDB:MyDB
    lateinit var adapter: RvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as ArrayList<Usercontact>?
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRVBinding.inflate(inflater,container,false)
        var contextBinding = binding.root.context
        myDB = MyDB(contextBinding)

        var list = param1

        adapter = RvAdapter(object : RvAdapter.OnClik{

            override fun view(contact: Usercontact, position: Int) {
                super.view(contact, position)

                var bundle = Bundle()
                bundle.putSerializable("key",contact)

                findNavController().navigate(R.id.viewFragment,bundle)

            }

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

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RVFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: ArrayList<Usercontact>) =
            RVFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}