package ly.coursetable.demo;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.FrameLayout;

public class InitActivity extends Activity{
	
	private FrameLayout frameLayout;
	private SplashView splashView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// 帧布局
		frameLayout = new FrameLayout(this);
		
		// 添加SplashView
		splashView = new SplashView(this);
		frameLayout.addView(splashView);
		setContentView(frameLayout);
		// 开启Splash动画 --- 模拟后台加载数据
		startLoad();
	}

	private Handler handler = new Handler();

	private void startLoad() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 数据加载完毕执行后面的动作 -- 让ContentView显示
				splashView.splashAndDisapper();
				startActivity(new Intent(InitActivity.this, CourseTableActivity.class));
				finish();
			}
		}, 3000);
		
	}
}
