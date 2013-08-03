package TOI.model;

import TOI.Constant.Constant;
import TOI.util.ItemUtils;
import TOI.util.ProductUtils;
import TOI.util.VelocityUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.tools.generic.MathTool;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Product
{
    public String pid;
    public String tid;
    public String price;
    public String title;
    public List<Item> itemsList = new ArrayList();
    public Map<String, String> itemTypes = null;
    public String itemIds;
    public String categoryLocal;
    public String subCategoryLocal;
    public String weight;
    public String virtualWeight;
    public String description;
    public List<String> mainPicsLocal = new ArrayList();

    public Product(String id)
    {
        this.pid = id;
    }

    public List<String> setMainPics()
    {
        ArrayList mainPics = new ArrayList();
        for (Item item : this.itemsList)
            mainPics.add(item.picUrls.get(0));
        return mainPics;
    }

    public List<String> setMainPicLocal()
    {
        List mainPics = setMainPics();
        try {
            for (int i = 0; i < mainPics.size(); i++) {
                URL url = new URL("mainPics.get(i)");
                URLConnection urlCon = url.openConnection();
                InputStream is = urlCon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                FileOutputStream fos = new FileOutputStream(new StringBuilder().append("E:\\IKEAPIC\\\\").append(this.pid).append("_").append(i).append(".jpg").toString());
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int read;
                while ((read = bis.read()) != -1) {
                    bos.write(read);
                }
                bos.close();
                this.mainPicsLocal.add(new StringBuilder().append("E:\\IKEAPIC\\\\").append(this.pid).append("_").append(i).append(".jpg").toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.mainPicsLocal;
    }

    public void setVirtualWeight()
    {
        if ((this.weight != null) && (this.weight != "-1") && (this.price != null))
        {
            double vw = 1.3012D + 1.3752D * Double.parseDouble(this.weight) + 0.0206D * Double.parseDouble(this.price);
            DecimalFormat df = new DecimalFormat(".##");
            this.virtualWeight = df.format(vw);
        }
        else {
            this.virtualWeight = "-1";
        }
    }

    public void setVirtualWeight2()
    {
        if ((this.weight != null) && (this.weight != "-1"))
        {
            double vw = Math.ceil(Double.parseDouble(this.weight) * 1.2D);
            this.virtualWeight = String.valueOf(vw);
        }
        else {
            this.virtualWeight = "-1";
        }
    }

    public void setTitleAndPrice()
    {
        Item topItem = (Item)this.itemsList.get(0);
        this.title = new StringBuilder().append(topItem.name).append(topItem.facts).toString();
        System.out.println(this.title);
        this.price = topItem.price;
    }

    public void setItemsId()
    {
        if (this.itemTypes.size() != 0)
            this.itemIds = this.itemTypes.keySet().toString();
        else
            this.itemIds = new StringBuilder().append("[").append(this.pid).append("]").toString();
    }

    public void setItemsId2()
    {
        StringBuilder ii = new StringBuilder();
        ii.append("[");
        for (Item item : this.itemsList)
            ii.append(new StringBuilder().append(item.pid).append(",").toString());
        ii.append("]");
        this.itemIds = ii.toString().replace(",]", "]");
    }

    public String setDescription()
    {
        VelocityContext context = new VelocityContext();
        context.put("itemList", this.itemsList);
        context.put("math", new MathTool());
        this.description = VelocityUtil.filterVM(Constant.vm, context);
        return this.description;
    }

    public void initItemListFromIKEA()
    {
        if (this.itemTypes.isEmpty()) {
            Item item = new Item(this.pid);
            item.initItemFromIKEA(this.pid);
            this.itemsList.add(item);
            item.setTypeAndColorCode(item.name);
        } else {
            for (String id : this.itemTypes.keySet()) {
                Item item = new Item(id);
                item.initItemFromIKEA(id);
                item.setTypeAndColorCode((String)this.itemTypes.get(id));
                this.itemsList.add(item);
            }
        }

        for (int i = 0; i < this.itemsList.size(); i++) {
            for (int j = i + 1; j < this.itemsList.size(); j++) {
                if (compare((Item)this.itemsList.get(i), (Item)this.itemsList.get(j)) == 1)
                {
                    Item temp = (Item)this.itemsList.get(i);
                    this.itemsList.set(i, this.itemsList.get(j));
                    this.itemsList.set(j, temp);
                }
            }
        }
        for (int i = 0; i < this.itemsList.size(); i++)
            System.out.println(((Item)this.itemsList.get(i)).price);
    }

    public void initItemListFromSQL()
    {
        List<String> items = ProductUtils.setItemsFromSQL(this.pid);
        for (String id : items)
            this.itemsList.add(ItemUtils.setItemFromSQL(id));
    }

    public void setCategory(List<String> cates)
    {
        if (cates != null) {
            this.categoryLocal = ((String)cates.get(0));
            this.subCategoryLocal = ((String)cates.get(1));
        }
    }

    public void creatDescribtion()
    {
    }

    public int compare(Item arg0, Item arg1)
    {
        Item item1 = arg0;
        Item item2 = arg1;

        double flag1 = Double.parseDouble(item1.price) - Double.parseDouble(item2.price);
        if (flag1 < 0.0D) {
            return -1;
        }
        return 1;
    }
}