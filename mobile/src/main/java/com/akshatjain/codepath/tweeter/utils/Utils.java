package com.akshatjain.codepath.tweeter.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
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

    public static String ParseTweet(String rawTweet, String url)
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
                formattedTweet = matcherTweet.replaceAll("<a href='" + (url == null ? match : url)  + "'>" + match + "</a>");
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

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String consiseFormat = getMoreConsiseString(relativeDate);
        return consiseFormat;
    }

    private static String getMoreConsiseString(String relativeDate) {
        String returnDate = "";
        if(relativeDate == null)
            return "";

        int indx = relativeDate.indexOf(" ago");
        if(indx != -1)
            relativeDate = relativeDate.substring(0,indx);

        if(relativeDate.contains("second")){
            indx = relativeDate.indexOf("second");
            int value = 1;

            try{
                value = Integer.parseInt(relativeDate.substring(0,indx-1));
            }catch (NumberFormatException nfe){}
            returnDate = value + "s";
        }else  if(relativeDate.contains("minute")){
            indx = relativeDate.indexOf("minute");
            int value = 1;

            try{
                value = Integer.parseInt(relativeDate.substring(0,indx-1));
            }catch (NumberFormatException nfe){}
            returnDate = value + "m";
        }else  if(relativeDate.contains("hour")){
            indx = relativeDate.indexOf("hour");
            int value = 1;

            try{
                value = Integer.parseInt(relativeDate.substring(0,indx-1));
            }catch (NumberFormatException nfe){}
            returnDate = value + "h";
        } else if(relativeDate.contains("day")){
            indx = relativeDate.indexOf("day");
            int value = 1;

            try{
                value = Integer.parseInt(relativeDate.substring(0,indx-1));
            }catch (NumberFormatException nfe){}
            returnDate = value + "d";
        }  else if(relativeDate.contains("year")){
            indx = relativeDate.indexOf("year");
            int value = 1;

            try{
                value = Integer.parseInt(relativeDate.substring(0,indx-1));
            }catch (NumberFormatException nfe){}
            returnDate = value + "y";
        }

        Log.d(Constants.TAG,"Consise date ==" + returnDate);
        return returnDate;
    }
}
