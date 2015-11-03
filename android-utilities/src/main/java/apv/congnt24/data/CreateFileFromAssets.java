package apv.congnt24.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Debug;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CreateFileFromAssets {

    Context context;
    private  static CreateFileFromAssets instance;

    public static CreateFileFromAssets getInstance(){
        if (instance == null){
            instance = new CreateFileFromAssets();
        }
        return instance;
    }

    public CreateFileFromAssets initialize(Context context){
        this.context = context;
        return this;
    }
    public CreateFileFromAssets CreateFileFromPath(String path){
        AssetManager assetManager = context.getAssets();
        File dir = new File(context.getFilesDir().getPath()+"/"+path);
        if (!dir.exists()){
            dir.mkdir();
        }
        String[] listFile = new String[0];
        try {
            listFile = assetManager.list(path);
            for (int i = 0; i < listFile.length; i++) {
                CreateOneFile(path+"/"+listFile[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }
    public CreateFileFromAssets CreateOneFile(String fileName){
        String path = context.getFilesDir().getPath()+"/"+fileName;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open(fileName);
            File file = new File(path);
            if (!file.exists()){
                file.createNewFile();
            }
            int read = 0;
            outputStream = new FileOutputStream(file);
            byte[] offer = new byte[1024];
            while ( (read = inputStream.read(offer)) != -1){
                outputStream.write(offer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return this;
    }
}
