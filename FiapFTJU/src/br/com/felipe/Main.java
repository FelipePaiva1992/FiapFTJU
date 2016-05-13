package br.com.felipe;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import twitter4j.Query;
import twitter4j.QueryResult;
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

			builder.setOAuthConsumerKey("TZ6yn1rkOnOWvRJM8111aF1sgS0");
			builder.setOAuthConsumerSecret("oWjTRFDqZ4111dP0CGKNquuV9oWHOC26Tk9b27MY3HIeDrBOIdhy");

			Configuration configuration = builder.build();

			TwitterFactory factory = new TwitterFactory(configuration);
			Twitter twitter = factory.getInstance();

			AccessToken accessToken = loadAccessToken();

			twitter.setOAuthAccessToken(accessToken);

			LocalDate semanaPassada = LocalDate.now().minusWeeks(1);
			DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			DateTimeFormatter formatoDataPrint = DateTimeFormatter.ofPattern("dd-MM-yyy");
			Query query = new Query("#java9");
			query.setSince(semanaPassada.format(formatoData));
			QueryResult result;
			List<Status> list = new ArrayList<>();
			result = twitter.search(query);
			while (result.hasNext()) {
				list.addAll(result.getTweets());
				query = result.nextQuery();
				result = twitter.search(query);
			}
			
			System.out.println("total " +  list.size());
			
			int totalRetweet = 0;
			for(Status tweet : list){
				totalRetweet += tweet.getRetweetCount();
			}
			System.out.println("Retweet " + totalRetweet);
			
			System.out.println("--------------");
			
			do{
				int contadorDia = 0;
				int contadorRDia = 0;
				int contadorFDia = 0;
				for(Status tweet : list){
					LocalDate dataTweet = tweet.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					if(ChronoUnit.DAYS.between(dataTweet, semanaPassada) == 0){
						contadorDia++;
						contadorRDia += tweet.getRetweetCount();
						contadorFDia += tweet.getFavoriteCount();
					}
				}
				System.out.println("Tweet do dia " + semanaPassada.format(formatoDataPrint) + " é " + contadorDia);
				System.out.println("Retweet do dia " + semanaPassada.format(formatoDataPrint) + " é " + contadorRDia);
				System.out.println("Favoritação do dia " + semanaPassada.format(formatoDataPrint) + " é " + contadorFDia);
				System.out.println("--------------");
				semanaPassada = semanaPassada.plusDays(1);
				contadorDia = 0;
				contadorRDia = 0;
				contadorFDia = 0;
			}while(ChronoUnit.DAYS.between(semanaPassada, LocalDate.now()) >= 0);
			
			System.out.println("--------------");
			Collections.sort(list,(t1, t2) -> t1.getUser().getScreenName().compareTo(t2.getUser().getScreenName()));
			System.out.println("Ordenados pelo nome do usuario : ");
			Status statusPrimeiroNome = list
					.stream()
					.findFirst()
					.get();
			
			System.out.println("Primeiro nome " + statusPrimeiroNome.getUser().getName());
			
			
			Status statusUltimoNome = list
					.stream()
					.reduce((a, b) -> b)
					.get();
			
			System.out.println("Ultimo nome " + statusUltimoNome.getUser().getName());
			
			System.out.println("--------------");
			Collections.sort(list,(t1, t2) -> t1.getCreatedAt().compareTo(t2.getCreatedAt()));
			System.out.println("Ordenados por data: ");
			
			Status statusMenorData = list
					.stream()
					.findFirst()
					.get();
			
			Status statusMaiorData = list
					.stream()
					.reduce((a, b) -> b)
					.get();
			
			System.out.println("Menor Data " + statusMenorData.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatoDataPrint));
			System.out.println("Maior Data " + statusMaiorData.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatoDataPrint));
			
			//Status status = twitter.updateStatus("Termino da Atividade Final, do professor @michelpf");
			//System.out.println("Tweet postado com sucesso! [" + status.getText() + "].");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static AccessToken loadAccessToken() {
		String token = "72349883641711720321-NX0j6RWICxNmhl4PkC1uhRGL210WeBk";
		String tokenSecret = "Erqva0nQRfWP8XC9QS41100ictq0N71QaxsPMkhozDc9P5X";
		return new AccessToken(token, tokenSecret);
	}

}