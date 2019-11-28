package com.zss.client;


import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class CodeCatch {


    public static Connection connectInit()  throws ClassNotFoundException {
        /* 连接数据库 */

        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/asharequerysystem";
        String user = "root";
        String password = "123456";

        //建立数据库连接
        Connection conn;
        conn = null;
        String sql;
        Statement stmt;
        try{
            conn=DriverManager.getConnection(url,user,password);

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return conn;


    }


    public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
        //连接数据库
        Connection conn=  connectInit();
       getAStock(conn);
       close(conn);

    }

    private static void getBatchStackCodeAndNameAndUrl(URL url,Connection conn) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(30000);

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"GB2312"));
        String line = null;
        StringBuffer sb = new StringBuffer();
        boolean flag = false;
        while ((line = br.readLine()) != null) {


            if(line.contains("<span id=\"name_")){
                System.out.println(line);
                //处理字符串直接存入数据库
                handleStockCodeAndNameAndUrl(line,conn);
            }

        }
        if (br != null) {
            br.close();
            br = null;
        }


    }

    private static void handleStockCodeAndNameAndUrl(String line,Connection conn) {

        String codeName="";
        String url="";
        //取=与&直接的
        String regexChn="[\u4e00-\u9fa5]+";
        String rgex = "http(.*?)stock";
        url=getSubUtil(line,rgex);
        String code= StringUtils.substringBetween(url,"q=","&");
        insertCode(getStockUrlString(code,url),getStockUrlString(regexChn,line),url,conn);


        /*System.out.println(line);
        System.out.println(url=getSubUtil(line,rgex));
        System.out.println(getStockUrlString(regexNumber,url));
        System.out.println(getStockUrlString(regexChn,line));*/

    }
    private static String getStockUrlString(String regex ,String string){
        System.out.println("getStockUrlString");
        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(string);

        while (m.find()) {
            if (!"".equals(m.group())){

                return m.group();

            }

        }

        return "";
    }
    /**
     * 正则表达式匹配两个指定字符串中间的内容
     * @param line
     * @return
     */
    private static String getSubUtil(String line, String rgex) {

        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(line);
        while (m.find()) {
           return m.group();

        }

        return "";
    }

    public static String getBatchStackCodes(URL url) throws IOException {

        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(30000);
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = null;
        StringBuffer sb = new StringBuffer();
        boolean flag = false;
        while ((line = br.readLine()) != null) {

            if (line.contains("<script type=\"text/javascript\">") || flag) {
                sb.append(line);

                flag = true;
            }

            if (line.contains("</script>")) {
                flag = false;
                if (sb.length() > 0) {
                    if (sb.toString().contains("code_list") && sb.toString().contains("element_list")) {
                        break;
                    } else {
                        sb.setLength(0);
                    }
                }
            }
        }
        if (br != null) {
            br.close();
            br = null;
        }
        return sb.toString();
    }
    public static List<String> handleStockCode(String code) {
        List<String> codes = null;
        int end = code.indexOf(";");
        code = code.substring(0, end);
        int start = code.lastIndexOf("=");
        code = code.substring(start);
        code = code.substring(code.indexOf("\"") + 1, code.lastIndexOf("\""));
        codes = Arrays.asList(code.split(","));
        return codes;
    }

    public static void close(Connection conn) {
        if(conn != null) {
            try {
                conn.close();  //关闭数据库链接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private static void getAStock (Connection conn) throws IOException {


        // 获取新浪78也的所有股票代码
        //从sina获取所有股票代码
        // http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?p=96&sr_p=-1
        //        // <span id="name_sz000979">中弘股份</span></a></td>
        System.out.println("Start getAllStackCodes...");



        List<String> codes = new ArrayList<String>();
        int i = 1;
        URL url = null;

        int total= getPages();
        int page=total/40;
        if(total%40!=0){
            page=total/40+1;
        }

        for (; i <page; i++) {
            url = new URL("http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?p=" + i + "&sr_p=-1");
           getBatchStackCodeAndNameAndUrl(url,conn);

        }
        close(conn);



    }
    /*检测股票代码页数
     * http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?sr_p=-1
     * 获取股票总数total  pagesize=40
     * */
    private static int getPages() throws IOException {
        URL url=new URL("http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?sr_p=-1)");
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(30000);

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"GB2312"));
        String line = null;
        StringBuffer sb = new StringBuffer();
        boolean flag = false;

        Integer total=null;
        while ((line = br.readLine()) != null) {


            if(line.contains("只股票符合选择条件")){
                System.out.println(line);
                String regEx="[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(line);
                total=new Integer(m.replaceAll("").trim());


            }

        }
        if (br != null) {
            br.close();
            br = null;
        }


        return total;
    }

    private static void insertCode(String code,String name,String url,Connection conn) {
        PreparedStatement ps=null;
        //先判断这个代码是否存在
        String selectSql="SELECT * FROM CODE WHERE code=?";
        String sql = "INSERT INTO code (code,name,url,active) VALUES(?,?,?,1)";        //插入sql语句

       /* boolean flag=true;
        while (!flag)break;*/


        try {
            System.out.println("开始检查");
            ps = conn.prepareStatement(selectSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,code);
           if(ps.executeQuery().next())  {
              // System.out.println("code存在");
               return;
           }
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1,code);
            ps.setString(2,name);
            ps.setString(3,url);
            ps.executeUpdate();            //执行sql语句
            System.out.println("insert success");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
