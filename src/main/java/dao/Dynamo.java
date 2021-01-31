package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import model.Url;

public class Dynamo {
	private AmazonDynamoDB client;
	private DynamoDBMapper mapper;

	public Dynamo() {
		this.client = AmazonDynamoDBClientBuilder.standard().build();
		this.mapper = new DynamoDBMapper(client);
	}

	public void gravarUrl(String urlCurta, String UrlOriginal) {
		mapper.save(new Url(urlCurta, UrlOriginal));
	}

	public String retornaUrlOriginal(String urlCurta) {
		return mapper.load(Url.class, urlCurta).getUrlOriginal();
	}
}
