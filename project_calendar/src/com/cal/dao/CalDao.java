package com.cal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cal.dto.CalDto;
import static com.cal.db.JDBCTemplate.*;

public class CalDao {
	
	public List<CalDto> getCalList(String id, String yyyyMMdd){
		Connection con = getConnection();
		List<CalDto> list = new ArrayList<CalDto>();
		String sql = " SELECT SEQ, ID, TITLE, CONTENT, MDATE, REGDATE FROM CALBOARD WHERE ID = ? AND SUBSTR(MDATE,1,8) = ? ";
		ResultSet rs = null;
		PreparedStatement pstm = null;
		
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, id);
			pstm.setString(2, yyyyMMdd);
			
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				CalDto dto = new CalDto();
				dto.setSeq(rs.getInt(1));
				dto.setId(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setMdate(rs.getString(5));
				dto.setRegdate(rs.getDate(6));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstm);
			close(con);
		}

		return list;
	}
	
	public CalDto selectOne(int seq) {
		Connection con = getConnection();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		CalDto dto = null;
		String sql = " SELECT SEQ, ID, TITLE, CONTENT, MDATE, REGDATE FROM CALBOARD WHERE SEQ = ? ";
		
		try {
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, seq);
			
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				dto = new CalDto();
				dto.setSeq(rs.getInt(1));
				dto.setId(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setMdate(rs.getString(5));
				dto.setRegdate(rs.getDate(6));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstm);
			close(con);
		}
		return dto;
	}
	
	public int insert(CalDto dto) {
		Connection con = getConnection();
		PreparedStatement pstm = null;
		String sql = " INSERT INTO CALBOARD VALUES (CALBOARDSEQ.NEXTVAL, ?, ?, ?, ?, SYSDATE) ";
		int res = 0;
		
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, dto.getId());
			pstm.setString(2, dto.getTitle());
			pstm.setString(3, dto.getContent());
			pstm.setString(4, dto.getMdate());
			
			res = pstm.executeUpdate();
			
			if(res > 0) {
				con.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(pstm);
			close(con);
		}
		
		return res;
	}
	
	public int update(CalDto dto) {
		Connection con = getConnection();
		int res = 0;
		String sql = " UPDATE CALBOARD SET TITLE = ?, CONTENT = ? WHERE SEQ = ? ";
		PreparedStatement pstm = null;
		
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, dto.getTitle());
			pstm.setString(2, dto.getContent());
			pstm.setInt(3, dto.getSeq());
			
			res = pstm.executeUpdate();
			
			if(res > 0) {
				con.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(pstm);
			close(con);
		}
		
		return res;
	}
	
	public int delete(int seq) {
		Connection con = getConnection();
		PreparedStatement pstm = null;
		String sql = " DELETE FROM CALBOARD WHERE SEQ = ? ";
		int res = 0;
		try {
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, seq);
			
			res = pstm.executeUpdate();
			
			if(res > 0) {
				con.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			close(pstm);
			close(con);
		}
		
		return res;
	}

	// 여러번 삭제
	// 반복문으로 이용한다. <- connection연결도 for문에서 돌아가는동안 계속 반복해 예외가 발생한다.
	public int multiDelete(String[] seq) {
		Connection con = getConnection();
		PreparedStatement pstm = null;
		int res = 0;
		String sql = " DELETE FROM MDBOARD WHERE SEQ = ? ";
		int[] cnt = null; // 결과 값들을 저장할 CNT

		try {
			pstm = con.prepareStatement(sql);
			for (int i = 0; i < seq.length; i++) {
				pstm.setString(1, seq[i]);

				// addBatch() : 우선 메모리에 적재함
				// executeBatch() : 메서드가 호출될 때 한 번에 실행
				pstm.addBatch();
				System.out.println("삭제할 번호 : " + seq[i]);
			}
			System.out.println("3.query 준비 : " + sql);

			// 결과가 int형 배열로 리턴(성공 : -2 / 실패 : -3)
			cnt = pstm.executeBatch();
			System.out.println("4. 실행 및 리턴");

			for (int i = 0; i < cnt.length; i++) {
				// 삭제를 성공했을때 성공한 갯수를 쓰자
				// cnt는 cnt[]이다.
				// res는 UPDATE, INSERT, DELETE 해서 나온 row의 숫자를 뜻하는 것을 저장하는 곳이다.
				// 그래서 cnt[]은 res와 다르게 i번째가 계속 들어올때 체크를 확인하고 체크되어있으면
				// 카운트를 해준다
				if (cnt[i] == -2) {
					res++;
				}
			}

			// 삭제해준 갯수와 내가 지정해준 값이랑 맞으면 commit 해줄려고 이 구문을 쓴다.
			if (res == seq.length) {
				commit(con);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstm);
			close(con);
		}

		return res;
	}
	
	// RM 쿼리스트링을 만들어서 달력화면에 저장된 달력 일정을 1,2,3순위로 조금만하게 보이게하는 화면을 만드는 메소드
	public List<CalDto> getCalViewList(String id, String yyyyMM){
		Connection con = getConnection();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		CalDto dto = null;
		
		List<CalDto> list = new ArrayList<CalDto>();
		
		String sql = " SELECT SEQ, ID, TITLE, CONTENT, MDATE, REGDATE " + 
					 " FROM " + 
					 "	 (SELECT (ROW_NUMBER() OVER(PARTITION BY SUBSTR(MDATE,1,8) ORDER BY MDATE))RN, " + 
					 "		 SEQ, ID, TITLE, CONTENT, MDATE, REGDATE " + 
					 "	 FROM CALBOARD " + 
					 "	 WHERE ID = ? AND SUBSTR(MDATE,1,6)= ? " + 
					 "	 ) " + 
					 " WHERE RN BETWEEN 1 AND 3 " + 
					 " ORDER BY MDATE ";
		
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, id);
			pstm.setString(2, yyyyMM);
			
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				dto = new CalDto();
				dto.setSeq(rs.getInt(1));
				dto.setId(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setMdate(rs.getString(5));
				dto.setRegdate(rs.getDate(6));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstm);
			close(con);
		}
		
		return list;
	}
	
	public int getCalViewCount(String id, String yyyyMMdd) {
		
		Connection con = getConnection();
		PreparedStatement pstm = null;
		// 테이블로 값이 나와서 rs이다.
		ResultSet rs = null;
		int count = 0;
		
		String sql = " SELECT COUNT(*) FROM CALBOARD WHERE ID = ? AND SUBSTR(MDATE,1,8) = ? ";
		
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, id);
			pstm.setString(2, yyyyMMdd);
			
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstm);
			close(con);
		}
		
		
		return count;
	}
}
