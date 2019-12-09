package com.zss.myspringboot.client;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.sql.*;

public class AshockNameCapital {
    public static void main(String[] args) throws ClassNotFoundException {


        //数据库连接
        CodeCatchFromSina codeCatch=new CodeCatchFromSina();
        Connection conn=  codeCatch.connectInit();
        //处理大写

        doCaptial(conn);

/*
        System.out.println(ToFirstChar("医生下达医嘱").toUpperCase());*/ //转为首字母大写


        //数据库关闭
     /*   codeCatch.close(conn);
        String cnStr = "235626玲";
        System.out.println(getPingYin(cnStr));
        System.out.println(getPinYinHeadChar(cnStr).replaceAll("[^a-z^A-Z^]", ""));
        String str = getPinYinHeadChar(cnStr).replaceAll("[^a-z^A-Z^]", "");
        if(StringUtils.isNoneBlank(str)){//判断是否有首字母
            str = str.substring(0,1).toUpperCase();//转换为大写
        }
        System.out.println(str);*/

    }

    private static void doCaptial(Connection conn) {
        PreparedStatement ps1=null;
        PreparedStatement ps2=null;
        //先判断这个代码是否存在
        String selectSql="SELECT name FROM CODE";
        //update code set namecapital='WK' Where name='万科';
        String sql = "update  code set name_capital=? WHERE name=?";        //插入sql语句

       /* boolean flag=true;
        while (!flag)break;*/


        try {
            int i=0;
            System.out.println("开始转换大写");
            ps1 = conn.prepareStatement(selectSql, Statement.RETURN_GENERATED_KEYS);
            ps2 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
           ResultSet rs = ps1.executeQuery();
            while (rs.next())  {
               String name=rs.getString(1);
                String   nameCapital="";

                System.out.println(name);
                nameCapital = ToFirstChar(name).toUpperCase();


               ps2.setString(1,nameCapital);
                ps2.setString(2,name);
                System.out.println(nameCapital);
               ps2.executeUpdate();
               i=i+1;

            }

            System.out.println(i);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




/**
    * 汉字转为拼音
    * @param chinese
    * @return
    */
public static String ToPinyin(String chinese){
String pinyinStr = "";
char[] newChar = chinese.toCharArray();
HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
for (int i = 0; i < newChar.length; i++) {
if (newChar[i] > 128) {
try {
pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
} catch (BadHanyuPinyinOutputFormatCombination e) {
e.printStackTrace();
}
}else{
pinyinStr += newChar[i];
}
}
   return pinyinStr;
}
         
    public static String ToFirstChar(String chinese) {
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray(); //转为单个字符
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0].charAt(0);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }

}
