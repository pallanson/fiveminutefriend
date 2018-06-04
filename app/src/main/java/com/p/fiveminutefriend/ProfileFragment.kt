package com.p.fiveminutefriend

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT_TREE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import java.io.File

class ProfileFragment : Fragment() {

    private val REQUEST_IMAGE_CAPTURE = 101
    private val IMAGE_FROM_GALLERY = 202
    private val IMAGE_PERMISSIONS_REQUEST_CODE = 1
    private val DOCUMENTS_REQUEST_CODE = 303
    private val TAG = "Image Permissions"
    private lateinit var photoFilePath: String
    private lateinit var preferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getImageFromFirebase()
        val uid = FirebaseAuth.getInstance().uid
        val userReference = FirebaseDatabase.getInstance().reference.child("Users").child(uid)

        imageView_picture_profile.setOnClickListener({
            val pictureDialog = AlertDialog.Builder(activity)
            pictureDialog.setTitle("Upload Image")
                    .setMessage("Upload from Camera or from Gallery?")
                    .setPositiveButton("From Camera") { _, _ ->
                        if (isImagePermissionGranted()) {
                            imageFromCamera()
                        }
                    }
                    .setNegativeButton("From Gallery") { _, _ ->
                        if (isImagePermissionGranted()) {
                            imageFromGallery()
                        }
                    }
                    .setNeutralButton("Cancel") { _, _ ->
                        Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                    .show()
        })

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var genderString = ""

                when (dataSnapshot.child("gender").value.toString().toInt()) {
                    0 -> genderString = "Male"
                    1 -> genderString = "Female"
                    2 -> genderString = "Other"
                }

                val firstName = dataSnapshot.child("firstName").value.toString()
                val lastName = dataSnapshot.child("lastName").value.toString()
                val username = dataSnapshot.child("username").value.toString()
                val email = dataSnapshot.child("email").value.toString()
                val age = dataSnapshot.child("age").value.toString()
                val language = dataSnapshot.child("language").value.toString()


                text_name_profile.text = firstName + " " + lastName
                text_username_profile.text = username
                text_email_profile.text = email
                //TODO: Replace hardcoded string with date when implemented
                text_birthday_profile.text = "February 3, 1992 (" + age + " years old)"
                text_gender_profile.text = genderString
                text_language_profile.text = language
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, "Error Retrieving Profile", Toast.LENGTH_SHORT).show()
            }
        })

        button_edit_profile.setOnClickListener({
            val editProfileFragment = EditProfileFragment()
            val manager = fragmentManager
            val transaction = manager.beginTransaction()
            transaction
                    .replace(R.id.layout_profile_view, editProfileFragment)
                    .addToBackStack("Edit")
                    .commit()
        })

        toolbar_profile.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        toolbar_profile.setNavigationOnClickListener {
            //fab_new_chat_main_activity.show()
            fragmentManager.popBackStackImmediate()
        }
    }

    private fun imageFromGallery() {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor: SharedPreferences.Editor = preferences.edit()

        if(preferences.contains("documentsSaved")) {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"

            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            startActivityForResult(chooserIntent, IMAGE_FROM_GALLERY)
        }
        else {
            val intent = Intent(ACTION_OPEN_DOCUMENT_TREE)
            startActivityForResult(intent, DOCUMENTS_REQUEST_CODE)
        }

    }

    private fun imageFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File = createProfilePhotoFile()
        val photoUri = FileProvider.getUriForFile(activity, "com.p.fiveminutefriend.provider", photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            val image = BitmapFactory.decodeFile(photoFilePath)
            imageView_picture_profile.setImageBitmap(image)
            uploadImageToFirebase(image)
        }

        if (requestCode == IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            val inputStream = activity.contentResolver.openInputStream(data!!.data)
            val imageBitmap = BitmapFactory.decodeStream(inputStream)
            bitmapToImageView(imageBitmap as Bitmap)
        }
        if(requestCode == DOCUMENTS_REQUEST_CODE && resultCode == RESULT_OK) {
//            val documentTreeUri = data!!.data
//            getDocumentsUri(activity, documentTreeUri)
        }
    }

    private fun bitmapToImageView(bitmap: Bitmap) {
        uploadImageToFirebase(bitmap)
//        val scalingTool = BitmapScalingTool(imageView_picture_profile.height, true)
//        val scaledBitmap = scalingTool.transform(bitmap)
//        imageView_picture_profile.setImageBitmap(scaledBitmap)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val path = MediaStore.Images.Media.insertImage(activity.contentResolver,
                bitmap,
                "profilePic",
                null)
        val imageUri = Uri.parse(path)
        Picasso.get().load(imageUri).into(imageView_picture_profile)
    }

    private fun isImagePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED &&
                    activity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&
                    activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        IMAGE_PERMISSIONS_REQUEST_CODE)
                false
            }
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            IMAGE_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission Denied by User")
                } else {
                    Log.v(TAG, "Permission Granted by User")
                }
            }
        }
    }

    private fun getImageFromFirebase() {
        val storageReference = FirebaseStorage
                .getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_STORAGE_REFERENCE +
                        FirebaseAuth.getInstance().currentUser!!.uid)

        storageReference.child("profilePic")
                .downloadUrl
                .addOnSuccessListener {
                    Picasso.get()
                            .load(it.toString())
                            .into(imageView_picture_profile)
                }
                .addOnFailureListener({
                    Toast.makeText(activity, "Url for image not found", Toast.LENGTH_SHORT)
                            .show()
                })
    }

    private fun uploadImageToFirebase(bitmap: Bitmap) {
        val storageReference = FirebaseStorage.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_STORAGE_REFERENCE +
                        FirebaseAuth.getInstance().currentUser!!.uid)
        val fileReference = storageReference.child("profilePic")
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val data: ByteArray = byteArrayOutputStream.toByteArray()

        val uploadTask = fileReference.putBytes(data)
        uploadTask.addOnFailureListener({
            Toast.makeText(activity, "Upload to database failed.", Toast.LENGTH_SHORT).show()
        })

        uploadTask.addOnSuccessListener {
            Toast.makeText(activity, "Upload to database successful", Toast.LENGTH_SHORT)
                    .show()
        }
        getImageFromFirebase()

    }

    private fun createProfilePhotoFile(): File {
        val fileName = "profilePic"
        val storageDirectory = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(fileName, ".jpg", storageDirectory)
        photoFilePath = image.absolutePath
        return image
    }

//  Tried to scrape documents, but couldn't work out how to do it without user input
//
//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    private fun getDocumentsUri(context: Context, self: Uri) {
//        val resolver = context.contentResolver
//        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(self,
//                    DocumentsContract.getDocumentId(self))
//        val results = ArrayList<Uri>()
//
//        val cursor = resolver.query(childrenUri,
//                arrayOf(DocumentsContract.Document.COLUMN_DOCUMENT_ID),
//                null,
//                null,
//                null)
//
//        while(cursor.moveToNext()) {
//            val documentId = cursor.getString(0)
//            val documentUri = DocumentsContract.buildDocumentUriUsingTree(self, documentId)
//            results.add(documentUri)
//        }
//
//        cursor.close()
//        uploadDocuments(activity, results)
//    }
//
//    private fun uploadDocuments(context: Context, uriList: ArrayList<Uri>) {
//
//        val storageReference = FirebaseStorage.getInstance()
//                .getReferenceFromUrl(Constants.FIREBASE_STORAGE_REFERENCE)
//
//        for (i in uriList) {
//            val file = File(i.path)
//            val fileReference = storageReference.child(file.name)
//            val byteArrayOutputStream = ByteArrayOutputStream()
//            val data: ByteArray = byteArrayOutputStream.toByteArray()
//            val uploadTask = fileReference.putBytes(data)
//            uploadTask.addOnSuccessListener {
//                Toast.makeText(context, "Stolen Documents Lel", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        editor.putBoolean("documentsSaved", true).apply()
//    }
}


