package com.akshatjain.codepath.tweeter.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by akshatjain on 7/27/16.
 */
public class Utils {

    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static String ParseTweet(String rawTweet)
    {
        String regex = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"; // matches <http://google.com>

        Log.d(Constants.TAG,"Raw Tweet == " + rawTweet);
//        Pattern link = Pattern.compile("http(s)?:\\/\\/([\\w+?.\\w+])+([a-zA-Z0-9\\~!\\@#;\\$\\%\\^\\&*()_-\\=+\\/\\?.:\\;\\'\\,]*)?");
        Pattern link = Pattern.compile(regex);
        Pattern screenName = Pattern.compile("\\w+");
        Pattern hashTag = Pattern.compile("#\\w+");

        Matcher matcherTweet = link.matcher(rawTweet);
        Log.d(Constants.TAG,"Tweet match? == " + matcherTweet.find());
        String formattedTweet = rawTweet;
        if(matcherTweet.find()) {
            int totalMatch = matcherTweet.groupCount();
            Log.d(Constants.TAG,"Tweet matchcount? == " + totalMatch);

            for (int i = 0; i < totalMatch; i++) {
                String match = matcherTweet.group(i);
                Log.d(Constants.TAG,"Tweet m = " + match);
                formattedTweet = matcherTweet.replaceAll("<a href='" + match + "'>" + match + "</a>");
            }
        }
       // String formattedTweet = matcherTweet.replaceAll("<a href='" + matcherTweet.toString() + "'>" + matcherTweet.toString() + "</a>");

//        formattedTweet = screenName.Replace(formattedTweet, delegate(Match m)
//        {
//            string val = m.Value.Trim('@');
//            return string.Format("@<a href='http://twitter.com/{0}'>{1}</a>", val, val);
//        });
//
//        formattedTweet = hashTag.Replace(formattedTweet, delegate(Match m)
//        {
//            string val = m.Value;
//            return string.Format("<a href='http://twitter.com/#search?q=%23{0}'>{1}</a>", val, val);
//        });
        Log.d(Constants.TAG,"Tweet == " + formattedTweet);
        return formattedTweet;
    }
}
