package edu.nus.trailblazelearn.utility;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Created by shashwatjain on 9/3/18.
 */

public class localDB {

    public void saveLocally(Context context, Map<String, Object> data, String fileName) {
        try {
//            FileOutputStream outputStream = new FileOutputStream(fileName);
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getFromLocal(Context context, String fileName) {
        try {
//            FileInputStream fileInputStream = new FileInputStream(fileName);
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Map myNewlyReadInMap = (Map) objectInputStream.readObject();
            objectInputStream.close();
            return myNewlyReadInMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
