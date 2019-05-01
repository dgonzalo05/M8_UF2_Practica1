package org.dgonzalo.mediaplayerapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements CancionFragment.OnFragmentInteractionListener, MediaPlayerFragment.miListener {


    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mediaPlayer = new MediaPlayer();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new CancionFragment();

        fm.beginTransaction().replace(R.id.fragment_cancion, fragment, "CANCION").commit();

        mDatabaseRef.child("canciones").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Cancion cancion = dataSnapshot.getValue(Cancion.class);
                CancionFragment cancionFragment = (CancionFragment) getSupportFragmentManager().findFragmentByTag("CANCION");
                if(cancionFragment != null){
                    cancionFragment.addCancion(cancion);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getImageUrl(String url, ImageView imagen) throws IOException {

        final ImageThread thread = new ImageThread(imagen);
        thread.execute(url);
    }

    @Override
    public void getAudioUrl(String url) {
        FirebaseStorage.getInstance()
            .getReference()
            .child(url)
            .getDownloadUrl()
            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(uri.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                    mediaPlayer.prepareAsync();
                }
            });
    }

    @Override
    public void stopAudio() {
        mediaPlayer.stop();
    }

    @Override
    public void startMediaPlayer(Cancion cancion) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = MediaPlayerFragment.newInstance(cancion);

        fm.beginTransaction().replace(R.id.fragment_cancion, fragment, "CANCION").commit();
    }


    public class ImageThread extends AsyncTask<String, Void, Bitmap> {

        ImageView imagen;
        public ImageThread(ImageView imagen){
            this.imagen = imagen;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            HttpURLConnection connection = null;
            URL url;
            Bitmap result = null;
            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                result = BitmapFactory.decodeStream(inputStream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(Bitmap data){
            super.onPostExecute(data);
            imagen.setImageBitmap(data);
        }
    }


}
