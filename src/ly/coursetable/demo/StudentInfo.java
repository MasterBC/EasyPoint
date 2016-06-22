package ly.coursetable.demo;

import android.os.Parcel;
import android.os.Parcelable;


public class StudentInfo implements Parcelable{
	private String name;//学生姓名
	private String studentNumber;//学生学号
	private int lateCount;//学生迟到次数
	private String phoneNumber;//学生电话号码
	
	public StudentInfo(){
		
	}
	public StudentInfo(String name,String studentNumber,int lateCount,String phoneNumber){
		this.name = name;
		this.studentNumber = studentNumber;
		this.lateCount = lateCount;
		this.phoneNumber = phoneNumber;
	}
	private StudentInfo(Parcel in){
		this.name = in.readString();
		this.studentNumber = in.readString();
		this.lateCount = in.readInt();
		this.phoneNumber = in.readString();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public int getLateCount() {
		return lateCount;
	}
	public void setLateCount(int lateCount) {
		this.lateCount = lateCount;
	}
	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	public String getPhoneNumber(){
		return phoneNumber;
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(getName());
		dest.writeString(getStudentNumber());
		dest.writeInt(getLateCount());
		dest.writeString(getPhoneNumber());
	}
	public static final Creator<StudentInfo> CREATOR = new Creator<StudentInfo>() {
        @Override
        public StudentInfo createFromParcel(Parcel in) {
            return new StudentInfo(in);
        }

        @Override
        public StudentInfo[] newArray(int size) {
            return new StudentInfo[size];
        }
    };
}
