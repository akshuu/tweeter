package com.akshatjain.codepath.tweeter.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akshatjain.codepath.tweeter.R;
import com.akshatjain.codepath.tweeter.restclienttemplate.RestApplication;
import com.akshatjain.codepath.tweeter.restclienttemplate.TwitterClient;
import com.akshatjain.codepath.tweeter.utils.Constants;
import com.akshatjain.codepath.tweeter.utils.Utils;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTweetComposed} interface
 * to handle interaction events.
 */
public class ComposeFragment extends DialogFragment {

    private OnTweetComposed mListener;

    @BindView(R.id.imgCancel)
    ImageView imgCancel;

    @BindView(R.id.imgProfile)
    ImageButton imgProfile;

    @BindView(R.id.txtTweet)
    EditText txtTweet;

    @BindView(R.id.btnTweet)
    Button btnTweet;

    @BindView(R.id.txtCount)
    TextView txtCount;

    private long replyId;
    private TwitterClient twitterClient;

    private int MAX_CHARS = 140;

    public ComposeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Material_NoActionBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compose, container, false);
        ButterKnife.bind(this, view);

        replyId = -1;
        twitterClient = RestApplication.getRestClient();
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        txtTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int tweetCnt = MAX_CHARS - (start + count);
                if(tweetCnt < 0){
                    txtCount.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    btnTweet.setEnabled(false);
                }else{
                    txtCount.setTextColor(getResources().getColor(android.R.color.black));
                    btnTweet.setEnabled(true);
                }
                txtCount.setText("" + tweetCnt);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Bundle args = getArguments();
        if(args != null && args.containsKey("Name")){
            txtTweet.setText("@" + args.getString("Name")+" ");
            txtTweet.setSelection(args.getString("Name").length()+2);
            btnTweet.setText("Reply");
            replyId = args.getLong("id");
        }else{
            btnTweet.setText("Tweet");
        }

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(txtTweet.getText())) {
                    Snackbar.make(v, "Please enter something to tweet", Snackbar.LENGTH_LONG)
                            .setAction("OK", null).show();

//                    Toast.makeText(getActivity(), "Please enter something to tweet", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Utils.isNetworkAvailable(getActivity())) {
                    String tweet = txtTweet.getText().toString();
                        twitterClient.postNewTweet(tweet, replyId, new TextHttpResponseHandler() {

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Log.d(Constants.TAG, "Failed Response code== " + statusCode + ", string = " + responseString);
                                Toast.makeText(getActivity(), "Error posting tweet. Please try again..." + statusCode, Toast.LENGTH_LONG).show();
                                return;
                            }


                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String res) {
                                Log.d(Constants.TAG, "Success Response code== " + statusCode + ", string = " + res);
                                if (mListener != null) {
                                    mListener.onTweetPosted();
                                }
                                dismiss();
                            }

                        });
                } else {
                    Snackbar.make(v, "No Internet connection. Please try again...", Snackbar.LENGTH_LONG)
                            .setAction("OK", null).show();

                }
            }
        });
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTweetComposed) {
            mListener = (OnTweetComposed) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTweetComposed");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTweetComposed {
        void onTweetPosted();
    }
}
