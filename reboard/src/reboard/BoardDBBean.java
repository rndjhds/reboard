// DAO(Data Access Object)

package reboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDBBean {

	// 싱글톤 : 객체 생성을 1번만 수행
	private static BoardDBBean instance = new BoardDBBean();
	
	public static BoardDBBean getInstance() {	// 정적 메소드
		return instance;
	}
	
	// Connection Pool에서 컨넥션을 구해오는 메소드
	private Connection getConnection() throws Exception{
		Context init = new InitialContext();
  		DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/orcl");
  		return ds.getConnection();
	}
	
	
	// 원문 글작성
	public int insert(BoardDataBean board) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = getConnection();
			
			String sql = "insert into board values(board_seq.nextval,?,?,?,?,";
				   sql += "sysdate,?,board_seq.nextval,?,?,?,?)";
				   
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, board.getWriter());
			pstmt.setString(2, board.getEmail());
			pstmt.setString(3, board.getSubject());
			pstmt.setString(4, board.getPasswd());
			pstmt.setInt(5, 0);	// readcount
			pstmt.setInt(6, 0);	// re_step
			pstmt.setInt(7, 0);	// re_level
//			pstmt.setInt(5, board.getReadcount());
//			pstmt.setInt(6, board.getRe_step());
//			pstmt.setInt(7, board.getRe_level());
			pstmt.setString(8, board.getContent());
			pstmt.setString(9, board.getIp());
			
			result = pstmt.executeUpdate();	// SQL문 실행
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null)try {pstmt.close();}catch (Exception e) {}
			if(con!=null)try {con.close();}catch (Exception e) {}
		}
		
		
		return result;
	}
	
	
	// 총 데이터 갯수 구하기
	public int getContent() {
		int result = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = getConnection();
			
			String sql = "select count(*) from board";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();	// SQL문 실행
			
			if(rs.next()) {
				result = rs.getInt(1);
//				result = rs.getInt("count(*)");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();}catch (Exception e) {}
			if(pstmt!=null)try {pstmt.close();}catch (Exception e) {}
			if(con!=null)try {con.close();}catch (Exception e) {}
		}
				
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
