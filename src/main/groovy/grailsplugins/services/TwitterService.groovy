package grailsplugins.services

import grailsplugins.config.TwitterConfig
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import jakarta.inject.Singleton
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

@Slf4j
@Singleton
@CompileStatic
class TwitterService {

    private final Twitter twitter

    TwitterService(TwitterConfig config) {
        if(config.enabled) {
            ConfigurationBuilder cb = new ConfigurationBuilder()
                    .setDebugEnabled(config.debugEnabled)
                    .setOAuthConsumerKey(config.consumerKey)
                    .setOAuthConsumerSecret(config.consumerSecret)
                    .setOAuthAccessToken(config.accessToken)
                    .setOAuthAccessTokenSecret(config.accessTokenSecret)
            TwitterFactory tf = new TwitterFactory(cb.build())
            this.twitter = tf.getInstance()
        }
        else {
            twitter = null
        }
    }

    void tweet(String status) {
        if (twitter) {
            try {
                log.info 'tweeting: {}', status
                twitter.updateStatus status
            } catch (e) {
                log.error 'failed to tweet {}', status
                e.printStackTrace()
            }
        }
    }
}
