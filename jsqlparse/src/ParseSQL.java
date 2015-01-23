
import java.io.StringReader;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.*;

public class ParseSQL {

	public static void main(String[] args)throws JSQLParserException {
		// TODO Auto-generated method stub
		String query = "Select * from moodle as m,highland as h,jurassic as h where moodle.id=highland.id and jurassic.class=moodle.class";
		CCJSqlParserManager pm = new CCJSqlParserManager(); 
		Statement stmt = pm.parse(new StringReader(query));
		if (stmt instanceof Select) {
			Select selectStatement = (Select) stmt;
			PlainSelect ps = (PlainSelect)selectStatement.getSelectBody();
			List<SelectItem> l = ps.getSelectItems();
			//for ()
			System.out.println(ps.getFromItem().toString());
			List<Join> j = ps.getJoins();
			for (int i=0; i<j.size(); i++){
				Join temp = (Join)j.get(i);
				System.out.println(temp.getRightItem());
			}

			TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
			List tableList = tablesNamesFinder.getTableList(selectStatement);
			for (Iterator iter = tableList.iterator(); iter.hasNext();) {
				String tableName = iter.next().toString();
				System.out.println(tableName);
			}
			List tableAliasList = tablesNamesFinder.getTableAlias(selectStatement);
			for (Iterator iter = tableAliasList.iterator(); iter.hasNext();) {
				String tableName = iter.next().toString();
				System.out.println(tableName);
			}
		}

	}

}
