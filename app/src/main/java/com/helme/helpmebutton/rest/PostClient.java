package com.helme.helpmebutton.rest;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class PostClient {
	private String url = "https://apiservmobile.com/";

	private List<NameValuePair> mParams;
	private String mResponseString = "";
	private JSONObject mResponseJSON;
    private int mStatusCode = 400;

	/************************* Constructors *************************/
	
	public PostClient(List<NameValuePair> mParams, String methodName) {
		this.mParams = mParams;
        url = url.concat(methodName);
	}
	
	/****************************************************************/

	
	/************************ Public methods ************************/
	
	public void executeRequest() {
		try {
			HttpResponse httpResponse = request();
            mStatusCode = httpResponse.getStatusLine().getStatusCode();
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				setResponseString(convertStreamToString(instream));

                Log.i("json", "" + mStatusCode);
				Log.i("json", mResponseString);
				Log.i("json", "Return response size:" + mResponseString.length());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/****************************************************************/

	/************************ Private methods ************************/
	
	private HttpResponse request() throws ClientProtocolException, IOException, IllegalStateException, JSONException {
		DefaultHttpClient client = (DefaultHttpClient) WebClientDevWrapper.getNewHttpClient();
		HttpPost post = new HttpPost(url);
		post.setEntity(new UrlEncodedFormEntity(mParams, HTTP.UTF_8));

		return client.execute(post);
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		finally {
			try {
				is.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/*******************************************************************/

	/*********************** Getters and Setters ***********************/

	public String getResponseString() {
		return mResponseString;
	}

	private void setResponseString(String mResponseString) {
		this.mResponseString = mResponseString;
	}

	public JSONObject getResponseJSON() {
		try {
			mResponseJSON = new JSONObject(mResponseString);
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return mResponseJSON;
	}

    public int getStatuscode()
    {
        return mStatusCode;
    }
}
