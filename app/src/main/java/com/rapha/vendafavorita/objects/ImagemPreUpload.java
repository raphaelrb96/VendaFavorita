package com.rapha.vendafavorita.objects;

import android.net.Uri;

public class ImagemPreUpload {

    String path;
    Uri uri;
    boolean liberada;
    boolean main;

    public ImagemPreUpload(String path, Uri uri, boolean liberada, boolean main) {
        this.path = path;
        this.uri = uri;
        this.liberada = liberada;
        this.main = main;
    }

    public boolean isMain() {
        return main;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Uri getUri() {
        return uri;
    }

    public boolean taLiberado() {
        return liberada;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public void setLiberada(boolean liberada) {
        this.liberada = liberada;
    }
}
