package com.zss.myspringboot.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CodeCatchFromTHS {
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
            conn= DriverManager.getConnection(url,user,password);

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return conn;


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
    private static void getAStockFromTHS (Connection conn) throws IOException {


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
           /// getBatchStackCodeAndNameAndUrl(url,conn);

        }




    }
    /*检测股票代码页数
     * http://q.10jqka.com.cn/index/index/board/all/field/zdf/order/desc/page/{page}/ajax/1/
     * 获取股票总数total  pagesize=40
     * */
    private static int getPages() throws IOException {
        String urlString="http://q.10jqka.com.cn/index/index/board/all/field/zdf/order/desc/page/1/ajax/1/";
        //int page=1;

        URL url=new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(30000);

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"GB2312"));
        String line = null;
        StringBuffer sb = new StringBuffer();
        boolean flag = false;

        Integer total=null;
        while ((line = br.readLine()) != null) {
////<span class="page_info">1/185</span>

            if(line.contains("<span class=\"page_info\">1/185</span>")){
                System.out.println(line);
              /*  String regEx="[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(line);
                total=new Integer(m.replaceAll("").trim());*/


            }

        }
        if (br != null) {
            br.close();
            br = null;
        }


        return total;
    }


    //http://q.10jqka.com.cn/index/index/board/all/field/zdf/order/desc/page/{page}/ajax/1/
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        /*Connection conn=  connectInit();
        close(conn);*/
        getPages();

    }

}
