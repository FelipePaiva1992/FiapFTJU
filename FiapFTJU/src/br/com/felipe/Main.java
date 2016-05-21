package br.com.felipe;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import twitter4j.Status;

public class Main {

	/**
	 * @param args
	 */

	public static void main(String[] args) {

		try {
			
			//Data atual menos 1 semana
			LocalDate semanaPassada = LocalDate.now().minusWeeks(1);
			
			
			DateTimeFormatter formatoDataTwitter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			DateTimeFormatter formatoDataPost = DateTimeFormatter.ofPattern("dd/MM");

			TwitterUtils twitterUtils = new TwitterUtils();
			
			//obtem todos os posts do periodo instanciado acima com a hashtag #java9
			List<Status> tweets = twitterUtils.obterListaTweets(semanaPassada.format(formatoDataTwitter), "#java9");


			//A cada interação ele compara a data de inicio da pesquisa (semana passada) e agrupa os posts e os contabiliza
			//apos a contabilização ele soma um dia a data inicial até que ela seja igual a do ultimo dia da consulta
			do {
				int contadorTDia = 0;
				int contadorRDia = 0;
				int contadorFDia = 0;
				for (Status tweet : tweets) {
					//conversão de java.util.Date para LocalDate
					LocalDate dataTweet = tweet.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					//Compara os dias entre a data do post e a data da interação
					if (ChronoUnit.DAYS.between(dataTweet, semanaPassada) == 0) {
						contadorTDia++;
						contadorRDia += tweet.getRetweetCount();
						contadorFDia += tweet.getFavoriteCount();
					}
				}
				
				twitterUtils.publicarMensagem(" - " + semanaPassada.format(formatoDataPost) + " T:" + contadorTDia + " RT:" + contadorRDia + " F:" + contadorFDia + " @michelpf");
				System.out.println(" - " + semanaPassada.format(formatoDataPost) + " T:" + contadorTDia + " RT:" + contadorRDia + " F:" + contadorFDia + " @michelpf");
				
				//Soma um dia a data de interação e zera os contadores
				semanaPassada = semanaPassada.plusDays(1);
				contadorTDia = 0;
				contadorRDia = 0;
				contadorFDia = 0;
			} while (ChronoUnit.DAYS.between(semanaPassada, LocalDate.now()) >= 0);
			
			//Usando lambda ordeno a lista utilizando o nome do usuario como paramentro para o compateTo
			Collections.sort(tweets, (t1, t2) -> t1.getUser().getScreenName().compareTo(t2.getUser().getScreenName()));

			twitterUtils.publicarMensagem("Ordenando a lista pelo nome do usuario : Primeiro nome " + obterPrimeiro(tweets).getUser().getName() + " / " + "Ultimo nome " + obterUltimo(tweets).getUser().getName() + " @michelpf");
			System.out.println("Ordenando a lista pelo nome do usuario : Primeiro nome = " + obterPrimeiro(tweets).getUser().getName() + " / " + "Ultimo nome = " + obterUltimo(tweets).getUser().getName() + " @michelpf");

			//Usando lambda ordeno a lista utilizando a data do post como paramentro para o compateTo
			Collections.sort(tweets, (t1, t2) -> t1.getCreatedAt().compareTo(t2.getCreatedAt()));

			
			twitterUtils.publicarMensagem("Ordenando a lista por data : Menor Data = " + obterPrimeiro(tweets).getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatoDataPost) + " / " + "Maior Data = " + obterUltimo(tweets).getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatoDataPost) + " @michelpf");
			System.out.println("Ordenando a lista por data : Menos Recente = " + obterPrimeiro(tweets).getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatoDataPost) + " / " + "Mais Recente = " + obterUltimo(tweets).getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatoDataPost) + " @michelpf");
			
			
			twitterUtils.publicarMensagem("Termino da Atividade Final, do professor @michelpf");
			System.out.println("Termino da Atividade Final, do professor @michelpf com a hashtag #java9");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//Metodo que recebe uma lista de Status e utilizndo lambda obtem o primeiro Status da lista
	public static Status obterPrimeiro(List<Status> lista) {
		return lista.stream().findFirst().get();
	}

	//Metodo que recebe uma lista de Status e utilizndo lambda obtem o ultimo Status da lista
	public static Status obterUltimo(List<Status> lista) {
		return lista.stream().reduce((a, b) -> b).get();
	}

}