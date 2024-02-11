package com.example.a651

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.a651.Adapters.Vp2Adapter
import com.example.a651.DB.MyDB
import com.example.a651.databinding.FragmentHomBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class HomFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentHomBinding
    lateinit var myDB:MyDB
    lateinit var adapter:Vp2Adapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentHomBinding.inflate(inflater,container,false)

        binding.btnDeletTab.setOnClickListener {



            var alertDialog = AlertDialog.Builder(binding.root.context)
            alertDialog.setTitle("O'chirish")
            alertDialog.setMessage("Siz haqiqatdan ham (${myDB.getalltab()[binding.tabLayout.selectedTabPosition]}) bo'limin o'chirmoqchimisiz")
            alertDialog.setCancelable(false)

            alertDialog.setPositiveButton("O'chirish"
            ) { dialog, which ->

                if (getalllist()[binding.tabLayout.selectedTabPosition].list.size==0){
                    myDB.delettab(myDB.getalltab()[binding.tabLayout.selectedTabPosition])
                    Toast.makeText(binding.root.context, "Bo'lim o'chirildi", Toast.LENGTH_SHORT).show()
                   findNavController().navigate(R.id.homFragment)
                } else Toast.makeText(binding.root.context, "Ushbu bo'limni o'chirish uchun avval" +
                        " bo'limga tegishli bo'lgan ma'lumotlarni o'chirishingiz shart!", Toast.LENGTH_SHORT).show()
            }
            alertDialog.setNegativeButton("Rat etish"
            ) { dialog, which ->
                Toast.makeText(binding.root.context, "Rat etildi", Toast.LENGTH_SHORT).show()
            }


            alertDialog.show()


        }

        myDB = MyDB(binding.root.context)

        addtollbar()




        adapter = Vp2Adapter(getalllist(),requireActivity())

        binding.vp2View.adapter = adapter



        TabLayoutMediator(binding.tabLayout,binding.vp2View){tab, position ->
            if (myDB.getalltab().isNotEmpty()){
                tab.text = myDB.getalltab()[position].tab_name
            }

        }.attach()


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

    private fun getalllist(): ArrayList<UserList> {
        var list = ArrayList<UserList>()

        if (myDB.getalltab().size!=0){

            var count = 0

            do {

               list.add(UserList(myDB.getallypx2(count)))
                count++

            } while ( count<myDB.getalltab().size)
        }


        return list


    }

//    private fun addtabl() {
//        if (myDB.getalltab().isEmpty()){
//        myDB.addtab("Ogohlantiruvchi")
//        myDB.addtab("Imtiyozli")
//        myDB.addtab("Ta'qiqlovchi")
//        myDB.addtab("Buyuruvchi")
//        }
//    }


    private fun addtollbar() {
        var actvity = activity as MainActivity
        binding.toolbar.title = "Ma'lumotlar ro'yxati"
        actvity.setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.addmenu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId==R.id.add_menu){
            findNavController().navigate(R.id.action_homFragment_to_addFragment)
        }
        return super.onOptionsItemSelected(item)}


}