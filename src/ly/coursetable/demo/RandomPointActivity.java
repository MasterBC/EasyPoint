package ly.coursetable.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RandomPointActivity extends Activity {

	private MyDatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private ImageView iv;
	private TextView tv;
	private Button buttonFinish;
	private Button buttonStop;
	private Handler myHandler;
	private Thread t;
	public volatile boolean exit = false;// 使用退出标志终止线程
	public int count = 1;
	public StudentInfo student;

	List<StudentInfo> students;

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
		
		
		// 此处应使用SQLite数据库操作
//		StudentInfo stu1 = new StudentInfo("木婉清", "01", 0, "111");// 导入学生信息
//		StudentInfo stu2 = new StudentInfo("公孙绿萼", "02", 0, "222");
//		StudentInfo stu3 = new StudentInfo("任我行", "03", 0, "333");
//		StudentInfo stu4 = new StudentInfo("王语嫣", "04", 0, "444");
//		StudentInfo stu5 = new StudentInfo("风清扬", "05", 0, "555");
//		StudentInfo stu6 = new StudentInfo("独孤求败", "06", 0, "666");
//		StudentInfo stu7 = new StudentInfo("张无忌", "07", 0, "777");
//		StudentInfo stu8 = new StudentInfo("李秋水", "08", 0, "888");
//		StudentInfo stu9 = new StudentInfo("东方不败", "09", 0, "999");
//		StudentInfo stu10 = new StudentInfo("岳灵珊", "10", 0, "123");
//
//		students = new ArrayList<StudentInfo>();
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

		setContentView(R.layout.activity_random_point);// 设置RandomPoint的layout

		iv = (ImageView) findViewById(R.id.iv);// 找到显示学生图片的ImageView
		tv = (TextView) findViewById(R.id.tv);// 找到显示学生信息的TextView
		buttonFinish = (Button) findViewById(R.id.btnFinish);// 找到完成随机点到按钮
		buttonStop = (Button) findViewById(R.id.btnStop);// 找到停止随机点到按钮

		insertData();// 把数据加载进内存

		iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(RandomPointActivity.this,
						StudentInfoActivity.class));
			}
		});

		buttonStop.setOnClickListener(new View.OnClickListener() {
			// 设置监听器，当此按钮被按下时，停止线程循环
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				exit = true;// 设置退出标志为true，停止循环

				try {
					RandomPointActivity.this.t.join();// 将线程设定为守护线程
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				count++;
				if (count % 2 == 0) {
					exit = true;
					buttonStop.setText("继续");

					
					String name = tv.getText().toString();
					if (!students.isEmpty()) {
						for (StudentInfo stu : students) {
							if (stu.getName() == name) {
								student = stu;
							}
						}
					}
					
					StringBuilder sb = new StringBuilder();
					sb.append("姓名：" + student.getName());
					sb.append("\n");
					sb.append("学号： " + student.getStudentNumber());
					sb.append("\n");
					sb.append("未到次数： " + student.getLateCount());
					
					
					
					
					AlertDialog.Builder dialog = new AlertDialog.Builder(
							RandomPointActivity.this);
					dialog.setTitle("学生信息");
					dialog.setCancelable(false);
					dialog.setMessage(sb.toString());
					dialog.setNegativeButton("未到", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							count++;
//							String name = tv.getText().toString();
//							if (!students.isEmpty()) {
//								for (StudentInfo stu : students) {
//									if (stu.getName() == name) {
							
							student.setLateCount(student.getLateCount() + 1);
							ContentValues values = new ContentValues();
							values.put("latecount", student.getLateCount());
							db.update("student", values, "name = ?", new String[] {student.getName()});
							
							
							//发送短信提醒
							SmsManager manager = SmsManager.getDefault();  
			                ArrayList<String> texts = manager.divideMessage(student.getName().toString()+" 同学,在上课时点名点到你,特此短信通知!");  
			                try{  
			                    for(String text : texts){  
			                        manager.sendTextMessage(student.getPhoneNumber(), null, text, null, null);  
			                    }  
			                }catch (Exception e) {  
			                    Toast.makeText(RandomPointActivity.this, "短信通知失败,请检查短信是否正确", Toast.LENGTH_SHORT).show();  
			                }  
			                Toast.makeText(RandomPointActivity.this, "短信已发送", Toast.LENGTH_SHORT).show(); 
							
										int lateCount = student.getLateCount();
										student.setLateCount(lateCount++);
//									}
//								}
//							}
							exit = false;
							buttonStop.setText("停止");

							myHandler = new MyHandler();// 主要接受子线程发送的数据，
														// 并用此数据配合主线程更新UI。

							MyThread m = new MyThread();
							t = new Thread(m);
							t.start();
						}
					});
					dialog.setPositiveButton("已到", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							count++;
							exit = false;
							buttonStop.setText("停止");

							myHandler = new MyHandler();// 主要接受子线程发送的数据，
														// 并用此数据配合主线程更新UI。

							MyThread m = new MyThread();
							t = new Thread(m);
							t.start();
						}
					});
					dialog.show();
				} else {
					exit = false;
					buttonStop.setText("停止");

					myHandler = new MyHandler();// 主要接受子线程发送的数据， 并用此数据配合主线程更新UI。

					MyThread m = new MyThread();
					t = new Thread(m);
					t.start();
				}
			}
		});
		buttonFinish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(RandomPointActivity.this,
						EndActivity.class));// 按下此按钮后即跳转到点到完成界面
			}
		});

		myHandler = new MyHandler();// 主要接受子线程发送的数据， 并用此数据配合主线程更新UI。

		MyThread m = new MyThread();
		t = new Thread(m);
		t.start();
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
			// Log.d("MyHandler", "handleMessage。。。。。。");
			super.handleMessage(msg);
			// 此处可以更新UI
			Bundle b = msg.getData();
			String stuname = b.getString("stuname");
			tv.setText(stuname);

			if (!students.isEmpty()) {
				for (StudentInfo stu : students) {// 遍历所有学生信息
					if (stu.getName() == stuname) {
						// 当回显的姓名与数组里面的学生数据匹配时，则更换图片
						int id = Integer.parseInt(stu.getStudentNumber());
						String a = "a" + id;
						String imageUri;
						switch (a) {
						case "a1":
							imageUri = "drawable://" + R.drawable.a1; // from
																		// drawables
																		// (only
																		// images,
																		// non-9patch)
							ImageLoader.getInstance()
									.displayImage(imageUri, iv);
							// iv.setImageDrawable(getResources().getDrawable(R.drawable.a1));
							break;
						case "a2":
							imageUri = "drawable://" + R.drawable.a2; // from
																		// drawables
																		// (only
																		// images,
																		// non-9patch)
							ImageLoader.getInstance()
									.displayImage(imageUri, iv);
							// iv.setImageDrawable(getResources().getDrawable(R.drawable.a2));
							break;
						case "a3":
							imageUri = "drawable://" + R.drawable.a3; // from
																		// drawables
																		// (only
																		// images,
																		// non-9patch)
							ImageLoader.getInstance()
									.displayImage(imageUri, iv);
							// iv.setImageDrawable(getResources().getDrawable(R.drawable.a3));
							break;
						case "a4":
							imageUri = "drawable://" + R.drawable.a4; // from
																		// drawables
																		// (only
																		// images,
																		// non-9patch)
							ImageLoader.getInstance()
									.displayImage(imageUri, iv);
							// iv.setImageDrawable(getResources().getDrawable(R.drawable.a4));
							break;
						case "a5":
							imageUri = "drawable://" + R.drawable.a5; // from
																		// drawables
																		// (only
																		// images,
																		// non-9patch)
							ImageLoader.getInstance()
									.displayImage(imageUri, iv);
							// iv.setImageDrawable(getResources().getDrawable(R.drawable.a5));
							break;
						case "a6":
							imageUri = "drawable://" + R.drawable.a6; // from
																		// drawables
																		// (only
																		// images,
																		// non-9patch)
							ImageLoader.getInstance()
									.displayImage(imageUri, iv);
							// iv.setImageDrawable(getResources().getDrawable(R.drawable.a6));
							break;
						case "a7":
							imageUri = "drawable://" + R.drawable.a7; // from
																		// drawables
																		// (only
																		// images,
																		// non-9patch)
							ImageLoader.getInstance()
									.displayImage(imageUri, iv);
							// iv.setImageDrawable(getResources().getDrawable(R.drawable.a7));
							break;
						case "a8":
							imageUri = "drawable://" + R.drawable.a8; // from
																		// drawables
																		// (only
																		// images,
																		// non-9patch)
							ImageLoader.getInstance()
									.displayImage(imageUri, iv);
							// iv.setImageDrawable(getResources().getDrawable(R.drawable.a8));
							break;
						case "a9":
							imageUri = "drawable://" + R.drawable.a9; // from
																		// drawables
																		// (only
																		// images,
																		// non-9patch)
							ImageLoader.getInstance()
									.displayImage(imageUri, iv);
							// iv.setImageDrawable(getResources().getDrawable(R.drawable.a9));
							break;
						case "a10":
							imageUri = "drawable://" + R.drawable.a10; // from
																		// drawables
																		// (only
																		// images,
																		// non-9patch)
							ImageLoader.getInstance()
									.displayImage(imageUri, iv);
							// iv.setImageDrawable(getResources().getDrawable(R.drawable.a10));
							break;
						default:
							break;
						}
					}
				}
			}
		}
	}

	class MyThread implements Runnable {// 实现Runnable接口
		public void run() {
			while (!exit) {
				try {
					Thread.sleep(100);// 暂停100毫秒
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Log.d("thread。。。。。。。", "mThread。。。。。。。。");
				Message msg = new Message();
				Bundle b = new Bundle();// 存放数据

				int max = 10;
				int min = 1;
				Random random = new Random();
				int s = random.nextInt(max) % (max - min + 1) + min;// 生成1到6的随机数

				for (StudentInfo stu : students) {// 遍历所有学生信息
					if (Integer.parseInt(stu.getStudentNumber()) == s) {
						// 当学生的学号与生成的随机数S相同时，则获取该学生姓名，并且回送给MyHandler处理
						b.putString("stuname", stu.getName());
						msg.setData(b);
						RandomPointActivity.this.myHandler.sendMessage(msg); // 向Handler发送消息，更新UI
					}
				}
			}
		}
	}
}
