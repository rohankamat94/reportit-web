package com.cirs.notification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.cirs.entities.Complaint;
import com.cirs.entities.Complaint.Status;
import com.cirs.entities.User;
import com.google.gson.Gson;

public class GcmRequestSender {
	private static final String AUTH_KEY="key=AIzaSyDv5G5WCn-Cm2iSmL3kUlehAhe_VXWxDp8";
	private static final Gson gson=new Gson();
	private static final String URL_STRING="https://gcm-http.googleapis.com/gcm/send";
	private static final class GcmRequestEntity{
		String to;
		GcmRequestDataEntity data;
		public GcmRequestEntity(String to, GcmRequestDataEntity data) {
			super();
			this.to = to;
			this.data = data;
		}
		
	}
	private static final class GcmRequestDataEntity{
		long complaintId;
		String newStatus;
		public GcmRequestDataEntity(long complaintId, String newStatus) {
			super();
			this.complaintId = complaintId;
			this.newStatus = newStatus;
		}
				
	}
	public static void makeRequest(Complaint of,Status oldStatus, Status newStatus){
		String to=of.getUser().getGcmToken();
		if(to==null || to.equals("")){
			return;
		}
		GcmRequestDataEntity data=new GcmRequestDataEntity(of.getId(), newStatus.toString());
		GcmRequestEntity entity=new GcmRequestEntity(to, data);
		String responseJson=gson.toJson(entity);
		System.out.println(responseJson);
		try {
			URL url=new URL(URL_STRING);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.addRequestProperty("Content-Type", "application/json");
			conn.addRequestProperty("Authorization",AUTH_KEY);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			bw.write(responseJson);
			bw.flush();
			bw.close();
			//TODO comment if not debugging
			System.out.println("status "+conn.getResponseCode());
			System.out.println("message"+conn.getResponseMessage());
//			BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			br.lines().forEach(System.out::println);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
