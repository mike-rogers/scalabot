package org.cansado.scalabot.twitter

import java.io._
import twitter4j._
import twitter4j.http._

object Configurator {
  def main(args: Array[String]) = {
    val twitter: Twitter = new TwitterFactory().getInstance();
    val br: BufferedReader = new BufferedReader(new InputStreamReader(java.lang.System.in));

    println("")
    println("")

    println("Please visit http://twitter.com/apps/new to register this application.")

    println("")
    println("")

    println("Please enter your consumerKey:")

    val consumerKey: String = br.readLine()

    println("")
    println("")

    println("Please enter your consumerSecret:")

    val consumerSecret: String = br.readLine()

    twitter.setOAuthConsumer(consumerKey, consumerSecret);
    val requestToken: RequestToken = twitter.getOAuthRequestToken();

    println("")
    println("")

    println("Open the following URL and grant access to your account:");
    println(requestToken.getAuthorizationURL());

    println("")
    println("")

    println("Enter the PIN from the website:");
    val pin: String = br.readLine();

    val accessToken: AccessToken = twitter.getOAuthAccessToken(requestToken, pin);

    println("")
    println("")

    println("Your tokenKey: " + accessToken.getToken())
    println("Your tokenSecret: " + accessToken.getTokenSecret())

    println("")
    println("")

    println("Thanks for using the Twitter Configurator!")
  }
}
