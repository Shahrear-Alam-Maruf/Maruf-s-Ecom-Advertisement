package com.example.videoupload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private Button uploadBtn, browseButton;
    //private ImageView imageView;
    private  VideoView videoView;
    MediaController mediaController;
    private ProgressBar progressBar;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Video");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri videoUri;

    private EditText vtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vtitle = findViewById(R.id.vtitle);

        uploadBtn = findViewById(R.id.upload);
        browseButton = findViewById(R.id.browse);
        progressBar = findViewById(R.id.progressBar);
        videoView = findViewById(R.id.videoView);
        mediaController=new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();

        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,101);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (videoUri != null){
                    uploadToFirebase(videoUri);
                }else{
                    Toast.makeText(MainActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadToFirebase(Uri uri){
        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Member model = new Member(uri.toString(), vtitle.getText().toString());
                        String modelId = root.push().getKey();
                        root.child(modelId).setValue(model);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                       // imageView.setImageResource(R.mipmap.ic_launcher);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==101 && resultCode == RESULT_OK && data != null){

            videoUri = data.getData();
            videoView.setVideoURI(videoUri);

        }
    }
}
      /* vtitle=(EditText)findViewById(R.id.vtitle);

        member = new Member();
       storageReference= FirebaseStorage.getInstance().getReference("videos");
       databaseReference= FirebaseDatabase.getInstance().getReference("myvideos");


        videoView=(VideoView)findViewById(R.id.videoView);
         upload=(Button)findViewById(R.id.upload);
        browse=(Button)findViewById(R.id.browse);
        mediaController=new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo(v);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadVideo();
            }
        });

    }

    private void UploadVideo() {
        String name =  vtitle.getText().toString();
        String search = vtitle.getText().toString();
        if(videouri != null || !TextUtils.isEmpty(name)){
            progressBar.setVisibility(View.VISIBLE);

            final StorageReference reference = storageReference.child(System.currentTimeMillis() +"." + getExtension(videouri));

            uploadTask = reference.putFile(videouri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return  reference.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
                        member.setName(name);
                        member.setVideoUrl(videouri.toString());
                        member.setSearch(search);
                        String i = databaseReference.push().getKey();
                        databaseReference.child(i).setValue(member);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else {
            Toast.makeText(this, "all fields required", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == PICK_VIDEO  || resultCode == RESULT_OK || data!= null || data.getData()!=null){
            videouri=data.getData();
            videoView.setVideoURI(videouri);

        }
    }

    public  void  chooseVideo(View view){
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_VIDEO);
    }

    public String getExtension(Uri uri)
    {
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(videouri));
    }
}*/