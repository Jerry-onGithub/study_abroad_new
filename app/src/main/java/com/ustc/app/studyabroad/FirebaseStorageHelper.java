package com.ustc.app.studyabroad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ustc.app.studyabroad.interfaces.Callback;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.interfaces.GetCommentsCallback;
import com.ustc.app.studyabroad.interfaces.GetDecisionsCallback;
import com.ustc.app.studyabroad.interfaces.GetRecentlyViewedCallback;
import com.ustc.app.studyabroad.interfaces.GetUrlCallback;
import com.ustc.app.studyabroad.interfaces.GetPostsCallback;
import com.ustc.app.studyabroad.interfaces.GetProfileCallback;
import com.ustc.app.studyabroad.interfaces.PostReturnCallback;
import com.ustc.app.studyabroad.interfaces.ReturnCallback;
import com.ustc.app.studyabroad.models.RecentlyViewed;
import com.ustc.app.studyabroad.userModels.Comment;
import com.ustc.app.studyabroad.userModels.Decisions;
import com.ustc.app.studyabroad.userModels.Post;
import com.ustc.app.studyabroad.userModels.Profile;
import com.ustc.app.studyabroad.userModels.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class FirebaseStorageHelper {

    public static String getUserId(){
        return(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }
    public static void uploadFile(Uri filePath, GetUrlCallback getUrlCallback) {
        //if there is a file to upload
        if (filePath != null) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageRef = firebaseStorage.getReference().child("users").child(user.getUid());
            StorageReference riversRef = storageRef.child("resume_file/" + "resume");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>> filepath SUCCESS "+filePath);
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                                    new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            String fileLink = task.getResult().toString();
                                            //next work with URL
                                            //System.out.println("IMAGE URL >>>>>>>>>>>>>>>>>>>>>>>>>> "+fileLink);
                                            getUrlCallback.onSuccess(fileLink);
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>> filepath FAILURE "+filePath);

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        }
                    });
        }
        //if there is not any file
        else {
            //display an error toast
        }
    }

    // UploadImage method
    public static void uploadImage(String id, String v, Uri filePath, GetUrlCallback getImageUrlCallback)
    {
        StorageReference storageRef = null;
        if (filePath != null) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            if(v=="profile"){
                storageRef = firebaseStorage.getReference().child("users").child(user.getUid()).child("profile_image/" + "profile picture");
            }
            if(v=="post"){
                storageRef = firebaseStorage.getReference().child("posts").child(id);
            }
            storageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                            new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String fileLink = task.getResult().toString();
                                    getImageUrlCallback.onSuccess(fileLink);
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        //progressDialog.setMessage("Uploaded " + (int)progress + "%");
                    }
                });
        }
    }

    public static void saveProfile(Profile profile){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("profile");
        database.setValue(profile);
    }

    public static void getProfile(String userId, GetProfileCallback getProfileCallback){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("profile")){
                    DatabaseReference mDatabase = database.child("profile");
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Profile profile = dataSnapshot.getValue(Profile.class);
                            getProfileCallback.onSuccess(profile);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            getProfileCallback.onFailure("f");
                        }
                    });
                }
                else{
                    getProfileCallback.onFailure("nf");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void setImage(Context ctx, ImageView img, String userId, Callback callback) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://study-abroad-65269.appspot.com/")/*.child("users").child(userId).child("profile_image/").child("profile picture")*/;

        storageReference.child("users").child(userId).child("profile_image/").child("profile picture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ctx).load(uri.toString()).into(img);
                callback.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure();
            }
        });
    }

    public static void getResume(String userId, GetUrlCallback callback) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://study-abroad-65269.appspot.com/");
        storageReference.child("users").child(userId).child("resume_file/").child("resume").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> URI "+ uri);
                callback.onSuccess(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure("");
            }
        });
    }

    public static void saveDecision(Decisions decisions){
        decisions.setDecisionId(UUID.randomUUID().toString());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("decisions").child(decisions.getDecisionId());
        database.setValue(decisions);
        saveDecisionGlobal(decisions);
    }
    public static void saveDecisionGlobal(Decisions decisions){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("global").child("decisions")/*.child(decisions.getStatus())*/.child(decisions.getDecisionId());
        database.setValue(decisions);
    }
    public static void saveDecisionsForUser(String i, Decisions decision, Callback callback){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());//.child("saved_decisions").child(decision.getDecisionId());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("saved_decisions")) {
                    DatabaseReference mDatabase = dataSnapshot.getRef().child("saved_decisions");
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Boolean found=false;
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Decisions dec = ds.getValue(Decisions.class);
                                if(dec.getDecisionId().equals(decision.getDecisionId())){
                                    if (!(i.equals("exists"))) {
                                        ds.getRef().removeValue();
                                    }
                                    found=true;
                                }
                            }
                            if (found==true){
                                callback.onFailure();
                            }
                            if(found==false){
                                if (i != "exists") {
                                    mDatabase.child(decision.getDecisionId()).setValue(decision);
                                }
                                callback.onSuccess();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else{
                    if (!(i.equals("exists"))) {
                        database.child("saved_decisions").child(decision.getDecisionId()).setValue(decision);
                    }
                    callback.onSuccess();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getDecisions(String s, String i, GetDecisionsCallback getDecisionsCallback){
        List<Decisions> decisions = new ArrayList<Decisions>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference db = null;
        if(i=="ar"){
            Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ar");
            db = FirebaseDatabase.getInstance().getReference().child("global");
        } else if(i=="s"){
            Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> s");
            db = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        } else {
            Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> else");
            db = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        }

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(s)){
                    Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> have " + i + " " + s);
                    DatabaseReference mDatabase = snapshot.getRef().child(s);
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                Decisions dsn = ds.getValue(Decisions.class);
                                decisions.add(dsn);
                            }
                            getDecisionsCallback.onSuccess(decisions);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            getDecisionsCallback.onFailure("f");
                        }
                    });
                }
                else{
                    Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> not " + i + " " + s);
                    getDecisionsCallback.onFailure("nf");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                getDecisionsCallback.onFailure("f");
            }
        });
    }

    public static void deleteDecision(String decision_id, String status, Callback callback){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("decisions").child(decision_id);
        try {
            database.removeValue();
            deleteDecisionGlobal(decision_id, status);
            callback.onSuccess();
        } catch (Exception e){
            callback.onFailure();
        }
    }
    private static void deleteDecisionGlobal(String decision_id, String status) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("global").child("decisions")/*.child(status)*/.child(decision_id);
        database.removeValue();
    }

    public static void downloadFile(String filename) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://study-abroad-65269.appspot.com/").child("users").child(user.getUid());
        StorageReference islandRef = storageRef.child("resume_file/" + "resume");

        System.out.println("URI >>>>>>>>>>>>>> " + islandRef.getDownloadUrl());
        File rootPath = new File(Environment.getExternalStorageDirectory(), "Download");
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }
        final File localFile = new File(rootPath, filename);

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ", ";local tem file created  created " + localFile.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ", ";local tem file not created  created " + exception.toString());
            }
        });
    }

    public static void savePost(Post post, Uri imagePath){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        post.setPostId(UUID.randomUUID().toString());
        post.setUserId(user.getUid());
        post.setUsername(user.getDisplayName());
        FirebaseStorageHelper.uploadImage(post.getPostId(), "post", imagePath, new GetUrlCallback() {
            @Override
            public void onSuccess(String imgUrl) {
                post.setPhoto(imgUrl);
            }
            @Override
            public void onFailure(String msg) {

            }
        });
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("global").child("posts").child(post.getPostId());
        database.setValue(post);
    }

    public static void getPosts(GetPostsCallback getPostsCallback){
        List<Post> posts = new ArrayList<Post>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("global");//.child("profile");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("posts")){
                    DatabaseReference mDatabase = database.child("posts");
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                Post post = ds.getValue(Post.class);
                                posts.add(post);
                            }
                            getPostsCallback.onSuccess(posts);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            getPostsCallback.onFailure("f");
                        }
                    });
                } else{
                    getPostsCallback.onFailure("nf");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                getPostsCallback.onFailure("nf");
            }
        });
    }

    public static void saveComment(Comment comment){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        comment.setUserId(user.getUid());
        comment.setName(user.getDisplayName());
        comment.setCommentId(UUID.randomUUID().toString());
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("global").child("comments").child(comment.getPostId()).child(comment.getCommentId());
        database.setValue(comment);

        DatabaseReference gpd = FirebaseDatabase.getInstance().getReference().child("global").child("posts").child(comment.getPostId()).child("comments");
        gpd.setValue(ServerValue.increment(1));
    }

    public static void getComments(String postId, GetCommentsCallback getCommentsCallback){
        List<Comment> comments = new ArrayList<Comment>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("global").child("comments");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(postId)){
                    DatabaseReference mDatabase = database.child(postId);
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                Comment comment = ds.getValue(Comment.class);
                                comments.add(comment);
                            }
                            getCommentsCallback.onSuccess(comments);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            getCommentsCallback.onFailure("f");
                        }
                    });
                } else{
                    getCommentsCallback.onFailure("nf");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                getCommentsCallback.onFailure("nf");
            }
        });
    }

    public static void check_(String postId, String wc, String l_or_d, String p, String commentId, PostReturnCallback callback) {
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference post = null;
        if(wc.equals("p")){
            post = FirebaseDatabase.getInstance().getReference().child("global").child("posts").child(postId);
        } else if(wc.equals("c")&&l_or_d.equals("likers_ids")){
            post = FirebaseDatabase.getInstance().getReference().child("global").child("comments").child(postId).child(commentId); //like comment
        } else if(l_or_d.equals("dislikers_ids")){
            post = FirebaseDatabase.getInstance().getReference().child("global").child("comments").child(postId).child(commentId);
        }

        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(l_or_d)) {    //likers_id
                    snapshot.child(l_or_d).getRef().addListenerForSingleValueEvent(new ValueEventListener() {   //likers_id
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Boolean f = false;
                            int l_or_ds = (int) dataSnapshot.getChildrenCount();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {        //likers_id
                                if (ds.getValue().toString().equals(user)) {
                                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> TRUE");
                                    f = true;   //already liked
                                    if(wc.equals("p")){
                                        ds.getRef().removeValue();
                                        snapshot.child(p).getRef().setValue(l_or_ds-1);
                                        callback.onFailure(String.valueOf(l_or_ds-1), "");
                                    }
                                    ///
                                    if(wc.equals("c")&&l_or_d.equals("likers_ids")){
                                        ds.getRef().removeValue();
                                        snapshot.child(p).getRef().setValue(l_or_ds-1);
                                        callback.onFailure(String.valueOf(l_or_ds-1), "");
                                    } else if(wc.equals("c")&&l_or_d.equals("dislikers_ids")){
                                        ds.getRef().removeValue();
                                        snapshot.child(p).getRef().setValue(l_or_ds-1);
                                        callback.onFailure("", String.valueOf(l_or_ds-1));
                                    }
                                    ///
                                }
                            }

                            if (f!=true) {
                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> FALSE");
                                if(wc.equals("p")){
                                    dataSnapshot.getRef().push().setValue(user);
                                    snapshot.child(p).getRef().setValue(l_or_ds+1);
                                    callback.onFailure(String.valueOf(l_or_ds+1), "");
                                } else {
                                    ///
                                    if(l_or_d.equals("likers_ids")){ //already disliked
                                        if (snapshot.hasChild("dislikers_ids")) {
                                            snapshot.child("dislikers_ids").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dlfDataSnapshot) {
                                                    Boolean dlf = false;
                                                    int dislikes = (int) dlfDataSnapshot.getChildrenCount();
                                                    for (DataSnapshot ds : dlfDataSnapshot.getChildren()) {
                                                        if (ds.getValue().toString().equals(user)) {
                                                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> TRUE");
                                                            dlf = true;
                                                            ds.getRef().removeValue();
                                                            snapshot.child("dislikes").getRef().setValue(dislikes - 1);

                                                            dataSnapshot.getRef().push().setValue(user);
                                                            snapshot.child(p).getRef().setValue(l_or_ds+1);

                                                            callback.onSuccess(String.valueOf(l_or_ds+1), String.valueOf(dislikes - 1));
                                                        }
                                                    }
                                                    if(dlf==false){
                                                        dataSnapshot.getRef().push().setValue(user);
                                                        snapshot.child(p).getRef().setValue(l_or_ds+1);
                                                        callback.onFailure(String.valueOf(l_or_ds+1), "");
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        } else{
                                            dataSnapshot.getRef().push().setValue(user);
                                            snapshot.child(p).getRef().setValue(l_or_ds+1);
                                            callback.onFailure(String.valueOf(l_or_ds+1), "");
                                        }
                                    } else if(l_or_d.equals("dislikers_ids")){ //already liked
                                        if (snapshot.hasChild("likers_ids")) {
                                            snapshot.child("likers_ids").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot lfDataSnapshot) {
                                                    Boolean lf = false;
                                                    int likes = (int) lfDataSnapshot.getChildrenCount();
                                                    for (DataSnapshot ds : lfDataSnapshot.getChildren()) {
                                                        if (ds.getValue().toString().equals(user)) {
                                                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> TRUE");
                                                            lf = true;
                                                            ds.getRef().removeValue();
                                                            snapshot.child("likes").getRef().setValue(likes - 1);

                                                            dataSnapshot.getRef().push().setValue(user);
                                                            snapshot.child(p).getRef().setValue(l_or_ds+1);

                                                            callback.onSuccess(String.valueOf(likes - 1), String.valueOf(l_or_ds+1));
                                                        }
                                                    }
                                                    if(lf==false){
                                                        dataSnapshot.getRef().push().setValue(user);
                                                        snapshot.child(p).getRef().setValue(l_or_ds+1);
                                                        callback.onFailure("", String.valueOf(l_or_ds+1));
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        } else{
                                            dataSnapshot.getRef().push().setValue(user);
                                            snapshot.child(p).getRef().setValue(l_or_ds+1);
                                            callback.onFailure("", String.valueOf(l_or_ds+1));
                                        }
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                    if(l_or_d.equals("likers_ids")){
                        ////
                        if (snapshot.hasChild("dislikers_ids")) {
                            snapshot.child("dislikers_ids").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dlfDataSnapshot) {
                                    Boolean dlf = false;
                                    int dislikes = (int) dlfDataSnapshot.getChildrenCount();
                                    for (DataSnapshot ds : dlfDataSnapshot.getChildren()) {
                                        if (ds.getValue().toString().equals(user)) {
                                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> TRUE");
                                            dlf = true;
                                            ds.getRef().removeValue();
                                            snapshot.child("dislikes").getRef().setValue(dislikes - 1);

                                            snapshot.child(l_or_d).getRef().push().setValue(user);
                                            snapshot.child(p).getRef().setValue(1);
                                            callback.onSuccess(String.valueOf(1), String.valueOf(dislikes-1));
                                        }
                                    }
                                    if(dlf==false){
                                        snapshot.child(l_or_d).getRef().push().setValue(user);
                                        snapshot.child(p).getRef().setValue(1);
                                        callback.onSuccess(String.valueOf(1), "");
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            ///
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> FALSE");
                            snapshot.child(l_or_d).getRef().push().setValue(user);
                            snapshot.child(p).getRef().setValue(1);
                            callback.onSuccess(String.valueOf(1), String.valueOf(0));
                        }
                    } else {
                        ////
                        if (snapshot.hasChild("likers_ids")) {
                            snapshot.child("likers_ids").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot lfDataSnapshot) {
                                    Boolean lf = false;
                                    int likes = (int) lfDataSnapshot.getChildrenCount();
                                    for (DataSnapshot ds : lfDataSnapshot.getChildren()) {
                                        if (ds.getValue().toString().equals(user)) {
                                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> TRUE");
                                            lf = true;
                                            ds.getRef().removeValue();
                                            snapshot.child("likes").getRef().setValue(likes - 1);

                                            snapshot.child(l_or_d).getRef().push().setValue(user);
                                            snapshot.child(p).getRef().setValue(1);
                                            callback.onSuccess(String.valueOf(likes - 1), String.valueOf(1));
                                        }
                                    }
                                    if(lf==false){
                                        snapshot.child(l_or_d).getRef().push().setValue(user);
                                        snapshot.child(p).getRef().setValue(1);
                                        callback.onSuccess("", String.valueOf(1));
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        ///
                        else {
                            snapshot.child(l_or_d).getRef().push().setValue(user);
                            snapshot.child(p).getRef().setValue(1);
                            callback.onSuccess(String.valueOf(0), String.valueOf(1));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private static void deleteComment(String commentId, String postId) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("global").child("comments").child(postId).child(commentId);
        database.removeValue();

        DatabaseReference post = FirebaseDatabase.getInstance().getReference().child("global").child("posts").child(postId).child("comments");
        post.setValue(FieldValue.increment(-50));
    }

    public static void saveRecentlyViewed(String index){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference recent = FirebaseDatabase.getInstance().getReference().child("global");
        List<String> visitors = new ArrayList<>();
        recent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("recent_views")){
                    DatabaseReference mDatabase = recent.child("recent_views");
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Boolean f = false;
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                RecentlyViewed recentlyViewed = ds.getValue(RecentlyViewed.class);
                                if(recentlyViewed.getUni_index().toString().equals(index) && !recentlyViewed.getVisitors().contains(userId)){
                                    Helper.print(recentlyViewed.getVisitors()+ ">>>>>>>>>>>>>>>>>>>>>>>>>>> ");
                                    recentlyViewed.setVisited_times(Integer.valueOf(ds.child("visited_times").getValue().toString()) + 1);
                                    recentlyViewed.getVisitors().add(userId);
                                    recentlyViewed.setTime(Helper.getTime());
                                    f=true;
                                    ds.getRef().setValue(recentlyViewed);
                                } else if(recentlyViewed.getUni_index().toString().equals(index) && recentlyViewed.getVisitors().contains(userId)){
                                    recentlyViewed.setTime(Helper.getTime());
                                    f=true;
                                    ds.getRef().setValue(recentlyViewed);
                                }
                            }
                            if(f == false){
                                visitors.add(userId);
                                RecentlyViewed rv = new RecentlyViewed(index, Helper.getTime(), 1, visitors);
                                mDatabase.child(index).setValue(rv);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else{
                    visitors.add(userId);
                    RecentlyViewed rv = new RecentlyViewed(index, Helper.getTime(), 1, visitors);
                    recent.child("recent_views").child(index).setValue(rv);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static void getRecentlyViewed(GetRecentlyViewedCallback getRecentlyViewedCallback){
        List<RecentlyViewed> rv = new ArrayList<RecentlyViewed>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("global");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("recent_views")){
                    DatabaseReference mDatabase = database.child("recent_views");
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                RecentlyViewed recentlyViewed = ds.getValue(RecentlyViewed.class);
                                rv.add(recentlyViewed);
                            }
                            getRecentlyViewedCallback.onSuccess(rv);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            getRecentlyViewedCallback.onFailure("f");
                        }
                    });
                } else{
                    getRecentlyViewedCallback.onFailure("nf");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                getRecentlyViewedCallback.onFailure("nf");
            }
        });
    }


    //SEARCH
    public static void searchPost(String q, String topic, GetPostsCallback getPostsCallback){
        List<Post> post = new ArrayList<Post>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("global");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("posts")){
                    DatabaseReference mDatabase = database.child("posts");
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                Post p = ds.getValue(Post.class);
                                if(q.equals("search")){
                                    String s = topic.toLowerCase();
                                    if(p.getMessage().toLowerCase().contains(s) || p.getTopic().toLowerCase().contains(s) ||
                                    p.getUsername().contains(s)){
                                        post.add(p);
                                    }
                                } else{
                                    if(p.getTopic().toLowerCase().equals(topic.toLowerCase())){
                                        post.add(p);
                                    }
                                }
                            }
                            getPostsCallback.onSuccess(post);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            getPostsCallback.onFailure("f");
                        }
                    });
                } else{
                    getPostsCallback.onFailure("nf");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                getPostsCallback.onFailure("nf");
            }
        });
    }

    public static void searchDecisions(String uni, GetDecisionsCallback getDecisionsCallback){
        List<Decisions> decisions = new ArrayList<Decisions>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("global");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("decisions")){
                    DatabaseReference mDatabase = database.child("decisions");
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                Decisions d = ds.getValue(Decisions.class);
                                if(d.getUni_name().toLowerCase().equals(uni.toLowerCase())){
                                    decisions.add(d);
                                }
                            }
                            getDecisionsCallback.onSuccess(decisions);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            getDecisionsCallback.onFailure("f");
                        }
                    });
                } else{
                    getDecisionsCallback.onFailure("nf");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                getDecisionsCallback.onFailure("nf");
            }
        });
    }
}

