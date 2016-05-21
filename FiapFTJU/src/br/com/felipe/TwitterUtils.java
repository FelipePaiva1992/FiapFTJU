package br.com.felipe;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtils {

	public static Twitter twitter;

	public TwitterUtils() {
		ConfigurationBuilder builder = new ConfigurationBuilder();

		builder.setOAuthConsumerKey("chave");
		builder.setOAuthConsumerSecret("chave");

		Configuration configuration = builder.build();

		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();

		AccessToken accessToken = obterTokenAcesso();

		twitter.setOAuthAccessToken(accessToken);
	}

	private AccessToken obterTokenAcesso() {
		String token = "chave";
		String tokenSecret = "chave";
		return new AccessToken(token, tokenSecret);
	}

	
	//Obtem a lista de Status passando a data de inicio da consulta e a hashtag
	public List<Status> obterListaTweets(String dataInicio, String hashtag) throws TwitterException {
		Query query = new Query(hashtag);
		query.setSince(dataInicio);
		QueryResult result;
		List<Status> tweets = new ArrayList<>();
		result = twitter.search(query);
		while (result.hasNext()) {
			tweets.addAll(result.getTweets());
			query = result.nextQuery();
			result = twitter.search(query);
		}
		return tweets;
	}

	//Publica a mensagem no twitter
	public void publicarMensagem(String mensagem) throws TwitterException {
		twitter.updateStatus(mensagem);
	}

}
