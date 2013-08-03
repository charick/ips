package TOI.util;

import TOI.Constant.Constant;
import TOI.model.Item;
import TOI.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductUtils
{
    /**
     * 添加product到数据库
     */
    public static void addProductToSQL(Product product)
    {
        String sql = "insert into " + Constant.product_sql + "(id,title,price,category," + "items,modifyTime,tid,isChanged,weight,virtualWeight) values(?,?,?,?,?,?,?,?,?,?)";
        try
        {
            PreparedStatement stmt = SQLUtils.getConnection().prepareStatement(sql);
            stmt.setString(1, product.pid);
            stmt.setString(2, product.title);
            stmt.setString(3, product.price);
            stmt.setString(4, product.subCategoryLocal);

            stmt.setString(5, product.itemIds);
            stmt.setTimestamp(6, SQLUtils.getCurrentDateStr());
            stmt.setString(7, product.tid);
            stmt.setString(8, "1");
            stmt.setString(9, product.weight);
            stmt.setString(10, product.virtualWeight);

            stmt.execute();
            System.out.println(product.pid + "added!");
        }
        catch (SQLException e) {
            System.out.println("=========SQLException==========" + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("========ClassNotFoundException===========" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("========Exception===========未知" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static int checkIsExist(Product product)
    {
        int count = 0;
        try {
            String sql = "select * from  " + Constant.product_sql + " where items like '%" + product.pid + "%'";
            Statement stmt = SQLUtils.getConnection().createStatement();
            ResultSet rss = stmt.executeQuery(sql);
            while (rss.next()) {
                count++;
            }
            return count;
        } catch (SQLException e) {
            System.out.println("=========SQLException==========" + e.getMessage());
            e.printStackTrace();
            return count;
        } catch (ClassNotFoundException e) {
            System.out.println("========ClassNotFoundException===========" + e.getMessage());
            e.printStackTrace();
        }return count;
    }

    /**
     * 更新一个product到数据库
     */
    public static void updateProductToSQL(Product product)
    {
        String ischange = "0";
        try {
            String sql = "select * from  " + Constant.product_sql + " where items like '%" + product.pid + "%'";
            Statement stmt = SQLUtils.getConnection().createStatement();
            ResultSet rss = stmt.executeQuery(sql);
            while (rss.next()) {
                String id = rss.getString("id");
                if ((!rss.getString("title").equals(product.title)) && (product.title != null)) {
                    updateSingleValue(id, "title", product.title);
                    ischange = "1";
                }
                if ((!rss.getString("price").equals(product.price)) && (product.price != null)) {
                    updateSingleValue(id, "price", product.price);
                    ischange = "1";
                }
                if (rss.getString("category") == null) {
                    updateSingleValue(id, "category", product.subCategoryLocal);
                    ischange = "1";
                }
                else if ((!rss.getString("category").equals(product.subCategoryLocal)) && (product.subCategoryLocal != null)) {
                    updateSingleValue(id, "category", product.subCategoryLocal);
                    ischange = "1";
                }
                if ((!rss.getString("items").equals(product.itemIds)) && (product.itemIds != null)) {
                    updateSingleValue(id, "items", product.itemIds);
                    ischange = "1";
                }
                if (rss.getString("weight") == null) {
                    updateSingleValue(id, "weight", product.weight);
                    ischange = "1";
                }
                else if ((!rss.getString("weight").equals(product.weight)) && (product.weight != null)) {
                    updateSingleValue(id, "weight", product.weight);
                    ischange = "1";
                }

                if (rss.getString("virtualWeight") == null) {
                    updateSingleValue(id, "virtualWeight", product.virtualWeight);
                    ischange = "1";
                }
                else if ((!rss.getString("virtualWeight").equals(product.virtualWeight)) && (product.virtualWeight != null)) {
                    updateSingleValue(id, "virtualWeight", product.virtualWeight);
                    ischange = "1";
                }

                if (ischange.equals("1")) {
                    updateSingleValue(id, "isChanged", ischange);
                    updateModifyTime(id, SQLUtils.getCurrentDateStr());
                    System.out.println("PRODUCT:" + product.pid + "update!");
                } else {
                    System.out.println("PRODUCT:" + product.pid + " NOT update!");
                }
            }
        } catch (SQLException e) {
            System.out.println("=========SQLException==========" + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("========ClassNotFoundException===========" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void updateModifyTime(String id, Timestamp time) {
        try {
            String sql2 = "update " + Constant.product_sql + " set modifyTime=? where id ='" + id + "'";
            PreparedStatement pstmt = SQLUtils.getConnection().prepareStatement(sql2);
            pstmt.setTimestamp(1, time);

            pstmt.execute();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("=========SQLException==========" + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("========ClassNotFoundException===========" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean updateSingleValue(String id, String key, String value) {
        try {
            String sql2 = "update " + Constant.product_sql + " set " + key + "=? where id='" + id + "'";
            PreparedStatement pstmt = SQLUtils.getConnection().prepareStatement(sql2);
            pstmt.setString(1, value);

            pstmt.execute();
            pstmt.close();
            System.out.println("PRODUCT:" + id + " " + key + "update!");
            return true;
        } catch (SQLException e) {
            System.out.println("=========SQLException==========" + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("========ClassNotFoundException===========" + e.getMessage());
            e.printStackTrace();
        }return false;
    }

    /**
     * 更新一个product到数据库
     */
    public static boolean updateProductTid(Product product, String tid)
    {
        product.tid = tid;
        String sql = "select * from  " + Constant.product_sql + " where items like '%" + product.pid + "%'";
        try {
            Statement stmt = SQLUtils.getConnection().createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String sql2 = "update " + Constant.product_sql + " set tid=? where items like  '%" + product.pid + "%' ";
                PreparedStatement pstmt = SQLUtils.getConnection().prepareStatement(sql2);
                pstmt.setString(1, tid);
                pstmt.execute();
                pstmt.close();
                System.out.println("[TID]:" + product.pid + "update!");
                return true;
            }return false;
        } catch (SQLException e) {
            System.out.println("=========SQLException==========" + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("========ClassNotFoundException===========" + e.getMessage());
            e.printStackTrace();
        }return false;
    }
    /**
     * 把product放入数据库
     */
    public static void sendProductToSQL(Product product)
    {
        if (checkIsExist(product) == 1)
            updateProductToSQL(product);
        else if (checkIsExist(product) == 0)
            addProductToSQL(product);
        else
            System.err.println("product重复");
    }
    /**
     * 从IKEA获得一个完整的产品
     */
    public static Product grabProductFromIKEA(String id)
    {
        System.out.println("=============================\nNOW:" + id);
        Product p = new Product(id);
        String buf = IkeaUtils.setBuf(id);

        p.setCategory(IkeaUtils.setCategory(buf));
        p.itemTypes = IkeaUtils.setItemTypes(buf);

        p.initItemListFromIKEA();
        p.setItemsId2();
        p.setTitleAndPrice();
        p.weight = IkeaStockUtil.WeightCatcher(id);
        p.setVirtualWeight2();
        return p;
    }
    /**
     * 添加一个完整的产品到数据库
     */
    public static void addToSQL(Product p, int itemToggle)
    {
        sendProductToSQL(p);
        if (itemToggle == 1)
            if (p.itemsList.size() != 0)
                for (Item item : p.itemsList)
                    ItemUtils.sendItemToSQL(item);
            else
                System.err.println("itemsList为空");
    }
    /**
     * 从数据库获得一个完整的产品
     */
    public static Product setFromSQL(String id)
    {
        Product p = new Product(id);

        String sql = "select * from  " + Constant.product_sql + " where items like '%" + id + "%'";
        try {
            Statement stmt = SQLUtils.getConnection().createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                p.pid = rs.getString("id");
                p.title = rs.getString("title");
                System.out.println(p.title);
                p.price = rs.getString("price");
                p.subCategoryLocal = rs.getString("category");
                p.itemIds = rs.getString("items");
                p.tid = rs.getString("tid");
                p.weight = rs.getString("weight");
                p.virtualWeight = rs.getString("virtualWeight");
            }
            p.initItemListFromSQL();
        } catch (SQLException e) {
            System.out.println("=========SQLException==========" + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("========ClassNotFoundException===========" + e.getMessage());
            e.printStackTrace();
        }
        return p;
    }
    /**
     * 从数据库获得Items的清单
     */
    public static List<String> setItemsFromSQL(String id)
    {
        List itemList = new ArrayList();
        String sql = "select * from  " + Constant.product_sql + " where items like  '%" + id + "%' ";
        PreparedStatement pstmt = null;
        try {
            Statement stmt = SQLUtils.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String itemListString = rs.getString("items").replace("[", "").replace("]", "").replace(" ", "");
                Collections.addAll(itemList, itemListString.split(","));
            }
        } catch (SQLException e) {
            System.out.println("=========SQLException==========" + e.getMessage());
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("========ClassNotFoundException===========" + e.getMessage());
            e.printStackTrace();
        }

        return itemList;
    }
    /**
     * 从数据库获得产品列表
     */
    public static List<String> setProductListFromSQL()
    {
        List productList = new ArrayList();
        String sql = "select * from  " + Constant.product_sql + " where tid is not null ";
        PreparedStatement pstmt = null;
        try {
            Statement stmt = SQLUtils.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String pid = rs.getString("id");
                productList.add(pid);
            }
        } catch (SQLException e) {
            System.out.println("=========SQLException==========" + e.getMessage());
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("========ClassNotFoundException===========" + e.getMessage());
            e.printStackTrace();
        }

        return productList;
    }
}