package ly.coursetable.demo;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {



	public static final String CREATE_STUDENT = "create table student ("
			+ "id integer primary key autoincrement, " 
			+ "name text, "
			+ "latecount integer, " 
			+ "phonenumber text)";
	private Context mContext;
	
	public MyDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_STUDENT);
		Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
