package org.dgonzalo.mediaplayerapp;

public class Cancion {

    private String imagen;
    private String titulo;
    private String artista;

    private String cancion;

    public Cancion(){
    }
    public Cancion(String imagen, String titulo, String artista, String cancion) {
        this.imagen = imagen;
        this.titulo = titulo;
        this.artista = artista;
        this.cancion = cancion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getCancion() {
        return cancion;
    }

    public void setCancion(String cancion) {
        this.cancion = cancion;
    }



}
