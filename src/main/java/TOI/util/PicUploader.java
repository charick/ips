package TOI.util;

import TOI.Constant.Constant;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.FileItem;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.PictureUploadRequest;
import com.taobao.api.response.PictureUploadResponse;

import java.io.File;

public class PicUploader
{
    public static String picUpload(String title)
    {
        try
        {
            TaobaoClient client = new DefaultTaobaoClient(Constant.url, Constant.appkey, Constant.appSecret);
            PictureUploadRequest req = new PictureUploadRequest();
            req.setPictureCategoryId(Long.valueOf(15718126980667548L));

            FileItem fItem = new FileItem(new File("E:\\IKEAPIC\\ITEMPICS\\" + title + ".jpg"));
            req.setImg(fItem);
            req.setImageInputTitle(title + ".jpg");
            req.setTitle(title);
            PictureUploadResponse response = (PictureUploadResponse)client.execute(req, Constant.sessionKey);
            String url = response.getPicture().getPicturePath();
            System.out.println(response.getPicture().getPicturePath());
            return url;
        } catch (ApiException e) {
            e.printStackTrace();
        }return null;
    }
}