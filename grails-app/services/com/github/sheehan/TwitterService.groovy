package com.github.sheehan

import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.util.logging.Slf4j
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

@Slf4j
class TwitterService implements GrailsConfigurationAware {

    private Twitter twitter

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

    @Override
    void setConfiguration(Config config) {
        if (config.twitter.enabled) {
            ConfigurationBuilder cb = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(config.twitter.consumerKey)
                .setOAuthConsumerSecret(config.twitter.consumerSecret)
                .setOAuthAccessToken(config.twitter.accessToken)
                .setOAuthAccessTokenSecret(config.twitter.accessTokenSecret)
            TwitterFactory tf = new TwitterFactory(cb.build())
            this.twitter = tf.getInstance()
        }
    }
}
