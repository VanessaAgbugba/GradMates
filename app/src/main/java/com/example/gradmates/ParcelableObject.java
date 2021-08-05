package com.example.gradmates;

import com.example.gradmates.Posts.ComposeActivity;

import org.parceler.Parcel;

//Class Post is not parcelable, so this class allows us to parse in DetailsActivity
@Parcel
public class ParcelableObject {

    ComposeActivity.Post post;

    public ParcelableObject(){}

    public void setPost(ComposeActivity.Post post){
        this.post = post;
    }
    public ComposeActivity.Post getPost(){
        return post;
    }
}
