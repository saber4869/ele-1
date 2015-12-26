package distance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Translate {
	Connection connection;
	Statement statement;
	
	int majorWeight = 10000;
	int jobWeight = 1;
	int schoolWeight = 10000;
	int ageWeight = 1000;
	int backgroundWeight = 1;
	int companyWeight = 10000;
	
	public Translate() throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
	    connection = DriverManager.getConnection("jdbc:mysql://localhost/project", "root", "");
		statement = connection.createStatement();
	}
	public  int majorTranslate(String major) throws SQLException{
		ResultSet resultSet = statement.executeQuery("Select major_name from major_name where major_name = '" + major + "'");
		if(resultSet.next()){
			return Integer.parseInt("0") * majorWeight;
		}
		return 1*majorWeight;
	}
	
	public int ageTranslate(String birthday) throws SQLException{
		//姝ｅ垯琛ㄨ揪寮�,灏嗗嚭鐢熸棩鏈熸洿鏀逛负骞撮緞
		int age = 0;
		String regEx="([0-9]+)-[0-9]+-[0-9]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(birthday);
        if(m.find()){ 
        	age = 2015 - Integer.parseInt(m.group(1));
        }
		return age*ageWeight;
	}
	
	public int jobTranslate(String jo) throws SQLException{
		if(jo == null){
			System.out.println(jo);
			return 0;
		}
		ResultSet resultSet = statement.executeQuery("Select vocation_id from job where job_name like '%"+ jo +"%'");
		if(resultSet.next()){
			String joid;
			joid = resultSet.getString(1);
			int len = joid.length();
			return (int) (Integer.parseInt(joid) * Math.pow(10, 8-len)) * jobWeight;
		}
		resultSet = statement.executeQuery("Select job_class_id, job_class_name from job_class");
		while(resultSet.next()){
			if(jo.indexOf(resultSet.getString(2)) >= 0){
				String joid = resultSet.getString(1);
				int len = joid.length();
				return (int) ((Integer.parseInt(resultSet.getString(1)) * Math.pow(10, 8-len) * jobWeight));
			}
		}
		return 0;
	}
	
	public  int  backgroundTranslate(String backgroundName) throws SQLException, ClassNotFoundException{

		ResultSet resultSet = statement.executeQuery("SELECT background_id FROM background where background_name = '" + backgroundName + "' or background_name like '%" + backgroundName + "%'");
		if(resultSet.next()){
			int result = resultSet.getInt(1);
			return result;
		}
		return 80000;  //璁剧疆瀛﹀巻鏉冮噸
	} 
	
	
	public  int  companyTranslate(String companyName) throws SQLException, ClassNotFoundException{

		if(companyName == null || companyName == "null") return 4*companyWeight;
		ResultSet resultSet = statement.executeQuery("SELECT rank FROM company_info where name = '" + companyName + "' or name like '%" + companyName + "%'" + " or licence_name like '%" + companyName + "%'");
		if(resultSet.next()){
			String result = resultSet.getString(1);
			if(result == "Top500") return 1*companyWeight;
			if(result == "澶у瀷") return 2*companyWeight;
			if(result == "涓瀷") return 3*companyWeight;
			if(result == "灏忓瀷") return 4*companyWeight;
			else return 4*companyWeight;
		}
		return 4*companyWeight;
	}

	
	
	
	public  int  schoolTranslate(String schoolName) throws SQLException, ClassNotFoundException{

		ResultSet resultSet = statement.executeQuery("SELECT class_id FROM school_info where school_name = '" + schoolName + "' or school_alias like '%" + schoolName + "%'");
		if(resultSet.next()){
			String result = resultSet.getString(1);
			return Integer.parseInt(result.split(";")[0]) * schoolWeight;
		}
		return 6*schoolWeight;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException{
		Translate translate = new Translate();
		Class.forName("com.mysql.jdbc.Driver");
	    Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/resume", "root", "");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("Select * from information2 limit 1000");
		int i=1;
		//鍒嗗埆璇诲彇123position
		FileWriter filewriter = new FileWriter("C:\\result_.arff");
		filewriter.write("@relation model\n");
		filewriter.write("@attribute 'school' numeric\n@attribute 'position' numeric\n@attribute 'major' numeric\n@attribute 'id' numeric\n@data\n");
		
		
		while (resultSet.next()) {
			int jobnum = 0;		
			int a = translate.jobTranslate(resultSet.getString("jobexp1_position"));
			int b = translate.jobTranslate(resultSet.getString("jobexp2_position"));
			int c = translate.jobTranslate(resultSet.getString("jobexp3_position"));
			int n = 0;
			if(a != 0)
				n++;
			if(b != 0)
				n++;
			if(c != 0)
				n++;
			jobnum = n==0?0:(a+b+c)/n;
			
			System.out.println(1);
			int schnum = translate.schoolTranslate(resultSet.getString("school_1"));
			int majnum = translate.majorTranslate(resultSet.getString("major_1"));
			int agenum = translate.ageTranslate(resultSet.getString("birthdate"));
			int backgroundnum = translate.backgroundTranslate(resultSet.getString("degree_1"));
			int companynum = translate.companyTranslate(resultSet.getString("jobExp1_companyName"));
			String id = resultSet.getString("ID");
			
			System.out.println(2);
			//鎺掗櫎鎺夊お娓ｇ殑浜�
			int score = schnum + majnum + backgroundnum + companynum;
			if(score > 230000){    //璇存槑杩欎釜浜哄お娓ｄ簡,涓嶈浠栫殑鏁版嵁
				continue;
			}
			System.out.println(3);
			if(schnum * jobnum == 0) continue;
			System.out.println(4);
			filewriter.write(schnum + " " + jobnum + " " + majnum + " " + agenum + " " + backgroundnum  + " " + id + "\n");
		}
		filewriter.close();
	}
	
}
