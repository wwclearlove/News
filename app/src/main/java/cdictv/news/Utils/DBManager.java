package cdictv.news.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import cdictv.news.R;

public class DBManager {
    private final String TAG = "DBManager";
    private final int BUFFER_SIZE = 1024;
    public static final String DB_NAME = "User.db";
    public static final String PACKAGE_NAME = "cdictv.news";
    public static final String DB_PATH = "/data"+
            Environment.getDataDirectory().getAbsolutePath()+
            "/"+PACKAGE_NAME+"/databaser";

    private SQLiteDatabase database;
    private Context context;


    public DBManager(Context context){
        this.context =context;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public void openDatabase(){
        database = openDatabase(DB_PATH +"/"+DB_NAME);
    }

    private SQLiteDatabase openDatabase(String dbfile){
        SQLiteDatabase db = null;
        File file = new File(DB_PATH);
        if(!file.exists()){
            file.mkdir();
        }
        if(!(new File(dbfile).isDirectory())){
            try {
                InputStream in = this.context.getResources().openRawResource(R.raw.user);
                FileOutputStream fos = new FileOutputStream(file);
                byte []bys = new byte[BUFFER_SIZE];
                int len = 0;
                while((len = in.read(bys))>0){
                    fos.write(bys,0,len);
                }
                fos.close();
                in.close();
                db = SQLiteDatabase.openOrCreateDatabase(file,null);
                Toast.makeText(context,"数据库导入完毕！",Toast.LENGTH_SHORT).show();
                return db;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       return null;
    }

    public void closeDatabase(){
        if(database != null){
            database.close();
        }

    }
}
