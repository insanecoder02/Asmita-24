package com.example.xenon.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Adapter.NotificationAdapter
import com.example.xenon.DataClass.Message
import com.example.xenon.databinding.FragmentNotificationBinding
import com.google.firebase.firestore.FirebaseFirestore

class Notification : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var msgadapter: NotificationAdapter
    private var msgList: MutableList<Message> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val fcmMessages = requireContext().getFCMMessages().toList()

        msgadapter = NotificationAdapter(msgList)
        binding.msgRv.adapter = msgadapter
        binding.msgRv.layoutManager = LinearLayoutManager(requireContext())

        fetchFromFireStore()

//        for (message in fcmMessages) {
//            val parts = message.split(":")
//            if (parts.size == 2) {
//                val title = parts[0].trim()
//                val body = parts[1].trim()
//                msgList.add(Message(title, body))
//            }
//            msgadapter.notifyDataSetChanged()
//        }

    }

    private fun fetchFromFireStore() {
        msgList.clear()
        val firestore = FirebaseFirestore.getInstance()
        val notificationsCollection = firestore.collection("Notification")

        notificationsCollection.orderBy("time").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val title = document.getString("title") ?: ""
                    val body = document.getString("body") ?: ""
                    msgList.add(Message(title, body))
                }
                msgadapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
}

//fun Context.getFCMMessages(): Set<String> {
//    val sharedPreferences = getSharedPreferences("FCM_MESSAGES", Context.MODE_PRIVATE)
//    return sharedPreferences.getStringSet("MESSAGES", HashSet()) ?: HashSet()
//}
