package ly.coursetable.demo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CourseTableActivity extends Activity {

	private MyDatabaseHelper dbHelper;// 操走SQLite数据库

	// 课程信息列表
	protected List<CourseInfo> courseInfos;
	// 控制课程信息的颜色
	private int colorIndex = 0;
	private Context mContext;

	/** 第一个无内容的格子 */
	protected TextView empty;
	/** 星期一的格子 */
	protected TextView monColum;
	/** 星期二的格子 */
	protected TextView tueColum;
	/** 星期三的格子 */
	protected TextView wedColum;
	/** 星期四的格子 */
	protected TextView thrusColum;
	/** 星期五的格子 */
	protected TextView friColum;
	/** 星期六的格子 */
	protected TextView satColum;
	/** 星期日的格子 */
	protected TextView sunColum;
	/** 课程表body部分布局 */
	protected RelativeLayout course_table_layout;
	/** 屏幕宽度 **/
	protected int screenWidth;
	/** 课程格子平均宽度 **/
	protected int aveWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coursetable);

		// 创建数据库
		dbHelper = new MyDatabaseHelper(this, "student.db", null, 1);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		// 开始组装第一条数据
		values.put("name", "罗建");
		values.put("latecount", 0);
		values.put("phonenumber", "+8618702515178");
		db.insert("student", null, values); // 插入第1条数据
		values.clear();
		// 开始组装第二条数据
		values.put("name", "刘北长");
		values.put("latecount", 0);
		values.put("phonenumber", "+8613155822484");
		db.insert("student", null, values); // 插入第2条数据
		values.clear();
		//3
		values.put("name", "任我行");
		values.put("latecount", 0);
		values.put("phonenumber", "333");
		db.insert("student", null, values); // 插入第3条数据
		values.clear();
		//4
		values.put("name", "王语嫣");
		values.put("latecount", 0);
		values.put("phonenumber", "444");
		db.insert("student", null, values); // 插入第4条数据
		values.clear();
		//5
		values.put("name", "风清扬");
		values.put("latecount", 0);
		values.put("phonenumber", "555");
		db.insert("student", null, values); // 插入第5条数据
		values.clear();
		//6
		values.put("name", "独孤求败");
		values.put("latecount", 0);
		values.put("phonenumber", "666");
		db.insert("student", null, values); // 插入第6条数据
		values.clear();
		//7
		values.put("name", "张无忌");
		values.put("latecount", 0);
		values.put("phonenumber", "777");
		db.insert("student", null, values); // 插入第7条数据
		values.clear();
		//8
		values.put("name", "李秋水");
		values.put("latecount", 0);
		values.put("phonenumber", "888");
		db.insert("student", null, values); // 插入第8条数据
		values.clear();
		//9
		values.put("name", "东方不败");
		values.put("latecount", 0);
		values.put("phonenumber", "999");
		db.insert("student", null, values); // 插入第9条数据
		values.clear();
		//10
		values.put("name", "岳灵珊");
		values.put("latecount", 0);
		values.put("phonenumber", "123");
		db.insert("student", null, values); // 插入第10条数据
		
		
		
		mContext = CourseTableActivity.this;
		// 获得列头的控件
		empty = (TextView) this.findViewById(R.id.test_empty);
		monColum = (TextView) this.findViewById(R.id.test_monday_course);
		tueColum = (TextView) this.findViewById(R.id.test_tuesday_course);
		wedColum = (TextView) this.findViewById(R.id.test_wednesday_course);
		thrusColum = (TextView) this.findViewById(R.id.test_thursday_course);
		friColum = (TextView) this.findViewById(R.id.test_friday_course);
		satColum = (TextView) this.findViewById(R.id.test_saturday_course);
		sunColum = (TextView) this.findViewById(R.id.test_sunday_course);
		course_table_layout = (RelativeLayout) this
				.findViewById(R.id.test_course_rl);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 屏幕宽度
		int width = dm.widthPixels;
		// 平均宽度
		int aveWidth = width / 8;
		// 第一个空白格子设置为25宽
		empty.setWidth(aveWidth * 3 / 4);
		monColum.setWidth(aveWidth * 33 / 32 + 1);
		tueColum.setWidth(aveWidth * 33 / 32 + 1);
		wedColum.setWidth(aveWidth * 33 / 32 + 1);
		thrusColum.setWidth(aveWidth * 33 / 32 + 1);
		friColum.setWidth(aveWidth * 33 / 32 + 1);
		satColum.setWidth(aveWidth * 33 / 32 + 1);
		sunColum.setWidth(aveWidth * 33 / 32 + 1);

		this.screenWidth = width;
		this.aveWidth = aveWidth;
		int height = dm.heightPixels;
		int gridHeight = height / 12;
		// 设置课表界面
		// 动态生成12 * maxCourseNum个textview
		for (int i = 1; i <= 12; i++) {

			for (int j = 1; j <= 8; j++) {

				TextView tx = new TextView(CourseTableActivity.this);
				tx.setId((i - 1) * 8 + j);
				// 除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
				if (j < 8)
					tx.setBackgroundDrawable(CourseTableActivity.this
							.getResources().getDrawable(
									R.drawable.course_text_view_bg));
				else
					tx.setBackgroundDrawable(CourseTableActivity.this
							.getResources().getDrawable(
									R.drawable.course_table_last_colum));

				// 相对布局参数
				RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
						aveWidth * 33 / 32 + 1, gridHeight);
				// 文字对齐方式
				tx.setGravity(Gravity.CENTER);
				// 字体样式
				tx.setTextAppearance(this, R.style.courseTableText);

				// 如果是第一列，需要设置课的序号（1 到 12）
				if (j == 1) {
					tx.setText(String.valueOf(i));
					rp.width = aveWidth * 3 / 4;
					// 设置他们的相对位置
					if (i == 1)
						rp.addRule(RelativeLayout.BELOW, empty.getId());
					else
						rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
				} else {
					rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8 + j - 1);
					rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8 + j - 1);
					tx.setText("");
				}

				tx.setLayoutParams(rp);
				course_table_layout.addView(tx);
			}
		}

		// 设置课程信息
		setCourseInfos();

		for (CourseInfo courseInfo : courseInfos) {

			TextView courseInfoView = createCourseInfoView(gridHeight,
					courseInfo);

			course_table_layout.addView(courseInfoView);
		}

	}

	// 在课程表上创造显示单个课程信息的textview
	public TextView createCourseInfoView(int gridHeight,
			final CourseInfo courseInfo) {

		// 五种颜色的背景
		int[] background = { R.drawable.course_info_blue,
				R.drawable.course_info_green, R.drawable.course_info_red,
				R.drawable.course_info_red, R.drawable.course_info_yellow };
		// 添加课程信息
		TextView courseInfoView = new TextView(this);

		courseInfoView.setText(courseInfo.getCourseText());
		// 该textview的高度根据其节数的跨度来设置
		// 设置上课的节数
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				aveWidth * 31 / 32, (gridHeight - 5)
						* courseInfo.getCourseCount());
		// textview的位置由课程开始节数和上课的时间（day of week）确定
		rlp.topMargin = 5 + (courseInfo.getCourseBeginNum() - 1) * gridHeight;
		rlp.leftMargin = 1;
		// 偏移由这节课是星期几决定
		rlp.addRule(RelativeLayout.RIGHT_OF, courseInfo.getWeekNum());

		// 字体剧中
		courseInfoView.setGravity(Gravity.CENTER);

		// 设置一种背景
		if (colorIndex >= background.length)
			colorIndex = 0;
		courseInfoView.setBackgroundResource(background[colorIndex]);
		colorIndex++;

		courseInfoView.setTextSize(12);
		courseInfoView.setLayoutParams(rlp);
		courseInfoView.setTextColor(Color.WHITE);
		// 设置不透明度
		courseInfoView.getBackground().setAlpha(222);
		// 设置监听事件
		courseInfoView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				initPopWindow(v);
			}
		});

		return courseInfoView;
	}

	public void setCourseInfos(List<CourseInfo> courseInfos) {
		this.courseInfos = courseInfos;
	}

	public void setCourseInfos() {
		courseInfos = new ArrayList<CourseInfo>();
		courseInfos.add(new CourseInfo("软件工程\n@w4302", 9, 5, 2));
		courseInfos.add(new CourseInfo("计算机网络\n@w4406", 2, 2, 3));
	}

	private void initPopWindow(View v) {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.course_operate_popitem, null, false);

		TextView tv_random_point = (TextView) view
				.findViewById(R.id.tv_random_point);
		TextView tv_all_point = (TextView) view.findViewById(R.id.tv_all_point);
		TextView tv_look_record = (TextView) view
				.findViewById(R.id.tv_look_record);
		// 1.构造一个PopupWindow，参数依次是加载的View，宽高
		final PopupWindow popWindow = new PopupWindow(view,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popWindow.setAnimationStyle(R.anim.course_operate_anim); // 设置加载动画
		// 这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
		// 代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
		// PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
		popWindow.setTouchable(true);
		popWindow.setTouchInterceptor(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000)); // 要为popWindow设置一个背景才有效

		// 设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
		// popWindow.showAsDropDown(v, 0, 0);
		popWindow.showAtLocation(findViewById(R.id.scroll_body), Gravity.RIGHT
				| Gravity.BOTTOM, 0, 0);

		// 设置popupWindow里的按钮的事件
		tv_random_point.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(mContext, "你点击了随机点到",
				// Toast.LENGTH_SHORT).show();
				popWindow.dismiss();
				startActivity(new Intent(CourseTableActivity.this,
						RandomPointActivity.class));
			}
		});
		tv_all_point.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(mContext, "你点击了全部点到",
				// Toast.LENGTH_SHORT).show();
				popWindow.dismiss();
				startActivity(new Intent(CourseTableActivity.this,
						CompletePointActivity.class));
			}
		});
		tv_look_record.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "你点击了查看点到记录", Toast.LENGTH_SHORT)
						.show();
				popWindow.dismiss();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
