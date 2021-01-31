package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import dao.Dynamo;
import utils.ConstantUtils;

public class buscarUrl implements RequestStreamHandler {

	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

		String urlCurta = tratarEntrada(input);
		String urlOriginal;

		Dynamo dynamo = new Dynamo();

		try {
			urlOriginal = dynamo.retornaUrlOriginal(urlCurta);
		} catch (Exception e) {
			urlOriginal = "";
		}

		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");

		//verifica se url tem http, necessario para redirecionar corretamente
		if (!urlOriginal.isEmpty()) {
			String temp = urlOriginal.toLowerCase();
			if (!temp.contains("http") || !temp.contains("https")) {
				urlOriginal = "http://" + urlOriginal;
			}
		}
		writer.write(montarReposta(urlOriginal));
		writer.close();

	}

	private String montarReposta(String urlOriginal) {

		JSONObject responseJson = new JSONObject();

		if(urlOriginal.isEmpty()){
			responseJson.put("statusCode", 400);
			responseJson.put("error", "url não conhecida");
		}else {
			responseJson.put("statusCode", 301);
			responseJson.put("location", urlOriginal);
		}
		return responseJson.toString();
	}

	private String tratarEntrada(InputStream input) throws IOException {
		JSONParser parser = new JSONParser();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));

		JSONObject event = new JSONObject();
		String retorno;

		try {
			event = (JSONObject) parser.parse(reader);
		} catch (org.json.simple.parser.ParseException e) {
			retorno = "";
		}

		try {
			retorno = (String) event.get("short_id").toString();
		} catch (Exception e) {
			retorno = "";
		}

		return ConstantUtils.DOMINIO + "/" + retorno;
	}

}
