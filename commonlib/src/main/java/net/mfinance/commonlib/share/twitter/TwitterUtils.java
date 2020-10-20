package net.mfinance.commonlib.share.twitter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;

import androidx.core.content.FileProvider;

public class TwitterUtils {


    public static void init(Context context) {
        Twitter.initialize(context);
    }



    public static void login() {
//        TwitterAuthClient authClient = new TwitterAuthClient();
//        authClient.requestEmail(session, new Callback<String>() {
//            @Override
//            public void success(Result<String> result) {
//                // Do something with the result, which provides the email address
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                // Do something on failure
//            }
//        });
    }

    /**
     * https://github.com/twitter-archive/twitter-kit-android/wiki/Compose-Tweets
     */
    public static void share(Activity activity) {
//        Uri imageUri = FileProvider.getUriForFile(MainActivity.this,
//                BuildConfig.APPLICATION_ID + ".file_provider",
//                new File("/path/to/image"));
//
//        TweetComposer.Builder builder = new TweetComposer.Builder(activity)
//                .text("just setting up my Twitter Kit.")
//                .image(imageUri);
//        builder.show();
    }
}
