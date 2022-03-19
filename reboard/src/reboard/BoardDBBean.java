// DAO(Data Access Object)

package reboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
	
	
	// 데이터 목록 : 데이터 10개 추출
	public List<BoardDataBean> getList(int start, int end) {
		List<BoardDataBean> list = new ArrayList<BoardDataBean>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = getConnection();
			
			String sql = "select * from (select rownum rnum, board.* from ";
				   sql += " (select * from board order by ref desc, re_step asc) board )";
				   sql += " where rnum >=? and rnum <=?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			rs = pstmt.executeQuery();	// SQL문 실행
			
			while(rs.next()) {
				BoardDataBean board = new BoardDataBean();
				board.setNum(rs.getInt("num"));
				board.setWriter(rs.getString("writer"));
				board.setEmail(rs.getString("email"));
				board.setSubject(rs.getString("subject"));
				board.setPasswd(rs.getString("passwd"));
				board.setReg_date(rs.getTimestamp("reg_date"));
				board.setReadcount(rs.getInt("readcount"));
				board.setRef(rs.getInt("ref"));
				board.setRe_level(rs.getInt("re_level"));
				board.setRe_step(rs.getInt("re_step"));
				board.setContent(rs.getString("content"));
				board.setIp(rs.getString("ip"));
				
				list.add(board);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();}catch (Exception e) {}
			if(pstmt!=null)try {pstmt.close();}catch (Exception e) {}
			if(con!=null)try {con.close();}catch (Exception e) {}
		}
		return list;
	}
	
	
	// 상세 페이지 : 조회수를 1증가 시키고 상세정보 구하기
	public BoardDataBean updateContent(int num) {
		BoardDataBean board = new BoardDataBean();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = getConnection();
			
			String sql ="update board set readcount=readcount+1 where num=?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();	// SQL문 실행
			
			sql = "select * from board where num=?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();	// SQL문 실행
			
			if(rs.next()) {
				board.setNum(rs.getInt("num"));
				board.setWriter(rs.getString("writer"));
				board.setEmail(rs.getString("email"));
				board.setSubject(rs.getString("subject"));
				board.setPasswd(rs.getString("passwd"));
				board.setReg_date(rs.getTimestamp("reg_date"));
				board.setReadcount(rs.getInt("readcount"));
				board.setRef(rs.getInt("ref"));
				board.setRe_level(rs.getInt("re_level"));
				board.setRe_step(rs.getInt("re_step"));
				board.setContent(rs.getString("content"));
				board.setIp(rs.getString("ip"));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();}catch (Exception e) {}
			if(pstmt!=null)try {pstmt.close();}catch (Exception e) {}
			if(con!=null)try {con.close();}catch (Exception e) {}
		}
		
		return board;
	}
	
	
	// 댓글 작성
	public int reply(BoardDataBean board) {
		int result = 0;
		
		// 부모글에 대한 정보
		int ref = board.getRef();
		int re_step = board.getRe_step();
		int re_level = board.getRe_level();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = getConnection();
			
			// 원문이 부모인 경우 : 원문의 re_step=0이기 때문에, 모든 댓글들의 re_step값이 1증가
			// 댓글이 부모인 경우 : 부모의 re_step보다 큰 댓글만, re_step값이 1증가
			
			String sql = "update board set re_step=re_step+1 ";
				   sql += " where ref=? and re_step > ?";
				   
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1,ref);	// 부모의 ref
			pstmt.setInt(2, re_step);	// 부모의 re_step
			pstmt.executeUpdate();	// SQL문 실행
			
			sql = "insert into board values(board_seq.nextval,?,?,?,?,";
			sql += "sysdate,?,?,?,?,?,?)";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, board.getWriter());
			pstmt.setString(2, board.getEmail());
			pstmt.setString(3, board.getSubject());
			pstmt.setString(4, board.getPasswd());
			pstmt.setInt(5, 0);		// readcount
			pstmt.setInt(6, ref);	// ref
			pstmt.setInt(7, re_step+1);		// re_step
			pstmt.setInt(8, re_level+1);		// re_level
			pstmt.setString(9, board.getContent());
			pstmt.setString(10, board.getIp());
			
			result = pstmt.executeUpdate();	// SQL문 실행
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null)try {pstmt.close();}catch (Exception e) {}
			if(con!=null)try {con.close();}catch (Exception e) {}
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
}
