package com.nanodegree.swatisingh.baking.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipe implements Parcelable{
    public final int id;
    public final String title;
    public ArrayList<String> ingredients;
    public ArrayList<String> steps;
    public ArrayList<String> instructions;
    public ArrayList<String> media;


    public Recipe(int id, String title, ArrayList<String> ingredients, ArrayList<String> shortDesc, ArrayList<String> desc, ArrayList<String> media) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = shortDesc;
        this.instructions = desc;
        this.media = media;
    }

    private Recipe(Parcel parcel){
        id = parcel.readInt();
        title = parcel.readString();
        ingredients = new ArrayList<String>();
        steps = new ArrayList<String>();
        instructions = new ArrayList<String>();
        media = new ArrayList<String>();
        parcel.readList(ingredients, getClass().getClassLoader());
        parcel.readList(steps,getClass().getClassLoader());
        parcel.readList(instructions, getClass().getClassLoader());
        parcel.readList(media,getClass().getClassLoader());
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeList(ingredients);
        parcel.writeList(steps);
        parcel.writeList(instructions);
        parcel.writeList(media);
    }
}
