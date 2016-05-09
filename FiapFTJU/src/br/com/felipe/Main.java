package br.com.felipe;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class Main {

	/**
	 * @param args
	 */

	public static void main(String[] args) {

		try {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			
			builder.setOAuthConsumerKey("TZ6yn111rkOnOWvRJM81aF1sgS0");
			builder.setOAuthConsumerSecret( "oWjTRFDqZ41dP0CGKNquuV9oWHOC2611Tk9b27MY3HIeDrBOIdhy");
			
			Configuration configuration = builder.build();

			TwitterFactory factory = new TwitterFactory(configuration);
			Twitter twitter = factory.getInstance();      

			AccessToken accessToken = loadAccessToken();

			twitter.setOAuthAccessToken(accessToken);

			Status status = twitter.updateStatus("Olá Twitter!");
			System.out.println("Tweet postado com sucesso! [" + status.getText() + "].");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static AccessToken loadAccessToken(){
		String token = "723498836417720321-NX0j611RWICxNmhl4PkC1uhRGL210WeBk"; 
		String tokenSecret = "Erqva0nQRfWP8XC9QS400ictq0N1171QaxsPMkhozDc9P5X"; 
		return new AccessToken(token, tokenSecret);
	}

}