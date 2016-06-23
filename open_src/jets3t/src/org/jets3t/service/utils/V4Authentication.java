package org.jets3t.service.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.jets3t.service.ServiceException;
import org.jets3t.service.security.ProviderCredentials;
import org.apache.commons.codec.binary.Hex;


/**
 * V4鉴权接口
 *
 */

public class V4Authentication
{

    private static String content_sha256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

    private String ak;

    private String sk;

    private String region;
    
    private String nowISOtime;

    public static String getContent_sha256()
    {
        return content_sha256;
    }

    public static void setContent_sha256(String content_sha256)
    {
        V4Authentication.content_sha256 = (null == content_sha256?"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855":content_sha256);
    }

    public String getAk()
    {
        return ak;
    }

    public void setAk(String ak)
    {
        this.ak = ak;
    }

    public String getSk()
    {
        return sk;
    }

    public void setSk(String sk)
    {
        this.sk = sk;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public static String makeServiceCanonicalString(String methoed,
            Map<String, Object> headers, String strURIPath,
            ProviderCredentials credent) throws ServiceException
    {

        V4Authentication v4 = new V4Authentication();
        v4.setAk(credent.getAccessKey());
        v4.setSk(credent.getSecretKey());
        v4.setRegion(credent.getRegion());
        v4.getNowISOTime();
        
        List<String> signedAndCanonicalList = v4.getSignedAndCanonicalHeaders(headers);
        
        String authorization = "";
        authorization += "AWS4-HMAC-SHA256" + " Credential=" + v4.getCredential();
        authorization += ",SignedHeaders=" + signedAndCanonicalList.toArray()[0];
        authorization += ",Signature=" + v4.getSignature(methoed, strURIPath, signedAndCanonicalList);
        
        return authorization;
    }

    private void getNowISOTime()
    {
        SimpleDateFormat fmt1 = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        fmt1.setTimeZone(new SimpleTimeZone(0, "GMT"));
        Date now = new Date();
        this.nowISOtime = fmt1.format(now);
    }
    
    private String  getSignature(String method,String fulPath,List<String>canonical) throws ServiceException
    {
        String Signa = null;
        String StringToSign = this.getStringToSign(method, fulPath, canonical);
        byte[] SigningKey = this.getSigningKey();
//        System.out.println("StringToSign:"+StringToSign);
        try
        {
            Signa =V4Authentication.byteToHex(V4Authentication.hmac_sha256Encode(SigningKey, StringToSign));
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            throw new ServiceException("has an err when V4 aurhentication ",e);
        }

        return Signa;
    }
    private String getCredential() 
    {
        String outPut = "";
        
        String shortDate = this.nowISOtime.split("T")[0];
        outPut += this.ak + "/";
        outPut += shortDate + "/";
        outPut += this.region + "/";
        outPut += "s3" + "/" + "aws4_request";
        return outPut;
    }
    
    private List<String> getSignedAndCanonicalHeaders(Map<String, Object>headers)
    {
        List<String> list = new ArrayList<String>();
        String Signed = "";
        String Canonical = "";
        Map<String, String> map = new TreeMap<String, String>();
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, Object> entry: headers.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key == null) {
                    continue;
                }
                String lk = key.toString().toLowerCase(Locale.getDefault());
                map.put(lk, value.toString());
            }
            int i = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {  
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (i != 0)
                {
                    Signed += ";";
                }
                i = 1;
                Signed += key.toString();
                Canonical += key.toString() + ":" + value.toString() + "\n";
            }
        }
        
        list.add(Signed);
        list.add(Canonical);
        return list;
    }
    private List<String> getCanonicalURIAndQuery(String fulPath) throws ServiceException
    {
        String URI = "";
        String Query = "";
        String []pathStrings = fulPath.split("[?]");
        if(pathStrings.length > 0)
        {
            URI += pathStrings[0];
        }
        if (pathStrings.length > 1)
        {
            String []uri = pathStrings[1].split("[&]");
            Map<String, String> map = new TreeMap<String, String>();
            for(int i = 0; i < uri.length; i++)
            {
                String []kvStrings = uri[i].split("[=]");
                String  key = RestUtils.encodeUrlString(kvStrings[0]);
                String  val = "";
                if (kvStrings.length > 1)
                {
                    val = RestUtils.encodeUrlString(kvStrings[1]);
                }
                map.put(key, val);
            }
            int j = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {  
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (j != 0)
                {
                    Query += "&";
                }
                j = 1;
                Query += key.toString() + "=" + value.toString();
            }
            
        }      
        List<String> list = new ArrayList<String>();
        list.add(URI);
        list.add(Query);    
        return list; 
    }
    
    private String getStringToSign(String method,String fulPath,List<String>canonical) throws ServiceException
    {
        String outputString = "";
        outputString += "AWS4-HMAC-SHA256" + "\n";
        outputString += this.nowISOtime + "\n";
        outputString += getScope() + "\n";
  
        try
        {
            String canonicalRequest = this.getCanonicalRequest(method, fulPath, canonical);
//            System.out.println("CanonicalRequest:"+canonicalRequest);
            outputString += V4Authentication.byteToHex(V4Authentication.sha256encode(canonicalRequest));
        }
        catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            throw new ServiceException("has an err when V4 aurhentication,err ",e);
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            throw new ServiceException("has an err when V4 aurhentication,err ",e);
        }
        return outputString;
    }
    private String getScope()
    {
        String outPut = "";
        String shortDate = this.nowISOtime.split("T")[0];
        outPut += shortDate + "/";
        outPut += this.region +  "/";
        outPut += "s3/aws4_request";
        return outPut;
    }
   private String getCanonicalRequest(String method,String fulPath,List<String>canonical) throws ServiceException
   {
       String outPut = "";
       outPut += method + "\n";  
       List<String>  list = this.getCanonicalURIAndQuery(fulPath);
       outPut += list.toArray()[0] + "\n";
       outPut += list.toArray()[1] + "\n";
       outPut += canonical.toArray()[1] + "\n";
       outPut += canonical.toArray()[0] + "\n";
       outPut += V4Authentication.content_sha256;    
       return outPut;
   }
   
   private byte[] getSigningKey()
   {
       String shortDate = this.nowISOtime.split("[T]")[0];
       String keyString = "AWS4"+this.sk;
       byte[] signingKey = null;
       try
    {
        byte[] dateKey = V4Authentication.hmac_sha256Encode(keyString.getBytes("UTF-8"), shortDate);
        byte[] dateRegionKey = V4Authentication.hmac_sha256Encode(dateKey, this.region);
        byte[] dateRegionServiceKey = V4Authentication.hmac_sha256Encode(dateRegionKey, "s3");
        signingKey = V4Authentication.hmac_sha256Encode(dateRegionServiceKey, "aws4_request");
    }
    catch (UnsupportedEncodingException e)
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    catch (Exception e)
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
       
       return signingKey;
   }
   
   public static byte[] hmac_sha256Encode(byte[] key, String data) throws Exception {
       Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
       SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
       sha256_HMAC.init(secret_key);
       return sha256_HMAC.doFinal(data.getBytes("UTF-8"));
     }
   
   public static byte[] sha256encode(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException
   {
       MessageDigest digest;
       byte[] hash = null;
       digest = MessageDigest.getInstance("SHA-256");
       hash = digest.digest(str.getBytes("UTF-8"));

       return hash;
   }
   
   public static String byteToHex(byte[] hash)
   {
       byte[] hexB = new Hex().encode(hash);
       return new String(hexB);
   }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
}
