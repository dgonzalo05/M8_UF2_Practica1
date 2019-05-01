package org.dgonzalo.mediaplayerapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;


public class CancionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;
    private miAdapter recyclerV;
    private ArrayList<Cancion> canciones;

    public CancionFragment() {
        // Required empty public constructor
    }


    public static CancionFragment newInstance(String param1, String param2) {
        CancionFragment fragment = new CancionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View VistaFragment = inflater.inflate(R.layout.fragment_cancion, container, false);
        recyclerView = VistaFragment.findViewById(R.id.recyclerID);
        canciones = new ArrayList<Cancion>();
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerV = new miAdapter(canciones);

        recyclerView.setAdapter(recyclerV);

        return VistaFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public void addCancion(Cancion cancion){
        canciones.add(cancion);
        recyclerV.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name

        void getImageUrl(String url, ImageView imagen) throws IOException;
        void startMediaPlayer(Cancion cancion);
    }

    public class miAdapter extends RecyclerView.Adapter<miAdapter.miViewHolder> {
        ArrayList<Cancion> canciones;

        public miAdapter(ArrayList<Cancion> canciones){
            this.canciones = canciones;
        }

        @NonNull
        @Override
        public miViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cancion_layout,viewGroup,false);
            miViewHolder holder = new miViewHolder(v);
            v.setOnClickListener(holder);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull miViewHolder miViewHolder, int i) {
            try {
                mListener.getImageUrl(canciones.get(i).getImagen(),miViewHolder.imagen);
            } catch (IOException e) {
                e.printStackTrace();
            }
            miViewHolder.titulo.setText(canciones.get(i).getTitulo());
            miViewHolder.artista.setText(canciones.get(i).getArtista());
            miViewHolder.setCancion(canciones.get(i));
        }

        @Override
        public int getItemCount() {
            return canciones.size();
        }
        public class miViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView imagen;
            TextView titulo, artista;

            public void setCancion(Cancion cancion) {
                this.cancion = cancion;
            }

            Cancion cancion;
            public miViewHolder(@NonNull View itemView) {
                super(itemView);

                imagen = itemView.findViewById(R.id.cancion_image);
                titulo = itemView.findViewById(R.id.cancion_titulo);
                artista = itemView.findViewById(R.id.cancion_artista);

            }
            @Override
            public void onClick(View v) {
                mListener.startMediaPlayer(cancion);
            }
        }

    }


}
