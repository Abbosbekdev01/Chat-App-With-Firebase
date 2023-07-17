package uz.abbosbek.chatapp1.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.abbosbek.chatapp1.R
import uz.abbosbek.chatapp1.adapters.UserAdapter
import uz.abbosbek.chatapp1.databinding.FragmentHomeBinding
import uz.abbosbek.chatapp1.models.User


class HomeFragment : Fragment() {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    lateinit var auth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var userReference: DatabaseReference
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        userReference = firebaseDatabase.getReference("users")
        userAdapter = UserAdapter()

        binding.rv.adapter = userAdapter
        userReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                for (child in children) {
                    val user = child.getValue(User::class.java)
                    if (user != null){
                        userAdapter.list.add(user)
                    }
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return binding.root
    }

}