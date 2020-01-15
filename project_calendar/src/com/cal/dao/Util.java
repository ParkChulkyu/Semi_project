package com.cal.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import com.cal.dto.CalDto;

public class Util {
	
	private String todates;
	
	public String getTodates() {
		
		return todates;
	}
	
	public void setTodates(String mdate) {
		// yyyy-MM-dd hh:mm:ss
		String m = mdate.substring(0,4) + "-" + 
				   mdate.substring(4,6) + "-" +
				   mdate.substring(6,8) + " " +
				   mdate.substring(8,10) + ":" +
				   mdate.substring(10) + ":00"; 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년MM월dd일 HH시mm분");
		Timestamp tm = Timestamp.valueOf(m);
		// Timestamp 알아보기 시간표현하는 함수
		todates = sdf.format(tm);
	}
	

	// 한자리수를 두자리수로 변환
	public static String isTwo(String msg) {
		return (msg.length()<2)?"0"+msg:msg;
	}
	
	// 달력의 토요일, 일요일, 평일 글자 색상 바꾸는 메소드
	// dayOfWeek : 요일
	// date : 날짜
	public static String fontColor(int date, int dayOfWeek) {
		
		String color = "";
		
		// 토요일 캘린더에서 return이 1이면 일요일이다. 0이 토요일
		if((dayOfWeek-1+date)%7==0) {
			color = "bule";
		} else if((dayOfWeek-1+date)%7==1) {
			color = "red";
		} else {
			color = "black";
		}
		
		return color;
	}
	
	public static String getCalView(int i, List<CalDto> clist) {
		
		String d = isTwo(i+"");
		String res = "";
		
		for(CalDto dto : clist) {
			// 짜른게 우리가 정해준 d와 똑같다면
			if(dto.getMdate().substring(6, 8).equals(d)) {
				res += "<p>" +
						((dto.getTitle().length() > 6)?
								// 제목이  0부터 6자리까지 잘랐는데도 길면 문자열 ...을 출력한다.
								dto.getTitle().substring(0,6) + "...":
									dto.getTitle()) +
						"</p>";
			}
		}
		
		return res;
	}
}
