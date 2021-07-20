package com.example.gradmates;

import org.parceler.Parcel;

//Class Post is not parcelable, so this class allows us to parse in DetailsActivity
@Parcel
public class ParcelableObject {

    Post post;

    public ParcelableObject(){}

    public void setPost(Post post){
        this.post = post;
    }
    public Post getPost(){
        return post;
    }
}
