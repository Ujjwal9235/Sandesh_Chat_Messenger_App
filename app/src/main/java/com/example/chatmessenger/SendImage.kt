package com.example.chatmessenger

import android.net.Uri
import android.net.sip.SipErrorCode.IN_PROGRESS
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.*


class SendImage : AppCompatActivity() {

    companion object {
        val Uri = "imageurl"

    }

    private val url = String

    // val imageView = ImageView(this)
    private val imageurl = Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_image)

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<com.example.chatmessenger.Model.User>(NewMessageActivity.USER_KEY)
        val toId = user?.uid

        val messageMember = User()

        //val storageReference = FirebaseStorage.getInstance().getReference("ChatImages")

        val imageView = findViewById<ImageView>(R.id.imageview_send_image)
        val button = findViewById<Button>(R.id.btn_send_image)
      //  val progressBar = findViewById<ProgressBar>(R.id.progressbar_send_image)
        val textView = findViewById<TextView>(R.id.textview_dont)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val url = bundle.getString("u")
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
        }

        Picasso.get().load(imageurl).into(imageView)
       // val imageurl = android.net.Uri.parse(url.toString())

       // val reference1 = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

      //  val toReference1 = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        button.setOnClickListener(View.OnClickListener {

            sendImage()
            textView.visibility = View.VISIBLE

        })

    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()

        // Return file Extension
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

   private fun sendImage() {

       val fromId = FirebaseAuth.getInstance().uid
       val user = intent.getParcelableExtra<com.example.chatmessenger.Model.User>(NewMessageActivity.USER_KEY)
       val toId = user?.uid

       val messageMember = User()
       val reference1 = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
       val toReference1 = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val progressBar = findViewById<ProgressBar>(R.id.progressbar_send_image)
        val imageurl = android.net.Uri.parse(url.toString())
       val storageReference = FirebaseStorage.getInstance().getReference("ChatImages")
        val uri = android.net.Uri.parse(url.toString())
        if (imageurl != null) {

            progressBar.visibility = View.VISIBLE
//            val fileUri = uri
//            val fileName = UUID.randomUUID().toString() + ".jpg"

           // val database = FirebaseDatabase.getInstance()

            val reference: StorageReference = storageReference.child(System.currentTimeMillis().toString() + "." + getFileExtension(imageurl))
            val uploadTask = reference.putFile(imageurl)

            val urlTask: Task<Uri?> = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                reference.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri: Uri? = task.result

                    messageMember to downloadUri


                     val id: String? = reference1.push().key
                     reference1.child(id!!).setValue(messageMember)

                     val id1: String? = toReference1.push().key
                     toReference1.child(id1!!).setValue(messageMember)

                    progressBar.visibility = View.VISIBLE
                }
            }

//
        }



else {
            Toast.makeText(this,"Please select somthing",Toast.LENGTH_SHORT).show()
        }

    }
}





