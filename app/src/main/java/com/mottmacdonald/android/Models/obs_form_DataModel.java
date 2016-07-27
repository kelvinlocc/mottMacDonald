package com.mottmacdonald.android.Models;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by KelvinLo on 7/22/2016.
 */


public class obs_form_DataModel {

    private Integer itemNo; // head, dynamicalyy added
    private String Recommedation;
    private String toBeRemediated_before;
    private String followUpAction;

    private File file;
    private Bitmap bitmap;

    private double[] shop_coordinate = new double[2];
    private int user_shop_distance;
    private Boolean[] reactList = new Boolean[3];



    public void setBitmap(Bitmap data){
        this.bitmap = data;
    }
    public Bitmap getBitmap (){
        return this.bitmap;
    }

    public void setRecommedation(String data){
        this.Recommedation = data;
    }

    public String getRecommedation(){
        return this.Recommedation;
    }
    public void setItemNo(Integer value) {
        this.itemNo= value;
    }

    public Integer getItemNo (){
        return this.itemNo;
    }

    public void setToBeRemediated_before (String data) {
        this.toBeRemediated_before = data;
    }
    public String getToBeRemediated_before (){
        return this.toBeRemediated_before;
    }

    public void  setFollowUpAction (String string) {
        this.followUpAction = string;
    }

    public String getFollowUpAction (){
        return this.followUpAction;
    }






    public void setFile(File data){
        this.file = data;
    }

    public File getFile(){
        return this.file;
    }







}
