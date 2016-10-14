package com.cn.fiveonefive.gphq.util;

import com.cn.fiveonefive.gphq.dto.Result;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by hb on 2016/3/25.
 */
public class GetHttpResult {

    private String url;

    public GetHttpResult(String url){
        this.url=url;
    }


    public Result getResult(){
        Result result=new Result();
        HttpGet httpRequest = new HttpGet(url);
//        Log.d("http", url);
        try{
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                if (strResult==null||strResult.equals("")){
                    result.setResultCode(-1);
                    return result;
                }

//                String str=strResult.substring(1,strResult.length()-1);
                result.setResultCode(0);
                result.setResultData(strResult);
            }else{
                result.setResultCode(-1);
                return result;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setResultCode(-2);
        }
        return result;
    }
}
