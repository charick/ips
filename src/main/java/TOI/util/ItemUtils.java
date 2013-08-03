package TOI.util;

import TOI.Constant.Constant;
import TOI.model.Item;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemUtils
{
    private static void additem(Item item)
    {
        String sql = new StringBuilder().append("insert into ").append(Constant.item_sql).append("(pid,name,facts,price,assembledSize,designer,environment,goodToKnow,").append("careInst,").append("custMaterials,custBenefit,picUrls,modifyTime,type) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)").toString();
        try
        {
            PreparedStatement stmt = SQLUtils.getConnection().prepareStatement(sql);
            stmt.setString(1, item.pid);
            stmt.setString(2, item.name);
            stmt.setString(3, item.facts);
            stmt.setString(4, item.price);
            stmt.setString(5, item.assembledSize);
            stmt.setString(6, item.designer);
            stmt.setString(7, item.environment);
            stmt.setString(8, item.goodToKnow);
            stmt.setString(9, item.careInst);
            stmt.setString(10, item.custMaterials);
            stmt.setString(11, item.custBenefit);
            StringBuilder picUrls = new StringBuilder();
            ArrayList PicIds = item.picUrls;
            for (int i = 0; i < PicIds.size(); i++) {
                String picId = (String)PicIds.get(i);
                picUrls.append(picId);
                if (i != PicIds.size() - 1)
                    picUrls.append(",");
            }
            stmt.setString(12, picUrls.toString());
            stmt.setTimestamp(13, SQLUtils.getCurrentDateStr());
            stmt.setString(14, item.type);

            stmt.execute();
            System.out.println(new StringBuilder().append("ITEM:").append(item.pid).append("added!").toString());
        }
        catch (SQLException e) {
            System.out.println(new StringBuilder().append("=========SQLException==========").append(e.getMessage()).toString());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println(new StringBuilder().append("========ClassNotFoundException===========").append(e.getMessage()).toString());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(new StringBuilder().append("========Exception===========未知").append(e.getMessage()).toString());
            e.printStackTrace();
        }
    }

    private static boolean updateItem(Item item)
    {
        String sql = new StringBuilder().append("select * from  ").append(Constant.item_sql).append(" where pid= '").append(item.pid).append("'").toString();
        try {
            Statement stmt = SQLUtils.getConnection().createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String sql2 = new StringBuilder().append("update ").append(Constant.item_sql).append(" set name=?,facts=?,price=?,assembledSize=?,designer=?,").append("environment=?,").append("goodToKnow=?,careInst=?,custMaterials=?,custBenefit=?,picUrls=?,modifyTime=?,type=? ").append("where pid = '").append(item.pid).append("' ").toString();

                PreparedStatement pstmt = SQLUtils.getConnection().prepareStatement(sql2);
                pstmt.setString(1, item.name);
                pstmt.setString(2, item.facts);
                pstmt.setString(3, item.price);
                pstmt.setString(4, item.assembledSize);
                pstmt.setString(5, item.designer);
                pstmt.setString(6, item.environment);
                pstmt.setString(7, item.goodToKnow);
                pstmt.setString(8, item.careInst);
                pstmt.setString(9, item.custMaterials);
                pstmt.setString(10, item.custBenefit);
                StringBuilder picUrls = new StringBuilder();
                ArrayList PicIds = item.picUrls;
                for (int i = 0; i < PicIds.size(); i++) {
                    String picId = (String)PicIds.get(i);
                    picUrls.append(picId);
                    if (i != PicIds.size() - 1)
                        picUrls.append(",");
                }
                pstmt.setString(11, picUrls.toString());
                pstmt.setTimestamp(12, SQLUtils.getCurrentDateStr());
                pstmt.setString(13, item.type);
                pstmt.execute();
                pstmt.close();
                System.out.println(new StringBuilder().append("ITEM:").append(item.pid).append("update!").toString());
                return true;
            }return false;
        } catch (SQLException e) {
            System.out.println(new StringBuilder().append("=========SQLException==========").append(e.getMessage()).toString());
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println(new StringBuilder().append("========ClassNotFoundException===========").append(e.getMessage()).toString());
            e.printStackTrace();
        }return false;
    }

    public static void sendItemToSQL(Item item)
    {
        if (!updateItem(item))
            additem(item);
    }

    public static Item setItemFromSQL(String pid)
    {
        String sql = new StringBuilder().append("select * from  ").append(Constant.item_sql).append(" where pid='").append(pid).append("'").toString();
        Item item = new Item();
        try {
            Statement stmt = SQLUtils.getConnection().createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                item.pid = rs.getString("pid");
                item.name = rs.getString("name");
                item.facts = rs.getString("facts");
                item.price = rs.getString("price");
                item.assembledSize = rs.getString("assembledSize");
                item.designer = rs.getString("designer");
                item.environment = rs.getString("environment");
                item.goodToKnow = rs.getString("goodToKnow");
                item.careInst = rs.getString("careInst");
                item.custMaterials = rs.getString("custMaterials");
                item.custBenefit = rs.getString("custBenefit");
                String picUrl = rs.getString("picUrls");
                Collections.addAll(item.picUrls, picUrl.split(","));
                String picUrlAtTaobao = rs.getString("picUrlAtTaobao");
                if(StringUtils.isNotBlank(picUrlAtTaobao))
                Collections.addAll(item.picUrlsAtTaobao, picUrlAtTaobao.split(","));
                item.setTypeAndColorCode(rs.getString("type"));
            }
        } catch (SQLException e) {
            System.out.println(new StringBuilder().append("=========SQLException==========").append(e.getMessage()).toString());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println(new StringBuilder().append("========ClassNotFoundException===========").append(e.getMessage()).toString());
            e.printStackTrace();
        }
        return item;
    }

    public static List<Item> getItems(String product)
    {
        String sql = new StringBuilder().append("select * from  ").append(Constant.item_sql).append(" where product=").append(product).toString();

        List itemList = new ArrayList();
        try {
            Statement stmt = SQLUtils.getConnection().createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Item item = new Item();
                item.pid = rs.getString("pid");
                item.name = rs.getString("name");
                item.facts = rs.getString("facts");
                item.price = rs.getString("price");
                item.assembledSize = rs.getString("assembledSize");
                item.designer = rs.getString("designer");
                item.environment = rs.getString("environment");
                item.goodToKnow = rs.getString("goodToKnow");
                item.careInst = rs.getString("careInst");
                item.custMaterials = rs.getString("custMaterials");
                item.custBenefit = rs.getString("custBenefit");
                String picUrl = rs.getString("pid");
                Collections.addAll(item.picUrls, picUrl.split(","));
                itemList.add(item);
            }
        } catch (SQLException e) {
            System.out.println(new StringBuilder().append("=========SQLException==========").append(e.getMessage()).toString());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println(new StringBuilder().append("========ClassNotFoundException===========").append(e.getMessage()).toString());
            e.printStackTrace();
        }
        return itemList;
    }

    public static void savePicToLocal(Item item)
    {
        try
        {
            for (int i = 0; i < item.picUrls.size(); i++) {
                URL url = new URL((String)item.picUrls.get(i));
                URLConnection urlCon = url.openConnection();
                InputStream is = urlCon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                FileOutputStream fos = new FileOutputStream(new StringBuilder().append("E:\\IKEAPIC\\\\ITEMPICS\\\\").append(item.pid).append("-").append(i).append(".jpg").toString());

                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int read;
                while ((read = bis.read()) != -1) {
                    bos.write(read);
                }
                bos.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setPicUrlAtTaobao(Item item)
    {
        int count = 0;
        StringBuilder picUrlsAtTB=new StringBuilder();
        for (String url : item.picUrls) {
            String taobaoUrl = PicUploader.picUpload(new StringBuilder().append(item.pid).append("-").append(count++).toString());
            item.picUrlsAtTaobao.add(taobaoUrl.replace("http://img.taobaocdn.com/imgextra/",""));
        }
        for (int i = 0; i <  item.picUrlsAtTaobao.size(); i++) {
            String picUrlAtTB = (String) item.picUrlsAtTaobao.get(i);
            picUrlsAtTB.append(picUrlAtTB);
            if (i != item.picUrlsAtTaobao.size() - 1)
                picUrlsAtTB.append(",");
        }
        updateSingleValue(item.pid, "PicUrlAtTaobao", picUrlsAtTB.toString());

    }

    public static boolean updateSingleValue(String id, String key, String value)
    {
        try
        {
            String sql2 = new StringBuilder().append("update ").append(Constant.item_sql).append(" set ").append(key).append("=?,modifyTime=? where pid='").append(id).append("'").toString();
            PreparedStatement pstmt = SQLUtils.getConnection().prepareStatement(sql2);
            pstmt.setString(1, value);
            pstmt.setTimestamp(2, SQLUtils.getCurrentDateStr());
            pstmt.execute();
            pstmt.close();
            System.out.println(new StringBuilder().append("ITEM:").append(id).append(" ").append(key).append(" update!").toString());

            return true;
        } catch (SQLException e) {
            System.out.println(new StringBuilder().append("=========SQLException==========").append(e.getMessage()).toString());
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println(new StringBuilder().append("========ClassNotFoundException===========").append(e.getMessage()).toString());
            e.printStackTrace();
        }return false;
    }

    public static List<String> setItemListFromSQL() {
        List itemList = new ArrayList();
        Timestamp currentDate = SQLUtils.getCurrentDateStr();

        String sql = new StringBuilder().append("select * from  ").append(Constant.item_sql).append(" where stockType is null").toString();

        PreparedStatement pstmt = null;
        try {
            Statement stmt = SQLUtils.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String pid = rs.getString("pid");
                itemList.add(pid);
            }
        } catch (SQLException e) {
            System.out.println(new StringBuilder().append("=========SQLException==========").append(e.getMessage()).toString());
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println(new StringBuilder().append("========ClassNotFoundException===========").append(e.getMessage()).toString());
            e.printStackTrace();
        }

        return itemList;
    }
}