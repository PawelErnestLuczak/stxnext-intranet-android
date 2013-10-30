
package com.stxnext.management.android.web.api.services;

import org.json.JSONObject;

import android.util.Log;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.util.EntityUtils;

import com.stxnext.management.android.web.api.HTTPResponse;


public class IntranetService extends AbstractService {

    @Override
    protected String getServiceDomain() {
        return "intranet.stxnext.pl";
    }

    public IntranetService() {
        super();
    }

    public HTTPResponse<String> loginWithCode(String code)
            throws Exception {
        
        HTTPResponse<String> result = new HTTPResponse<String>();
        HttpGet request = getRequest("auth/callback?code="+code, null,false);

        HttpResponse response = executeRequestAndParseError(request, result);
        HttpEntity entity = response.getEntity();
        if(result.ok()){
            
            saveCookies();
            String jsonStub = EntityUtils.toString(entity);
            Log.e("","ok");
        }
        EntityUtils.consume(entity);
        return result;
    }
   
    public HTTPResponse<String> getUsers()
            throws Exception {
        
        HTTPResponse<String> result = new HTTPResponse<String>();
        HttpGet request = getRequest("api/users?full=1&inactive=1", null,false);


        HttpResponse response = executeRequestAndParseError(request, result);
        HttpEntity entity = response.getEntity();
        if(result.ok()){
            String jsonStub = EntityUtils.toString(entity);
            JSONObject json = new JSONObject(jsonStub);
            result.setExpectedResponse(json.getString("token"));
        }
        EntityUtils.consume(entity);
        return result;
    }
    
    
}
