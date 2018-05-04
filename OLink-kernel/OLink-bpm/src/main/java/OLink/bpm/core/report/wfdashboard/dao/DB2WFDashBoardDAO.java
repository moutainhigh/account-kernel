package OLink.bpm.core.report.wfdashboard.dao;

import java.sql.Connection;
import java.util.Collection;

import OLink.bpm.base.dao.DataPackage;
import OLink.bpm.core.report.wfdashboard.ejb.DashBoardVO;
import OLink.bpm.util.DbTypeUtil;
import OLink.bpm.util.StringUtil;

public class DB2WFDashBoardDAO extends AbstractWFDashBoardDAO implements WFDashBoardDAO{

	public DB2WFDashBoardDAO(Connection conn) throws Exception {
		super(conn);
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_DB2);
	}
	
	public DataPackage<DashBoardVO> getSumRole(String application, String domainid,
											   String flowid, int curPage) throws Exception {
		String sql = "SELECT A.ACTORID NAME,COUNT(A.ACTORID) VALUE  FROM "
				+ this.getFullTableName("T_ACTORRT") + " A LEFT JOIN "
				+ this.getFullTableName("T_FLOWSTATERT")
				+ " B ON(A.FLOWSTATERT_ID = B.ID AND B.STATE=256)";
		
		   sql += " WHERE A.DOMAINID='"+domainid+"' AND A.APPLICATIONID='"+application+"'";

		if (!StringUtil.isBlank(flowid))
			sql += " AND B.FLOWID ='" + flowid + "'";

		sql += " GROUP  BY A.ACTORID ";

 		int startLine = (curPage- 1)* 10;
		int endLine = startLine + 10;
		
		DataPackage<DashBoardVO> dpg = new DataPackage<DashBoardVO>();
		dpg.rowCount = countSql(sql);
		
		sql = "SELECT NAME,VALUE FROM (SELECT NAME,VALUE,ROW_NUMBER() OVER() AS ROW_ FROM (" + sql + ") A ORDER BY NAME) B  where   ROW_>"+ startLine
		     +"   AND   ROW_<="+endLine;
		
		dpg.setDatas(getDatas(sql));
  
		dpg.linesPerPage = 10;
		dpg.pageNo = curPage;
		return dpg;
	}
	
	public Collection<DashBoardVO> getSumTimeData(String application, String domainid,
			String flowid) throws Exception {
		String usedTimeCol = "(DAYS(PROCESSTIME) - DAYS(ACTIONTIME)) * 86400+ (MIDNIGHT_SECONDS(PROCESSTIME) - MIDNIGHT_SECONDS(ACTIONTIME))  ";
		String sql = "SELECT B.STATELABEL NAME,ROUND((CAST(AVG("+usedTimeCol+") AS FLOAT)/60)/60,2) VALUE FROM " 
				//"ROUND(AVG(TIMESTAMPDIFF(MINUTE,ACTIONTIME,PROCESSTIME)/60),2) VALUE FROM "
				+ this.getFullTableName("T_RELATIONHIS")
				+ " A,"
				+ "(SELECT A.FLOWID,A.DOCID,B.STATELABEL,B.DOMAINID,B.APPLICATIONID FROM "
				+ getFullTableName("T_FLOWSTATERT")
				+ " A,"
				+ getFullTableName("T_DOCUMENT")
				+ " B WHERE A.STATE =256 AND A.DOCID = B.ID) B "
				+ " WHERE A.DOCID = B.DOCID ";
		 sql += " AND B.DOMAINID='"+domainid+"' AND B.APPLICATIONID='"+application+"'";
		
		

		if (!StringUtil.isBlank(flowid))
			sql += " AND B.FLOWID ='" + flowid + "'";

		sql += " GROUP BY B.STATELABEL";

		return getDatas(sql);
	}

}
