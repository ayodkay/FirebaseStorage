package com.apps.ayodkay.services;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    TextView userName;
    ImageButton useredit;
    TextView userabout;
    TextView useremail;
    Dialog myDialog;
    ProgressBar mprogress;
    CircleImageView userphoto;

    private Uri mImageUri;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrent_user;
    private StorageReference mStorageRef;
    private DatabaseReference mUsernameDatabase;
    private static final int CHOOSE_IMAGE = 100;
    private StorageTask<UploadTask.TaskSnapshot> mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");




        myDialog = new Dialog(this);
        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

        String user_id = current_user.getUid();

        DatabaseReference mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);


        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();


        userName = findViewById(R.id.Username);
        userphoto = findViewById(R.id.userPhoto);
        userabout = findViewById(R.id.userAbout);
        useremail = findViewById(R.id.userEmail);
        mprogress = findViewById(R.id.profileProgress);


        userphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogress.setVisibility(View.VISIBLE);
                showImageChooser();

            }
        });


        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String about = Objects.requireNonNull(dataSnapshot.child("about").getValue()).toString();
                userabout.setText(about);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        loadUserInformation();
    }

    public void ShowDialog(View view){

        TextView txtclose;
        final TextView editName;
        final EditText editState;
        final EditText editCity;
        final TextView editemail;
        final EditText editAddress;
        final EditText editAbout;
        MaterialCardView materialCardView1;
        MaterialCardView materialCardView2;
        MaterialCardView materialCardView3;
        MaterialCardView materialCardView4;
        MaterialCardView materialCardView5;
        Button EditProfile;
        TextView changepassword;



        myDialog.setContentView(R.layout.editprofile);
        editName = myDialog.findViewById(R.id.editname);
        txtclose = myDialog.findViewById(R.id.txtclose);
        editemail = myDialog.findViewById(R.id.editemail);
        editAbout = myDialog.findViewById(R.id.editabout);
        editAddress = myDialog.findViewById(R.id.editaddress);
        editState = myDialog.findViewById(R.id.editstate);
        editCity = myDialog.findViewById(R.id.editcity);
        changepassword = myDialog.findViewById(R.id.passwordIntent);
        EditProfile = myDialog.findViewById(R.id.EditProfile);


        materialCardView1 = myDialog.findViewById(R.id.materialcpf);
        materialCardView2 = myDialog.findViewById(R.id.materialcompany);
        materialCardView3 = myDialog.findViewById(R.id.materialwebsite);
        materialCardView4 = myDialog.findViewById(R.id.materialprofession);
        materialCardView5 = myDialog.findViewById(R.id.materialphone);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = Objects.requireNonNull(mCurrentUser).getUid();


        FirebaseUser user = mAuth.getCurrentUser();
        editemail.setText(user.getEmail());
        editName.setText(user.getDisplayName());




        mUsernameDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);



        boolean phoneempty = mAuth.getCurrentUser().getDisplayName().contains("1.");
        if (phoneempty) {
            Toast.makeText(Profile.this, "Error 404,Please Login Again", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent logout = new Intent(Profile.this, Login.class);
            startActivity(logout);
            finish();


        }else{

            materialCardView1.setVisibility(View.GONE);
            materialCardView2.setVisibility(View.GONE);
            materialCardView3.setVisibility(View.GONE);
            materialCardView4.setVisibility(View.GONE);
            materialCardView5.setVisibility(View.GONE);

            mUsernameDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    String about = Objects.requireNonNull(dataSnapshot.child("about").getValue()).toString();
                    editAbout.setText(about);

                    String address = Objects.requireNonNull(dataSnapshot.child("address").getValue()).toString();
                    editAddress.setText(address);

                    String state = Objects.requireNonNull(dataSnapshot.child("state").getValue()).toString();
                    editState.setText(state);


                    String city = Objects.requireNonNull(dataSnapshot.child("city").getValue()).toString();
                    editCity.setText(city);



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });



        }



        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean phoneempty = mAuth.getCurrentUser().getDisplayName().contains("1.");
                if (phoneempty) {

                    Toast.makeText(Profile.this, "Error 404,Please Login Again", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent logout = new Intent(Profile.this, Login.class);
                    startActivity(logout);
                    finish();
                    /*final String about = editAbout.getText().toString();
                    final String state = editState.getText().toString();
                    final String city = editCity.getText().toString();
                    final String address = editAddress.getText().toString();
                    final String cpf = editCPF.getText().toString();
                    final String company = editcompany.getText().toString();
                    final String website = editWebsite.getText().toString();

                    if (about.isEmpty()){
                        editAbout.setError("About is empty");
                        editAbout.requestFocus();
                        return;
                    }

                    if (state.isEmpty()){
                        editState.setError("State is empty");
                        editState.requestFocus();
                        return;
                    }

                    if (city.isEmpty()){
                        editCity.setError("City is empty");
                        editCity.requestFocus();
                        return;
                    }

                    if (address.isEmpty()){
                        editAddress.setError("Address is empty");
                        editAddress.requestFocus();
                        return;
                    }

                    if (cpf.isEmpty()){
                        editCPF.setError("CPF is empty");
                        editCPF.requestFocus();
                        return;
                    }

                    if (company.isEmpty()){
                        editcompany.setError("company is empty");
                        editcompany.requestFocus();
                        return;
                    }

                    if (website.isEmpty()){
                        editWebsite.setError("website is empty");
                        editWebsite.requestFocus();
                        return;
                    }

                    mUsernameDatabase.child("about").setValue(about).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                            }
                        }
                    });
                    mUsernameDatabase.child("state").setValue(state).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                            }
                        }
                    });
                    mUsernameDatabase.child("city").setValue(city).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                            }
                        }
                    });
                    mUsernameDatabase.child("address").setValue(address).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Profile.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    mUsernameDatabase.child("cpf").setValue(cpf).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                            }
                        }
                    });
                    mUsernameDatabase.child("company").setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                            }
                        }
                    });
                    mUsernameDatabase.child("website").setValue(website).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Profile.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/
                } else {
                    final String about = editAbout.getText().toString();
                    final String state = editState.getText().toString();
                    final String city = editCity.getText().toString();
                    final String address = editAddress.getText().toString();



                    if (about.isEmpty()){
                        editAbout.setError("About is empty");
                        editAbout.requestFocus();
                        return;
                    }

                    if (state.isEmpty()){
                        editState.setError("State is empty");
                        editState.requestFocus();
                        return;
                    }

                    if (city.isEmpty()){
                        editCity.setError("City is empty");
                        editCity.requestFocus();
                        return;
                    }

                    if (address.isEmpty()){
                        editAddress.setError("Address is empty");
                        editAddress.requestFocus();
                        return;
                    }


                    mUsernameDatabase.child("about").setValue(about).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Profile.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    mUsernameDatabase.child("state").setValue(state).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                            }
                        }
                    });
                    mUsernameDatabase.child("city").setValue(city).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                            }
                        }
                    });
                    mUsernameDatabase.child("address").setValue(address).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Profile.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                myDialog.dismiss();
            }
        });

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newserviceprovider = new Intent(Profile.this, Forgotten.class);
                startActivity(newserviceprovider);
                myDialog.dismiss();
            }
        });


        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        (myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }
    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getEmail()!= null){
                useremail.setText(user.getEmail());
            }

            if (user.getDisplayName()!= null){
                userName.setText(user.getDisplayName());

            }

            if (user.getPhotoUrl()!= null){
                Picasso.with(this).load(user.getPhotoUrl()).into(userphoto);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            if (mUploadTask != null && mUploadTask.isInProgress()){

                Toast.makeText(Profile.this, "Upload is in progress", Toast.LENGTH_SHORT).show();
            }else {
                uploadFile();
            }


            Picasso.with(this).load(mImageUri).into(userphoto);

        }

    }

    private void uploadFile() {

        if (mImageUri != null) {

            Date date = new Date();

            long time = date.getTime();
            

            Uri file = Uri.fromFile(new File("path/to/images/" + time));

            final StorageReference fileReference = mStorageRef.child("images/" + file.getLastPathSegment());
            mUploadTask = fileReference.putFile(mImageUri);

            // Register observers to listen for when the download is done or if it fails
            mUploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });


            Task<Uri> urlTask = mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        FirebaseUser user = mAuth.getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(downloadUri)
                                .build();
                        user.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Profile.this, "upload Done", Toast.LENGTH_SHORT).show();
                                mprogress.setVisibility(View.GONE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                    } else {
                        Toast.makeText(Profile.this, "Error uploaing", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            // [END upload_get_download_url]
        }

    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

}
