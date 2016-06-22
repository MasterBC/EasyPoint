package ly.coursetable.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ly.coursetable.demo.RandomPointActivity.MyHandler;
import ly.coursetable.demo.RandomPointActivity.MyThread;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CompletePointActivity extends Activity {

	private MyDatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private Handler myHandler = null;
	private Thread t = null;

	private List<StudentInfo> students = null;
	private StudentInfo student = null;
	public volatile int id = 1;
	public volatile boolean exit = true;// 使用退出标志终止线程
	public volatile int count = 1;
	private ImageView ivStu;
	private TextView etStuName;
	private TextView etStuId;
	private TextView etStuLateCount;
	private Button btnStuLate;
	private Button btnStuCome;

	public void insertData() {

		dbHelper = new MyDatabaseHelper(this, "student.db", null, 1);
		db = dbHelper.getWritableDatabase();
		// 查询student 表中所有的数据
		Cursor cursor = db.query("student", null, null, null, null, null, null);
		int id = 1;
		students = new ArrayList<StudentInfo>();
		if (cursor.moveToFirst()) {
			do {
				// 遍历Cursor 对象，取出数据并打印
				String name = cursor.getString(cursor.getColumnIndex("name"));
				int latecount = cursor.getInt(cursor
						.getColumnIndex("latecount"));
				String phonenumber = cursor.getString(cursor
						.getColumnIndex("phonenumber"));
				Log.d("CompletePointActivity", "book name is " + name);
				Log.d("CompletePointActivity", "book latecount is " + latecount);
				Log.d("CompletePointActivity", "book phonenumber is " + phonenumber);
				StudentInfo stu = new StudentInfo(name, Integer.toString(id),latecount, phonenumber);
				students.add(stu);
				id++;
			} while (cursor.moveToNext());
		}
		cursor.close();

//		// 此处应使用SQLite数据库操作
//		StudentInfo stu1 = new StudentInfo("罗建", "01", 0, "+8618702515178");// 导入学生信息
//		StudentInfo stu2 = new StudentInfo("刘北长", "02", 0, "+8613155822484");
//		StudentInfo stu3 = new StudentInfo("任我行", "03", 0, "333");
//		StudentInfo stu4 = new StudentInfo("王语嫣", "04", 0, "444");
//		StudentInfo stu5 = new StudentInfo("风清扬", "05", 0, "555");
//		StudentInfo stu6 = new StudentInfo("独孤求败", "06", 0, "666");
//		StudentInfo stu7 = new StudentInfo("张无忌", "07", 0, "777");
//		StudentInfo stu8 = new StudentInfo("李秋水", "08", 0, "888");
//		StudentInfo stu9 = new StudentInfo("东方不败", "09", 0, "999");
//		StudentInfo stu10 = new StudentInfo("岳灵珊", "10", 0, "123");
//
//		
//
//		students.add(stu1);// 把学生信息存放到ArrayList中
//		students.add(stu2);
//		students.add(stu3);
//		students.add(stu4);
//		students.add(stu5);
//		students.add(stu6);
//		students.add(stu7);
//		students.add(stu8);
//		students.add(stu9);
//		students.add(stu10);
//		student = stu1;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		ImageLoaderConfiguration config = new ImageLoaderConfiguration// 异步加载图片配置
		.Builder(this)
				.memoryCacheExtraOptions(480, 800)
				// max width, max height，即保存的每个缓存文件的最大长宽
				// .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75,
				// null) // Can slow ImageLoader, use it carefully (Better don't
				// use it)/设置缓存的详细信息，最好不要设置这个
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// You can pass your own memory cache
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100)
				// 缓存的文件数量
				.discCache(new UnlimitedDiskCache(getCacheDir()))
				// 自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout
																			// (5
																			// s),
																			// readTimeout
																			// (30
																			// s)超时时间
				.writeDebugLogs() // Remove for release app
				.build();// 开始构建
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);// 全局初始化此配置

		setContentView(R.layout.activity_complete_point);

		ivStu = (ImageView) findViewById(R.id.ivStu);
		etStuName = (TextView) findViewById(R.id.stuName);
		etStuId = (TextView) findViewById(R.id.stuId);
		etStuLateCount = (TextView) findViewById(R.id.stuLateCount);
		btnStuLate = (Button) findViewById(R.id.btnStuLate);
		btnStuCome = (Button) findViewById(R.id.btnStuCome);

		insertData();

		myHandler = new MyHandler();
		MyThread m = new MyThread();
		t = new Thread(m);
		t.start();
		btnStuLate.setOnClickListener(new View.OnClickListener() {
			// 设置监听器，当此按钮被按下时，停止线程循环
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// 向该学生发送一条短信

				SmsManager manager = SmsManager.getDefault();
				ArrayList<String> texts = manager.divideMessage(student
						.getName().toString()
						+ " 同学,在上课时点名点到你,你的旷课次数过多,请及时前来上课!特此通知!");
				try {
					for (String text : texts) {
						manager.sendTextMessage(student.getPhoneNumber(), null,
								text, null, null);
					}
					Toast.makeText(CompletePointActivity.this, "短信已发送",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(CompletePointActivity.this,
							"短信通知失败,请检查短信是否正确", Toast.LENGTH_SHORT).show();
				}
				

				// 未到次数加1
				student.setLateCount(student.getLateCount() + 1);
				ContentValues values = new ContentValues();
				values.put("latecount", student.getLateCount());
				db.update("student", values, "name = ?", new String[] {student.getName()});
				if (id == 10) {
					exit = true;

					try {
						CompletePointActivity.this.t.join();// 将线程设定为守护线程
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Toast.makeText(CompletePointActivity.this, "本次点到已经完成",
							Toast.LENGTH_SHORT).show();
					finish();
					startActivity(new Intent(CompletePointActivity.this,
							EndActivity.class));

				}

				exit = false;// 循环开始
				myHandler = new MyHandler();
				MyThread m = new MyThread();
				t = new Thread(m);
				t.start();

			}
		});

		btnStuCome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (id == 10) {

					exit = true;
					try {
						CompletePointActivity.this.t.join();// 将线程设定为守护线程
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Toast.makeText(CompletePointActivity.this, "本次点到已经完成",
							Toast.LENGTH_SHORT).show();
					finish();
					startActivity(new Intent(CompletePointActivity.this,
							EndActivity.class));

				}

				exit = false;// 循环开始
				myHandler = new MyHandler();
				MyThread m = new MyThread();
				t = new Thread(m);
				t.start();
			}
		});

	}

	class MyHandler extends Handler {
		public MyHandler() {
		}

		public MyHandler(Looper L) {
			super(L);
		}

		// 子类必须重写此方法，接受数据
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// 此处可以更新UI
			Bundle b = msg.getData();
			StudentInfo student = (StudentInfo) b.getParcelable("student");
			// StudentInfo student = (StudentInfo) b.getSerializable("student");
			etStuName.setText(student.getName());
			etStuId.setText(student.getStudentNumber());
			etStuLateCount.setText(Integer.toString(student.getLateCount()));
			// 当回显的姓名与数组里面的学生数据匹配时，则更换图片
			int id = Integer.parseInt(student.getStudentNumber());
			String a = "a" + id;
			String imageUri;
			switch (a) {
			case "a1":
				imageUri = "drawable://" + R.drawable.a1;
				ImageLoader.getInstance().displayImage(imageUri, ivStu);
				break;
			case "a2":
				imageUri = "drawable://" + R.drawable.a2;
				ImageLoader.getInstance().displayImage(imageUri, ivStu);
				break;
			case "a3":
				imageUri = "drawable://" + R.drawable.a3;
				ImageLoader.getInstance().displayImage(imageUri, ivStu);
				break;
			case "a4":
				imageUri = "drawable://" + R.drawable.a4;
				ImageLoader.getInstance().displayImage(imageUri, ivStu);
				break;
			case "a5":
				imageUri = "drawable://" + R.drawable.a5;
				ImageLoader.getInstance().displayImage(imageUri, ivStu);
				break;
			case "a6":
				imageUri = "drawable://" + R.drawable.a6;
				ImageLoader.getInstance().displayImage(imageUri, ivStu);
				break;
			case "a7":
				imageUri = "drawable://" + R.drawable.a7;
				ImageLoader.getInstance().displayImage(imageUri, ivStu);
				break;
			case "a8":
				imageUri = "drawable://" + R.drawable.a8;
				ImageLoader.getInstance().displayImage(imageUri, ivStu);
				break;
			case "a9":
				imageUri = "drawable://" + R.drawable.a9;
				ImageLoader.getInstance().displayImage(imageUri, ivStu);
				break;
			case "a10":
				imageUri = "drawable://" + R.drawable.a10;
				ImageLoader.getInstance().displayImage(imageUri, ivStu);
				break;
			default:
				break;
			}

		}
	}

	class MyThread implements Runnable {// 实现Runnable接口
		public void run() {

			if (id == 1) {
				exit = false;
			}
			while (!exit) {
				exit = true;
				// try {
				// Thread.sleep(1000);// 暂停1秒
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				// Log.d("thread。。。。。。。", "mThread。。。。。。。。");
				Message msg = new Message();
				Bundle b = new Bundle();// 存放数据

				for (StudentInfo stu : students) {// 遍历所有学生信息
					if (Integer.parseInt(stu.getStudentNumber()) == id) {
						// b.putSerializable("student", stu);
						b.putParcelable("student", stu);
						student = stu;
						msg.setData(b);
					}
				}
				// if(id == 10){
				// id=0;
				// }

				Log.i("lbc log", Integer.toString(id));
				id++;

				CompletePointActivity.this.myHandler.sendMessage(msg); // 向Handler发送消息，更新UI
			}
		}
	}

}
