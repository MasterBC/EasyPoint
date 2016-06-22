package ly.coursetable.demo;

import android.R.integer;


/**
 * @author Administrator
 * alt + shift + j
 *
 */
public class CourseInfo {
	
	private String courseText; 
	private int	courseCount;    
	private int courseBeginNum; 
	private int	weekNum;   
	
	public CourseInfo(String courseText, int courseCount, int courseBeginNum,
			int weekNum) {
		this.courseText = courseText;
		this.courseCount = courseCount;
		this.courseBeginNum = courseBeginNum;
		this.weekNum = weekNum;
	}
	
	public String getCourseText() {
		return courseText;
	}
	public void setCourseText(String courseText) {
		this.courseText = courseText;
	}
	public int getCourseCount() {
		return courseCount;
	}
	public void setCourseCount(int courseCount) {
		this.courseCount = courseCount;
	}
	public int getCourseBeginNum() {
		return courseBeginNum;
	}
	public void setCourseBeginNum(int courseBeginNum) {
		this.courseBeginNum = courseBeginNum;
	}
	public int getWeekNum() {
		return weekNum;
	}
	public void setWeekNum(int weekNum) {
		this.weekNum = weekNum;
	}
	@Override
	public String toString() {
		return "CourseInfo [courseText=" + courseText + ", courseCount="
				+ courseCount + ", courseBeginNum=" + courseBeginNum
				+ ", weekNum=" + weekNum + "]";
	}
	
	
}
