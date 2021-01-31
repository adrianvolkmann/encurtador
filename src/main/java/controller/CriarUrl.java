package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import dao.Dynamo;
import utils.ConstantUtils;
import utils.JsonConverter;

public class CriarUrl implements RequestStreamHandler {

	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

		String urlOriginal = tratarEntrada(input);
		String urlCurta = geraUrlCurta();

		// gravar banco
		if (!urlOriginal.isEmpty()) {
			Dynamo dynamo = new Dynamo();
			dynamo.gravarUrl(urlCurta, urlOriginal);
		}

		// retornar resposta json
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(montarReposta(urlCurta, urlOriginal));
		writer.close();
	}

	private String montarReposta(String urlCurta, String urlOriginal) {
		JSONObject responseBody = new JSONObject();
		JSONObject responseJson = new JSONObject();

		if (urlOriginal.isEmpty()) {
			responseBody.put("erro", "Verifique a sintaxe e tente novamente");

			responseJson.put("statusCode", 400);
			responseJson.put("body", responseBody.toString());

		} else {
			responseBody.put("urlEncurtada", urlCurta);
			responseBody.put("urlOriginal", urlOriginal);

			responseJson.put("statusCode", 200);
			responseJson.put("body", responseBody.toString());
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

		/*
		 * metodo nao encontra o valor se estiver aninhado, usa metodo auxiliar para
		 * procurar dentro do body, procurar alternativas json parsers
		 */

		try {
			retorno = (String) event.get("urlOriginal").toString();
		} catch (Exception e) {
			try {
				String body = (String) event.get("body").toString();
				retorno = JsonConverter.retornaValor(body);
			} catch (Exception e2) {
				// formatos invalidos
				retorno = "";
			}
		}

		return retorno;
	}

	private String geraUrlCurta() {
		return ConstantUtils.DOMINIO + "/" + RandomStringUtils.randomAlphanumeric(6);
	}

}
