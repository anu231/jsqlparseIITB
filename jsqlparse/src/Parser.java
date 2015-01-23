//package dbridge.analysis.region.eedag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import dbridge.analysis.region.eedag.operator.Operator;
//import soot.Value;
//import soot.jimple.StringConstant;
import net.sf.ehcache.transaction.manager.selector.Selector;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class Parser {

    public List<String> ProjectionCols;
    public List<String> Tables;
    public List<BinaryExpWhere> SelectionCond;
    public Operator OP;
    public Parser() {
    	ProjectionCols = new ArrayList<String>();
    	Tables = new ArrayList<String>();
    	SelectionCond = new ArrayList<BinaryExpWhere>();
    }
    
    public void queryParse(String query)
    {
    	Statement statement = null;
    	try {
    
    		statement = CCJSqlParserUtil.parse(query.substring(1, query.length()-1));
    		//statement = CCJSqlParserUtil.parse("SELECT a,b FROM MY_TABLE1");
    	} catch (JSQLParserException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    
    	Select selectStatement = (Select) statement;    
    	PlainSelect ps = new PlainSelect();
        
    	//get the statement(select)
    	ps = (PlainSelect)selectStatement.getSelectBody();
    
        //get projection list
        List<SelectItem> l = ps.getSelectItems();
    
    	for (Iterator iter1 = l.iterator(); iter1.hasNext();) {
    		//System.out.println(iter1.next().toString());
    
    		ProjectionCols.add(iter1.next().toString());
    	}
    
        // get the list of tables
    	TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    	Tables = tablesNamesFinder.getTableList(selectStatement);
    
    	/*
    	for (Iterator iter = Tables.iterator(); iter.hasNext();) {
    		System.out.println(iter.next().toString());
    	}*/
    
    	// get the where conditions/expressions assuming only AND
    	Expression ex = ps.getWhere(); 
    
        BinaryExpression be = null;
        be = (BinaryExpression) ex;
        
    	BinaryExpression right = null;
    	BinaryExpression left = be;
    	Value leftop=null;
    	Value rightop=null;
    
    
    	BinaryExpWhere exp=null;
    	if(left!=null)
    	{
    	while(left.getStringExpression() == "AND")
    	{
    
    		right = (BinaryExpression)left.getRightExpression();
    		//System.out.println(right.getLeftExpression().toString()+"--"+right.getStringExpression().toString()+"--"+right.getRightExpression().toString());

        	leftop=(Value)StringConstant.v(right.getLeftExpression().toString());
        	rightop=(Value)StringConstant.v(right.getRightExpression().toString());
        	Set_OP(right.getStringExpression().toString());
        	exp = new BinaryExpWhere(OP,leftop,rightop,(Value)StringConstant.v(right.toString()));
    		SelectionCond.add(0,exp);
    
    		left =  (BinaryExpression)left.getLeftExpression();
    		//System.out.println(left.toString());
    		//System.out.println(right.toString());
    		//System.out.println("--------------");
    	}
    
    	leftop=(Value)StringConstant.v(left.getLeftExpression().toString());
    	rightop=(Value)StringConstant.v(left.getRightExpression().toString());
    	Set_OP(left.getStringExpression().toString());
    	exp = new BinaryExpWhere(OP,leftop,rightop,(Value)StringConstant.v(left.toString()));
    	//System.out.println(left.getLeftExpression().toString());
    	//System.out.println(left.getRightExpression().toString());
    	//System.out.println(left.getStringExpression().toString());
    	SelectionCond.add(0, exp);
    	//TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    	//List tableList = tablesNamesFinder.getTableList(selectStatement);
    
    	}
    }
    public void Set_OP (String v)
    {
    
    	if(v==">")
    		this.OP=Operator.Gt;
    	else if(v=="<")
    		this.OP=Operator.Lt;
    	else if(v=="=")
    		this.OP=Operator.Eq;
    	else if(v==">=")
    		this.OP=Operator.GtEq;
    	else if(v=="<=")
    		this.OP=Operator.LtEq;
    	else if(v=="<>")
    		this.OP=Operator.NotEq;
    }
    /*
	public static void main(String args[])
	{
	Statement statement = null;
	try {
		statement = CCJSqlParserUtil.parse("SELECT a,b FROM MY_TABLE1,Table2 where marks>10 and id=10 or lf=0 GROUP BY a order by b");
	} catch (JSQLParserException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	Select selectStatement = (Select) statement;
	SelectItemVisitor st = null;

	PlainSelect ps = new PlainSelect();
    ps = (PlainSelect)selectStatement.getSelectBody();
	List<SelectItem> l = ps.getSelectItems();

	for (Iterator iter1 = l.iterator(); iter1.hasNext();) {
		System.out.println(iter1.next().toString());
	}
    Expression ex = ps.getWhere();
    BinaryExpression be = null;
    be = (BinaryExpression) ex;
    
    System.out.println(be.isNot());
    System.out.println(be.toString());
    System.out.println(((BinaryExpression)be.getLeftExpression()).getLeftExpression());
    System.out.println(((BinaryExpression)be.getRightExpression()).getStringExpression());
    System.out.println(be.getStringExpression());
		System.out.println(ex.toString());
		BinaryExpression right = null;
		BinaryExpression left = null;
		left = be;
	while(left.getStringExpression() == "AND" || left.getStringExpression() == "OR")
	{
		System.out.println(left.toString());
		right = (BinaryExpression)left.getRightExpression();
		System.out.println(right.getLeftExpression().toString()+"--"+right.getStringExpression().toString()+"--"+right.getRightExpression().toString());
		left =  (BinaryExpression)left.getLeftExpression();
		System.out.println(left.toString());
		System.out.println(right.toString());
		System.out.println("--------------");
	}

	//TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
	//List tableList = tablesNamesFinder.getTableList(selectStatement);
	TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
	List<String> tableList = tablesNamesFinder.getTableList(selectStatement);


	for (Iterator iter = tableList.iterator(); iter.hasNext();) {
		System.out.println(iter.next().toString());
	}
}*/
}
