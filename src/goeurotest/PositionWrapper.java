package goeurotest;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PositionWrapper {
	private String query;
	private String locale;
	private String requestPath;
	private String filename;

	public PositionWrapper(String[] args) {
		this.query = args[0];

		if (args.length > 1)
			this.locale = args[1].toLowerCase();
		else {
			this.locale = "en";
		}

		if (args.length > 2)
			this.filename = args[2];
		else {
			setFilename();
		}

		this.requestPath = "http://www.goeuro.com/GoEuroAPI/rest/api/v2/position/suggest/";
	}

	public String retrieve() {
		InputStream is = null;
		OutputStreamWriter os = null;
		String response = "OK";
		try {
			URL url = new URL(getURI());
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("Accept-Charset",
					StandardCharsets.UTF_8.name());
			is = connection.getInputStream();

			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			JSONArray jsa = new JSONArray(in.readLine());

			os = new OutputStreamWriter(new FileOutputStream(getFilename()));
			os.append("_type,_id,name,type,latitude,longitude");
			os.append(GoEuroConstants.NEWLINE);

			for (int i = 0; i < jsa.length(); i++) {
				JSONObject current = jsa.getJSONObject(i);
				StringBuffer record = new StringBuffer();
				record.append(current.optString("_type", null));
				record.append(",");
				record.append(current.get("_id").toString());
				record.append(",");
				record.append(current.getString("name"));
				record.append(",");
				record.append(current.getString("type"));
				record.append(",");
				JSONObject geoP = current.getJSONObject("geo_position");
				record.append(geoP.get("latitude").toString());
				record.append(",");
				record.append(geoP.get("longitude").toString());
				record.append(System.getProperty("line.separator"));
				os.append(record);
				os.flush();
			}
		} catch (MalformedURLException mue) {
			System.out.println("URL is malformed");
			response = "KO";
			mue.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("Connection Problem detected");
			response = "KO";
			ioe.printStackTrace();
		} catch (JSONException je) {
			System.out.println("Service respond with no JSON-like objects");
			response = "KO";
			je.printStackTrace();
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException ioe) {
				System.out.println("Connection Problem detected");
				response = "KO";
				ioe.printStackTrace();
			}
		}

		return response;
	}

	private void setFilename() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd_hhmmss");
		String timestamp = sdf.format(Calendar.getInstance().getTime());
		this.filename = ("GoEuro_" + this.query + "_" + timestamp + ".csv");
	}

	public String getFilename() {
		return this.filename;
	}

	public String getURI() {
		return this.requestPath + this.locale + "/" + this.query;
	}
}