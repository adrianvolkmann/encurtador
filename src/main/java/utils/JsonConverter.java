package utils;

import org.json.JSONObject;

public class JsonConverter {

	public static String retornaValor(String jsonBody) {
		JSONObject bodyJson = new JSONObject(jsonBody);
		return (String) bodyJson.get("urlOriginal");
	}
}
