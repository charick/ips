package TOI.util;

import TOI.Constant.Constant;
import TOI.model.Product;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.FileItem;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Picture;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaobaoUtils
{
    public static Map<String, String> getCCMapFromFile()
    {
        Map result = new HashMap();
        try
        {
            InputStreamReader insReader = new InputStreamReader(new FileInputStream(new File("resources/category/tb_category")), "utf-8");
            BufferedReader bufReader = new BufferedReader(insReader);
            String temp;
            while ((temp = bufReader.readLine()) != null) {
                String[] tempA = temp.split("#######");
                result.put(tempA[0], tempA[1]);
            }
            bufReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void saveTBcategory2File() {
        try {
            ArrayList<SellerCid> tbclist = getSellerCid();
            FileWriter fw = new FileWriter(new File("resources/category/tb_category"));
            for (SellerCid tbCategory : tbclist) {
                fw.write(tbCategory + "\n");
            }
            fw.flush();
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("save taobao category to file finish!");
    }

    public static void addTaobaoItem(Product p)
    {
        for (TOI.model.Item item : p.itemsList) {
            ItemUtils.setPicUrlAtTaobao(item);
        }
        TaobaoClient client = new DefaultTaobaoClient(Constant.url, Constant.appkey, Constant.appSecret);
        ItemAddRequest req = new ItemAddRequest();
        req.setNum(Long.valueOf(30L));
        req.setPrice(p.price);
        req.setType("fixed");
        req.setStuffStatus("new");
        req.setTitle(p.title);
        req.setDesc(p.setDescription());
        req.setLocationState("北京");
        req.setLocationCity("北京");
        req.setApproveStatus("instock");
        req.setCid(Long.valueOf(50006298L));

        req.setFreightPayer("buyer");

        req.setHasInvoice(Boolean.valueOf(true));
        req.setHasWarranty(Boolean.valueOf(true));

        System.out.println((String)getCCMapFromFile().get(p.subCategoryLocal));
        req.setSellerCids((String)getCCMapFromFile().get(p.subCategoryLocal));
        req.setItemWeight(p.virtualWeight);

        if (Constant.nick == "charick") {
            req.setPostageId(Long.valueOf(755800881L));
        } else {
            req.setPostFee("5");
            req.setExpressFee("5");
            req.setEmsFee("5");
        }

        FileItem fItem = new FileItem(new File("E:\\IKEAPIC\\ITEMPICS\\" + ((TOI.model.Item)p.itemsList.get(0)).pid + "-0.jpg"));
        System.out.println("E:\\IKEAPIC\\ITEMSPIC\\" + ((TOI.model.Item)p.itemsList.get(0)).pid + "-0.jpg");
        req.setImage(fItem);

        req.setOuterId(p.pid);
        try
        {
            ItemAddResponse response = (ItemAddResponse)client.execute(req, Constant.sessionKey);
            System.out.println(response.getBody());
            try
            {
                JSONObject root = new JSONObject(response.getBody());
                JSONObject item_add_response = (JSONObject)root.get("item_add_response");
                JSONObject item = (JSONObject)item_add_response.get("item");

                String num_iid = item.getString("num_iid");

                ProductUtils.updateProductTid(p, num_iid);
                System.out.println(num_iid.toString());
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        catch (ApiException e)
        {
            e.printStackTrace();
        }
        System.err.print(p.pid);
        ProductUtils.updateSingleValue(p.pid, "isChanged", "0");
    }
    /**
     * 获取线上宝贝
     */
    public static void getOnlineProducts()
    {
        TaobaoClient client = new DefaultTaobaoClient(Constant.url, Constant.appkey, Constant.appSecret);
        ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
        req.setFields("num_iid,title,price");

        req.setPageNo(Long.valueOf(1L));

        req.setOrderBy("list_time:desc");
        req.setIsTaobao(Boolean.valueOf(true));

        req.setPageSize(Long.valueOf(200L));
        try
        {
            ItemsOnsaleGetResponse response = (ItemsOnsaleGetResponse)client.execute(req, Constant.sessionKey);
            System.out.println(response.getTotalResults());
            System.out.println(response.getItems().get(0));
            int count = 0;
            for (com.taobao.api.domain.Item onlineProduct : response.getItems()) {
                String numId = onlineProduct.getNumIid().toString();
                String title = onlineProduct.getTitle();
                if (title.contains(".")) {
                    int firstDot = title.indexOf(".");
                    String pid = title.substring(firstDot - 3, firstDot + 7).replace(".", "");
                    System.out.println(pid);
                    Product p = new Product(pid);
                    p.tid = numId;
                    p.title = title;
                    ProductUtils.addProductToSQL(p);
                    count++;
                }
            }
            System.out.println(count);
        }
        catch (ApiException e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新线上产品
     */
    public static void updateOnlineItem(Product product)
    {
        TaobaoClient client = new DefaultTaobaoClient(Constant.url, Constant.appkey, Constant.appSecret);
        if (product.tid != null) {
            ItemUpdateRequest req = new ItemUpdateRequest();
            req.setNumIid(Long.valueOf(Long.parseLong(product.tid)));

            req.setDesc(product.setDescription());

            req.setSellerCids((String)getCCMapFromFile().get(product.subCategoryLocal));
            req.setPostageId(Long.valueOf(755800881L));

            req.setItemWeight(product.virtualWeight);
            try
            {
                ItemUpdateResponse response = (ItemUpdateResponse)client.execute(req, Constant.sessionKey);
                System.out.println(response.getBody());
            }
            catch (ApiException e)
            {
            }
        }
        else {
            System.err.println("No tid");
        }
    }

    private static ArrayList<SellerCid> getSellerCid()
    {
        ArrayList tbcategories = new ArrayList();
        TaobaoClient client = new DefaultTaobaoClient(Constant.url, Constant.appkey, Constant.appSecret);
        SellercatsListGetRequest req = new SellercatsListGetRequest();
        req.setNick(Constant.nick);

        String tmp = null;
        try {
            SellercatsListGetResponse response = (SellercatsListGetResponse)client.execute(req);
            tmp = response.getBody();
        } catch (ApiException e1) {
            e1.printStackTrace();
        }
        try {
            JSONObject root = new JSONObject(tmp);
            JSONObject seller_cats = (JSONObject)root.get("sellercats_list_get_response");
            JSONObject seller_cat = (JSONObject)seller_cats.get("seller_cats");
            JSONArray seller_cat_array = seller_cat.getJSONArray("seller_cat");
            int i = 0;

            while (!seller_cat_array.isNull(i)) {
                JSONObject product = (JSONObject)seller_cat_array.get(i++);
                if (!"0".equals(product.getString("parent_cid")))
                {
                    SellerCid tbp = new SellerCid();
                    tbp.cid = product.getString("cid");
                    tbp.name = product.getString("name");
                    if (tbcategories.contains(tbp)) {
                        SellerCid older = (SellerCid)tbcategories.get(tbcategories.indexOf(tbp));
                        older.cid = (older.cid + "," + tbp.cid);
                    } else {
                        tbcategories.add(tbp);
                    }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return tbcategories;
    }

    public static void deleteUnusePics()
    {
        TaobaoClient client = new DefaultTaobaoClient(Constant.url, Constant.appkey, Constant.appSecret);
        PictureGetRequest req = new PictureGetRequest();

        req.setPictureCategoryId(Long.valueOf(15718119237420486L));

        req.setOrderBy("time:desc");

        req.setPageNo(Long.valueOf(1L));
        req.setPageSize(Long.valueOf(100L));
        try
        {
            PictureGetResponse response = (PictureGetResponse)client.execute(req, Constant.sessionKey);
            System.out.println(response.getBody());
            List pictureList = response.getPictures();
            for (int i = 0; i < pictureList.size(); i++) {
                Long pictureId = ((Picture)pictureList.get(i)).getPictureId();
                PictureIsreferencedGetRequest req1 = new PictureIsreferencedGetRequest();
                req1.setPictureId(pictureId);
                PictureIsreferencedGetResponse response1 = (PictureIsreferencedGetResponse)client.execute(req1, Constant.sessionKey);
                System.out.println(response1.getIsReferenced());
                if (!response1.getIsReferenced().booleanValue()) {
                    PictureDeleteRequest req2 = new PictureDeleteRequest();
                    req2.setPictureIds(pictureId.toString());
                    PictureDeleteResponse response2 = (PictureDeleteResponse)client.execute(req2, Constant.sessionKey);
                    System.out.println(response2.getSuccess());
                }
            }
        }
        catch (ApiException e)
        {
            e.printStackTrace();
        }
    }

    public static void getPicCategory(String name) {
        TaobaoClient client = new DefaultTaobaoClient(Constant.url, Constant.appkey, Constant.appSecret);
        PictureCategoryGetRequest req = new PictureCategoryGetRequest();

        req.setPictureCategoryName(name);

        req.setParentId(Long.valueOf(-1L));
        try {
            PictureCategoryGetResponse response = (PictureCategoryGetResponse)client.execute(req, Constant.sessionKey);
            System.out.println(response.getBody());
        }
        catch (ApiException e)
        {
            e.printStackTrace();
        }
    }

    public static void uploadExtraPic(Product p) {
        TaobaoClient client = new DefaultTaobaoClient(Constant.url, Constant.appkey, Constant.appSecret);
        ItemImgUploadRequest req = new ItemImgUploadRequest();

        for (int i = 1; (i < p.itemsList.size()) && (i < 5); i++) {
            req.setNumIid(Long.valueOf(Long.parseLong(p.tid)));
            req.setPosition(Long.valueOf(i));

            FileItem fItem = new FileItem(new File("E:\\IKEAPIC\\ITEMPICS\\" + ((TOI.model.Item)p.itemsList.get(i)).pid + "-0.jpg"));

            System.out.println("E:\\IKEAPIC\\ITEMSPIC\\" + ((TOI.model.Item)p.itemsList.get(i)).pid + "-0.jpg");

            req.setImage(fItem);
            if (i == 0)
                req.setIsMajor(Boolean.valueOf(true));
            else
                req.setIsMajor(Boolean.valueOf(false));
            try {
                ItemImgUploadResponse response = (ItemImgUploadResponse)client.execute(req, Constant.sessionKey);
                System.out.println(response.getBody());
            }
            catch (ApiException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static String         WAIT_SELLER_SEND_GOODS="WAIT_SELLER_SEND_GOODS" ;//(等待卖家发货,即:买家已付款)
    public static String        SELLER_CONSIGNED_PART="SELLER_CONSIGNED_PART";     //卖家部分发货
    public static String        WAIT_BUYER_CONFIRM_GOODS="WAIT_BUYER_CONFIRM_GOODS";     //卖家发货
    public static String        TRADE_BUYER_SIGNED="TRADE_BUYER_SIGNED";     //签收
    public static String        TRADE_FINISHED="TRADE_FINISHED";     //货到付款专用

    public static List<Trade> TradeFilter(String start, String end, String type,String field)
    {
        try
        {
            TaobaoClient client = new DefaultTaobaoClient(Constant.url, Constant.appkey, Constant.appSecret);
            TradesSoldGetRequest req = new TradesSoldGetRequest();
            req.setFields(field);
            Date dateTime = SimpleDateFormat.getDateTimeInstance().parse(start + " 00:00:00");
            req.setStartCreated(dateTime);
            Date dateTime2 = SimpleDateFormat.getDateTimeInstance().parse(end + " 23:59:59");
            req.setEndCreated(dateTime2);
            req.setStatus(type);
//            req.setBuyerNick("gaojia923");

            TradesSoldGetResponse response = client.execute(req, Constant.sessionKey);
            List<Trade> trades = response.getTrades();
           return trades;
        } catch (ApiException e1) {
            e1.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        }return null;
    }

    static class SellerCid
    {
        public String cid;
        public String name;

        public boolean equals(Object obj)
        {
            return ((SellerCid)obj).name.equals(this.name);
        }

        public String toString()
        {
            return this.name + "#######" + this.cid;
        }
    }
}