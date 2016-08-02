package com.omenacle.bamzu.models;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by omegareloaded on 7/28/16.
 */
public class User {

    //User Details
    private String uId;
    private String email;
    private String providerId;
    private String displayName;
    private Uri photoUrl;

    public User(){

    }

    public User(FirebaseUser user) {
        this.uId = user.getUid();
        this.email = user.getEmail();
        this.providerId = user.getProviderId();
        this.displayName = user.getDisplayName();
        this.photoUrl = user.getPhotoUrl();
    }


    private void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    private void setEmail(String email){
        this.email = email;
    }

    public String toString(){
        return this.uId;
    }



}
