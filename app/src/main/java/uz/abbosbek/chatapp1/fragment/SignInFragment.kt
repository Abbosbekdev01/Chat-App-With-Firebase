package uz.abbosbek.chatapp1.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uz.abbosbek.chatapp1.R
import uz.abbosbek.chatapp1.databinding.FragmentSignInBinding
import uz.abbosbek.chatapp1.models.User

private const val TAG = "SignInFragment"

class SignInFragment : Fragment() {
    private val binding by lazy { FragmentSignInBinding.inflate(layoutInflater) }
    lateinit var googleSignInClient: GoogleSignInClient
    var RC_SIGN_IN = 1
    lateinit var auth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var userReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            findNavController().popBackStack()
            findNavController().navigate(R.id.homeFragment)
            return binding.root
        }


        firebaseDatabase = FirebaseDatabase.getInstance()
        userReference = firebaseDatabase.getReference("users")

        binding.btnSignIn.setOnClickListener {
            signIn()
        }

        return binding.root
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "onActivityResult: ${account.id}")
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                Log.d(TAG, "onActivityResult: $e")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "firebaseAuthWithGoogle: Success")
//                    val user = auth.currentUser
                    val user = User(auth.currentUser?.uid, auth.currentUser?.displayName, auth.currentUser?.photoUrl.toString())
                    userReference.child(user.id!!).setValue(user)
                    findNavController().navigate(R.id.homeFragment)
                    Toast.makeText(requireContext(), "${user.name}", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d(TAG, "firebaseAuthWithGoogle", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}