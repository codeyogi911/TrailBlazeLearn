package edu.nus.trailblazelearn.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;



public class UploadedFiles {
    public static ArrayList<String> downLoadUri;
    public ArrayList<String> fileType;

    public UploadedFiles(ArrayList<String> uri, ArrayList<String> fileType) {
        this.downLoadUri = uri;
        this.fileType = fileType;
    }

    public ArrayList<String> getFileType() {
        return fileType;
    }

    public void setFileType(ArrayList<String> fileType) {
        this.fileType = fileType;
    }

    public ArrayList<String> getFileUri() {
        return downLoadUri;
    }

    public void setFileUri(ArrayList<String> fileUri) {
        this.downLoadUri = fileUri;
    }
}
