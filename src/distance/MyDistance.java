package distance;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyDistance {
	
	static String ans = "";
	private static String[][] result_array = new String[20][7];
	
	public String interaction(String background, String age, String school, String major, String position) throws ClassNotFoundException, SQLException, IOException{
		ans = "";
		
		Translate translate = new Translate();
		BufferedReader reader = null;
		FileInputStream fileInputStream = new FileInputStream(
				"c:\\result_.arff");
		InputStreamReader inputStreamReader = new InputStreamReader(
				fileInputStream, "ASCII");
		reader = new BufferedReader(inputStreamReader);

		double[][][] Distance = new double[500][500][4];
		int[][] cord = new int[500][6];
		String data = reader.readLine();
		Scanner input = new Scanner(System.in);


		int i = 0;
		for (i = 0; data != null; data = reader.readLine(), i++) {
			for (int j = 0; j < 6; j++) {            //濡傛灉闇�瑕佹坊鍔犲瓧娈电殑璇�,闇�瑕佹妸j鍙樺ぇ
				cord[i][j] = Integer.parseInt(data.split(" ")[j]);
			}
		}
		System.out.print("璇疯緭鍏ュ鏍″悕绉帮細");
		cord[0][0] =translate.schoolTranslate(school);
		System.out.print("璇疯緭鍏ヤ笓涓氬悕绉帮細");
		cord[0][2] =translate.majorTranslate(major);
		System.out.print("璇疯緭鍏ヨ亴浣嶅悕绉帮細");
		cord[0][1] =translate.jobTranslate(position);
		//姝や汉鐨勫勾榫�
		cord[0][3] = Integer.parseInt(age) * 10000;
		//姝や汉鐨勫鍘�
		cord[0][4] = translate.backgroundTranslate(background);
		
		//System.out.print("璇疯緭鍏ュ勾榫�:");
		//cord[0][3] = Integer.parseInt(arg0)
		
		//cord[0][0] = 10000;
		//cord[0][1] = 1010000;
		//cord[0][2] = 10000;
		cord[0][5] = 0;
		for (int n = 0; n < i; n++) {
			for (int k = 0; k < i; k++) {
				double d = 0;
				for (int j = 0; j < 5; j++) {            //濡傛灉鍔犲瓧娈电壒寰佸��,闇�瑕佹敼杩欓噷,鎶奐鍙樺ぇ鍗冲彲
					double m = Math.pow(Math.abs((double)(cord[0][j] - cord[k][j]))/10000,2);		
					d += m;
				}
				Distance[n][k][0] = cord[0][5];
				Distance[n][k][1] = cord[k][5];
				Distance[n][k][2] = d;
			}
		}
		double[] index = new double[37];
		int[] ID = new int[37];
		FileWriter out = new FileWriter("D:/distance2.txt");
		int n = 0;
		int count = 0;
		for(int k = 0; k < i; k++){
				DecimalFormat df = new DecimalFormat("######0");   
				String w = "鐐笽D: "+df.format(Distance[n][k][0])+" 娴嬭瘯鐐笽D: "+df.format(Distance[n][k][1])+" 璺濈鐨勫钩鏂�: "+Distance[n][k][2]+'\n';
				out.write(w);
				w = "    鐐规暟鎹�: "+cord[0][0]+" "+cord[0][1]+" "+cord[0][2]+" "+cord[0][3] +" "+cord[0][4] +" "+cord[0][5] + '\n';
				out.write(w);
				w = "娴嬭瘯鐐规暟鎹�: "+cord[k][0]+" "+cord[k][1]+" "+cord[k][2]+" "+cord[k][3]+ " "+cord[k][4] + " " +cord[k][5] + '\n';
				index[count] = Distance[n][k][2];
				ID[count] = (int)Distance[n][k][1];
				count++;
				out.write(w);
		}
		System.out.println(i);
        //ans += i;

		System.out.println(index[3] + " ");
		
        //鐩存帴鎻掑叆鎺掑簭
        for (int i1 = 1; i1 < index.length; i1++) {
            //寰呮彃鍏ュ厓绱�
            double temp = index[i1];
            int j;
            int temp2 = ID[i1];
            for (j = i1-1; j>=0; j--) {
                //灏嗗ぇ浜巘emp鐨勫線鍚庣Щ鍔ㄤ竴浣�
                if(index[j]>temp){
                    index[j+1] = index[j];
                    ID[j+1] = ID[j];
                }else{
                    break;
                }
            }
            index[j+1] = temp;
            ID[j+1] = temp2;
        }
        
        System.out.println(index[3] + " ");
        
        int[] ID100 = new int[37];
        for(int i1 = 0; i1 < 37; i1++){
        	ID100[i1] = ID[i1];
        }
        System.out.println();
        System.out.println("鎺掑簭涔嬪悗锛�");
        ans += ("鎺掑簭涔嬪悗锛�" + "\n");
        for (int i1 = 0; i1 < 20; i1++) {
            System.out.print("璺濈锛�"+index[i1]+" ID锛�"+ID100[i1]+"\n");
            ans += ("璺濈锛�"+index[i1]+" ID锛�"+ID100[i1]+"\n");
        }
        showDetail(ID100);
        groupBySalary(ID100);
		return ans;
	}
	
	class Ans implements Comparable{
		public double distance;
		public int id;
		public int compareTo(Object b) {
			Ans bb = (Ans) b;
			if (this.distance < bb.distance)
				return -1;
			if (this.distance == bb.distance)
				return 0;
			else return 1;
		}
	}
	
	public static void main(String[] arg) throws IOException, ClassNotFoundException, SQLException {
		
		Translate translate = new Translate();
		BufferedReader reader = null;
		FileInputStream fileInputStream = new FileInputStream(
				"c:\\result_.arff");
		InputStreamReader inputStreamReader = new InputStreamReader(
				fileInputStream, "ASCII");
		reader = new BufferedReader(inputStreamReader);

		double[][][] Distance = new double[500][500][4];
		int[][] cord = new int[500][4];
		String data = reader.readLine();
		Scanner input = new Scanner(System.in);


		int i = 0;
		for (i = 0; data != null; data = reader.readLine(), i++) {
			for (int j = 0; j < 4; j++) {
				cord[i][j] = Integer.parseInt(data.split(" ")[j]);
			}
		}
//		System.out.print("璇疯緭鍏ュ鏍″悕绉帮細");
//		cord[0][0] =translate.schoolTranslate(input.next());
//		System.out.print("璇疯緭鍏ヤ笓涓氬悕绉帮細");
//		cord[0][2] =translate.majorTranslate(input.next());
//		System.out.print("璇疯緭鍏ヨ亴浣嶅悕绉帮細");
//		cord[0][1] =translate.jobTranslate(input.next());
		cord[0][0] = 10000;
		cord[0][1] = 1010000;
		cord[0][2] = 10000;
		//cord[0][3] = 0;
		for (int n = 0; n < i; n++) {
			for (int k = 0; k < i; k++) {
				double d = 0;
				for (int j = 0; j < 3; j++) {
					double m = Math.pow(Math.abs((double)(cord[0][j] - cord[k][j]))/10000,2);		
					d += m;
				}
				Distance[n][k][0] = cord[0][3];
				Distance[n][k][1] = cord[k][3];
				Distance[n][k][2] = d;
			}
		}
		double[] index = new double[37];
		int[] ID = new int[37];
		FileWriter out = new FileWriter("D:/distance2.txt");
		int n = 0;
		int count = 0;
		for(int k = 0; k < i; k++){
				DecimalFormat df = new DecimalFormat("######0");   
				String w = "鐐笽D: "+df.format(Distance[n][k][0])+" 娴嬭瘯鐐笽D: "+df.format(Distance[n][k][1])+" 璺濈鐨勫钩鏂�: "+Distance[n][k][2]+'\n';
				out.write(w);
				w = "    鐐规暟鎹�: "+cord[0][0]+" "+cord[0][1]+" "+cord[0][2]+" "+cord[0][3]+'\n';
				out.write(w);
				w = "娴嬭瘯鐐规暟鎹�: "+cord[k][0]+" "+cord[k][1]+" "+cord[k][2]+" "+cord[k][3]+'\n';
				index[count] = Distance[n][k][2];
				ID[count] = (int)Distance[n][k][1];
				count++;
				out.write(w);
		}
		System.out.println(i);
        //ans += i;

        //鐩存帴鎻掑叆鎺掑簭
        for (int i1 = 1; i1 < index.length; i1++) {
            //寰呮彃鍏ュ厓绱�
            double temp = index[i1];
            int j;
            int temp2 = ID[i1];
            for (j = i1-1; j>=0; j--) {
                //灏嗗ぇ浜巘emp鐨勫線鍚庣Щ鍔ㄤ竴浣�
                if(index[j]>temp){
                    index[j+1] = index[j];
                    ID[j+1] = ID[j];
                }else{
                    break;
                }
            }
            index[j+1] = temp;
            ID[j+1] = temp2;
        }
        int[] ID100 = new int[37];
        for(int i1 = 0; i1 < 37; i1++){
        	ID100[i1] = ID[i1];
        }
        System.out.println();
        System.out.println("鎺掑簭涔嬪悗锛�");
        ans += ("鎺掑簭涔嬪悗锛�" + "\n");
        for (int i1 = 0; i1 < 20; i1++) {
            System.out.print("璺濈锛�"+index[i1]+" ID锛�"+ID100[i1]+"\n");
            ans += ("璺濈锛�"+index[i1]+" ID锛�"+ID100[i1]+"\n");
        }
        showDetail(ID100);
        groupBySalary(ID100);
    }
	  public static void showDetail(int[] ids) throws ClassNotFoundException, SQLException{
		  
			//mysql鏁版嵁搴撹繛鎺�
		  	int[] ids20 = new int[20];
		  	System.arraycopy(ids, 0, ids20, 0, 20);
			Class.forName("com.mysql.jdbc.Driver");
		    Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/resume", "root", "");
			Statement statement = connection.createStatement();
			
			for(int i=0; i< ids20.length; i++){
				ResultSet resultSet = statement.executeQuery("SELECT * FROM information2 WHERE ID = '" + ids20[i] + "'");
				String positionName = null;
				
				while (resultSet.next()) {
					String skills = getSkills(resultSet.getString("jobexp1_position"));
					System.out.println(resultSet.getString("degree_1") + resultSet.getString("school_1") + " " + resultSet.getString("major_1") + " " + resultSet.getString("jobexp1_position") + " " + resultSet.getString("jobexp1_companyname")+ " " + skills );
					ans += (resultSet.getString("degree_1") + " " + resultSet.getString("school_1") + " " + resultSet.getString("major_1") + " " + resultSet.getString("jobexp1_position") + " " + resultSet.getString("jobexp1_companyname")+ " " + skills+"\n");
				

					result_array[i][0] = resultSet.getString("degree_1");
					result_array[i][1] = resultSet.getString("school_1");
					result_array[i][2] = resultSet.getString("major_1");
					result_array[i][3] = resultSet.getString("jobexp1_position");
					result_array[i][4] = resultSet.getString("jobexp1_companyname");
					result_array[i][5] = skills;
					result_array[i][6] = resultSet.getString("projectName_1");
							
					
				}
			}
			//搴斿鐨勬妧鑳�
			//鍏堟妸posionName
			
		  }
	  public String[][] get_result_array(){
		return result_array;
		  
	  }
	  public static void showDetail(Vector ids) throws ClassNotFoundException, SQLException{
		  
			//mysql鏁版嵁搴撹繛鎺�
//		  	Vector ids20 = new Vector(20);
//		  	for(int i = 0; i < 20; i++){
//		  		ids20.add(ids.get(i));
//		  	}
		  
			Class.forName("com.mysql.jdbc.Driver");
		    Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/resume", "root", "");
			Statement statement = connection.createStatement();
			
			
			for(int i=0; i< ids.size(); i++){
				ResultSet resultSet = statement.executeQuery("SELECT * FROM information2 WHERE ID = '" + ids.get(i) + "'");
				String positionName = null;
				
				while (resultSet.next()) {
					String skills = getSkills(resultSet.getString("jobexp1_position"));
					System.out.println(resultSet.getString("degree_1") + resultSet.getString("school_1") + " " + resultSet.getString("major_1") + " " + resultSet.getString("jobexp1_position") + " " + resultSet.getString("jobexp1_companyname")+ " " + skills );
					ans += (resultSet.getString("degree_1") +  " " + resultSet.getString("school_1") + " " + resultSet.getString("major_1") + " " + resultSet.getString("jobexp1_position") + " " + resultSet.getString("jobexp1_companyname")+ " " + skills +"\n");
				}
			}
			//搴斿鐨勬妧鑳�
			//鍏堟妸posionName
		  }
	  
	  public static String getSkills(String position) throws SQLException, ClassNotFoundException{
			//mysql鏁版嵁搴撹繛鎺�
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/project", "root", "");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT skill FROM job WHERE job_name like '%" + position + "%'");
		if(resultSet.next()){
			return resultSet.getString(1);
		}
		ResultSet resultSet2 = statement.executeQuery("SELECT skilltb.name FROM job,job_class,skilltb WHERE job.vocation_id = job_class.job_class_id and job_class.job_class_name = skilltb.kind and job_class.job_class_name like '%" + position + "%'");
		String output = "";
		while(resultSet2.next()){
			output += resultSet2.getString(1) + ",";
		}
		return output;
	  }
	  
	  public static int parseSalaryFromStringToInt(String salaryStr){
		  		int salary = 0;
				if(salaryStr.contains("闈㈣")){
					salary = -1;
				}
				else{
	                String regEx="([0-9]+)-([0-9]+)";
	                Pattern p = Pattern.compile(regEx);
	                Matcher m = p.matcher(salaryStr);
	                if(m.find()){ 
	                	salary = (Integer.parseInt(m.group(1))+Integer.parseInt(m.group(1)))/2;
	                }
	                else{
	                	String regEx2="([0-9]+)";
		                Pattern p2 = Pattern.compile(regEx2);
		                Matcher m2 = p2.matcher(salaryStr);
		                if(m2.find())
		                	salary = Integer.parseInt(m2.group(1));
	                } 	
				}
                return salary;
	  }
	  
	  
	  public static void groupBySalary(int[] ids) throws SQLException, ClassNotFoundException{
		  	Class.forName("com.mysql.jdbc.Driver");
		    Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/resume", "root", "");
			Statement statement = connection.createStatement();
			int[] salary = new int[100];
			int[] salaryid = new int[500];
			int n=0;
			
			int[] ids20 = new int[20];
			System.arraycopy(ids, 0, ids20, 0, 20);
			
			for(int i=0; i< ids20.length; i++){
				ResultSet resultSet = statement.executeQuery("SELECT expectSalary FROM information2 WHERE ID = '" + ids20[i] + "'");
				
				while (resultSet.next()) {
					
					int salary_ =  parseSalaryFromStringToInt(resultSet.getString(1));
					if(salary_ < 0) continue;
					salary[n] = salary_;
					salaryid[n++] = ids20[i];
//	                } 	
				}
			}
			int min = 1000000, max = 0;
			for(int k=0; k<n; k++){
				if(min > salary[k]){
					min = salary[k];
				}
				if(max < salary[k]){
					max = salary[k];
				}
			}
			int[] level = new int[4];
			int stage = (max - min) / 4;
			level[0] = min + stage;
			level[1] = level[0] + stage;
			level[2] = level[1] + stage;
			level[3] = level[2] + stage;
			
			Vector v0 = new Vector(4);
			Vector v1 = new Vector(4);
			Vector v2 = new Vector(4);
			Vector v3 = new Vector(4);
			
			for(int k = 0; k < n; k++){
				if(salary[k] < level[0]){
					v0.add(salaryid[k]);
				}
				else if(salary[k] < level[1]){
					v1.add(salaryid[k]);
				}
				else if(salary[k] < level[2]){
					v2.add(salaryid[k]);
				}
				else{
					v3.add(salaryid[k]);
				}
			}
			
			System.out.println("salary<" + level[0]);
			ans += ("salary<" + level[0] + "\n");
			showDetail(v0);

			System.out.println("salary<" + level[1]);
			ans += ("salary<" + level[1] + "\n");
			showDetail(v1);

			System.out.println("salary<" + level[2]);
			ans += ("salary<" + level[2] + "\n");
			showDetail(v2);

			System.out.println("salary<" + level[3]);
			ans += ("salary<" + level[3] + "\n");
			showDetail(v3);
			
			int[] jobYears = new int[100];
			//鏁ｇ偣鍥�
			int nnnn = 0;
			for(int i = 0; i<ids.length; i++){
				ResultSet resultSet = statement.executeQuery("SELECT expectSalary, jobExp1_fromDate FROM information2 WHERE ID = '" + ids[i] + "'");
				
				if(resultSet.next()){
					int salary_ = parseSalaryFromStringToInt(resultSet.getString(1));
					resultSet.getString(2);           //杩欏彞璇濅笉鑳藉垹銆�
					if(salary_ < 0 || resultSet.wasNull()) continue;
					salary[n] = salary_;
					//鍒嗘瀽鏃ユ湡锛屽苟淇濆瓨鍒版暟缁�
	                String regEx="([0-9]+)-[0-9]+-[0-9]+";
	                Pattern p = Pattern.compile(regEx);
	                Matcher m = p.matcher(resultSet.getString(2));
	                if(m.find()){ 
	                	jobYears[i] = 2015 - Integer.parseInt(m.group(1));
	                }
					//id
					salaryid[n++] = ids[i];
				}
			}
			
//		    List<Map<String, Number>> list = new ArrayList<Map<String, Number>>();
//		    Map<String, Number> map = null;
//		 
//		    double xs[] = new double[] { 25, 28, 34, 35, 43, 48, 55, 62, 65, 67  };
//		 
//		    double ys[] = new double[] { 13, 12, 17, 18, 18, 21, 26, 30, 29, 28 };
//		 
//		    for (int i = 0, j = xs.length; i < j; i++) {
//		        map = new HashMap<String, Number>();
//		        map.put("x", xs[i]);
//		        map.put("y", ys[i]);
//		        list.add(map);
//		    }
			

//		    // Create Chart
//		    Chart chart = new Chart(800, 600);
//		    chart.getStyleManager().setChartType(ChartType.Scatter);
//
//		    // Customize Chart
//		    chart.getStyleManager().setChartTitleVisible(false);
//		    chart.getStyleManager().setLegendPosition(LegendPosition.InsideSW);
//		    chart.getStyleManager().setMarkerSize(16);
//
//		    // Series
//		    chart.addSeries("Gaussian Blob", jobYears, salary);
		    
		    
		    
		    
//		    double[] xData = new double[]{1, 2 ,4 , 8};
//		    double[] yData = new double[]{level[0], level[1], level[2], level[3]};
//		    
//		    Chart chart = QuickChart.getChart("Salary Chart", "Years", "Salary", "Salary-Year", xData, yData);
		    
		    
//		    ScatterChart01 scatterchart = new ScatterChart01() ;
//		    // Create Chart
//		    Chart chart = scatterchart.getChart("Salary Chart", "Years", "Salary", "Salary-Year", xData, yData);
//		     Show it
//		    new SwingWrapper(chart).displayChart();
		    

	  }
}
	
