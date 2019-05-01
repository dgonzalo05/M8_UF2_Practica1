package org.dgonzalo.mediaplayerapp;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link miListener} interface
 * to handle interaction events.
 * Use the {@link MediaPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaPlayerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ARTISTA = "artista";
    private static final String ARG_CANCION = "cancion";
    private static final String ARG_TITULO = "titulo";
    private static final String ARG_IMAGEN = "imagen";

    // TODO: Rename and change types of parameters
    private Cancion cancion;
    private String songurl,artista,titulo,imagenurl;

    private Button playButton;
    private ImageView imageView;
    private TextView tituloView, artistaView;
    private boolean isPlaying;
    private float imageX, imageY;
    private miListener mListener;
    View.OnClickListener pulsacion;

    public MediaPlayerFragment() {
        // Required empty public constructor
    }

    public static MediaPlayerFragment newInstance(Cancion cancion) {
        MediaPlayerFragment fragment = new MediaPlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ARTISTA, cancion.getArtista());
        args.putString(ARG_CANCION, cancion.getCancion());
        args.putString(ARG_TITULO, cancion.getTitulo());
        args.putString(ARG_IMAGEN, cancion.getImagen());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artista = getArguments().getString(ARG_ARTISTA);
            songurl = getArguments().getString(ARG_CANCION);
            titulo = getArguments().getString(ARG_TITULO);
            imagenurl = getArguments().getString(ARG_IMAGEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View VistaFragment = inflater.inflate(R.layout.fragment_media_player, container, false);
        imageView = VistaFragment.findViewById(R.id.imagen);
        playButton = VistaFragment.findViewById(R.id.play_button);
        artistaView = VistaFragment.findViewById(R.id.artista);
        tituloView = VistaFragment.findViewById(R.id.titulo);

        artistaView.setText(artista);
        tituloView.setText(titulo);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        try {
            mListener.getImageUrl(imagenurl,imageView);
        } catch (IOException e) {
            e.printStackTrace();
        }

        isPlaying = false;
        pulsacion = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    imageView.animate().scaleX(1).scaleY(1).setDuration(1000);
                    mListener.stopAudio();
                    isPlaying = false;

                }
                else {
                    imageX = imageView.getX();
                    imageY = imageView.getY();
                    imageView.animate().scaleX(width/imageX).scaleY(width/imageY).setDuration(1000);
                    mListener.getAudioUrl(songurl);
                    isPlaying = true;

                }

            }
        };
        playButton.setOnClickListener(pulsacion);


        return VistaFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof miListener) {
            mListener = (miListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement miListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface miListener {
        // TODO: Update argument type and name
        void getImageUrl(String url, ImageView imagen) throws IOException;
        void getAudioUrl(String url);
        void stopAudio();

    }
}
