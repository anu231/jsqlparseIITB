

import java.io.StringReader;
import java.util.*;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.Select;

public class Parsing {
	public static void main(String[] args){
		CCJSqlParserManager pm = new CCJSqlParserManager();
		String sql = "SELECT * FROM MY_TABLE1, MY_TABLE2, (SELECT * FROM MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 "+
		" WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT * FROM MY_TABLE6)" ;
		net.sf.jsqlparser.statement.Statement statement=null;
		try {
			statement = pm.parse(new StringReader(sql));
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* 
		now you should use a class that implements StatementVisitor to decide what to do
		based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
		interested in SELECTS
		*/
		if (statement instanceof Select) {
			Select selectStatement = (Select) statement;
			TablesNameFinder tablesNamesFinder = new TablesNameFinder();
			List tableList = tablesNamesFinder.getTableList(selectStatement);
			for (Iterator iter = tableList.iterator(); iter.hasNext();) {
				String tableName = iter.next().toString();
				System.out.println(tableName);
			}
		}
	}
}
