package isllyend.top.conciseweather.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Chigo on 2017/3/20.
 * Email:isllyend@gmail.com
 */

public class BitmapUtil {
    public static void saveBitmap(Bitmap bm) {
        File sd= Environment.getExternalStorageDirectory();
        String path=sd.getPath()+"/cweather";
        File file=new File(path);
        if(!file.exists()){
            file.mkdir();
        }
        File f = new File(file.getAbsolutePath(), "share.jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
//            bm.recycle();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
