package model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "tabelaUrl")
public class Url {

	private String urlCurta;
	private String urlOriginal;

	public Url(String urlCurta, String urlOriginal) {
		this.urlCurta = urlCurta;
		this.urlOriginal = urlOriginal;
	}

	public Url() {
	}

	@DynamoDBHashKey(attributeName = "urlCurta")
	public String getUrlCurta() {
		return urlCurta;
	}

	public void setUrlCurta(String urlCurta) {
		this.urlCurta = urlCurta;
	}

	@DynamoDBAttribute(attributeName = "urlOriginal")
	public String getUrlOriginal() {
		return urlOriginal;
	}

	public void setUrlOriginal(String urlOriginal) {
		this.urlOriginal = urlOriginal;
	}

}
