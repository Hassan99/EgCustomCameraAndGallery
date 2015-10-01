package com.example.samples.additionalclasses;

import android.net.Uri;


/**
 * Created by hassan on 14-09-15.
 */
public class TempModels {

    private static TempModels temp;
    private Uri uri;

    public static TempModels getInstance() {
        if (temp == null) {
            temp = new TempModels();
        }
        return temp;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }



}
