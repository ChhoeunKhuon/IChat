package com.num.ichat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.num.ichat.R
//import com.num.ichat.adapter.ChatAdapter
import com.num.ichat.databinding.ActivityNumberBinding
import com.num.ichat.databinding.FragmentChatBinding
import com.num.ichat.model.UserModel

class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    private var database: FirebaseDatabase?=null
    lateinit var userList: ArrayList<UserModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater)

        database = FirebaseDatabase.getInstance("https://ichatapplication-e03ae-default-rtdb.asia-southeast1.firebasedatabase.app/")
        userList = ArrayList()


        database!!.reference.child("users")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()

                    for(snapshot1 in snapshot.children){
                        val user = snapshot1.getValue(UserModel::class.java)
                        if(user!!.uid != FirebaseAuth.getInstance().uid){
                            userList.add(user)
                        }
                    }
//                    binding.userListRecyclerView.adapter = ChatAdapter(requireContext(), userList)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        return binding.root
    }

}