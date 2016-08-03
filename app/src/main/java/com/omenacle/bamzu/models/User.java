package com.omenacle.bamzu.models;

import android.net.Uri;

/**
 * Created by omegareloaded on 7/28/16.
 */
public class User {

    //User Details
    public String email;
    public String displayName;
    public Uri photoUrl;

    public User() {

    }

    public User(String displayName, String email, Uri photoUrl) {
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
    }


    private void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    private void setEmail(String email) {
        this.email = email;
    }


    public String toString() {
        return this.displayName;
    }


}
