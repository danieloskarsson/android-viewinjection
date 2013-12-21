package com.danieloskarsson.android.viewinjection.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class SampleActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

	/**
	 * @author Daniel Oskarsson (daniel.oskarsson@gmail.com)
	 */
	public static class PlaceholderFragment extends Fragment {

		private TextView textView;
		private ImageView imageView;

		private int httpResponseCode;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_sample, container, false);

			textView = (TextView) rootView.findViewById(R.id.textView);
			imageView = (ImageView) rootView.findViewById(R.id.imageView);

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						final URLConnection urlConnection = new URL("http://example.com/404").openConnection();
						final HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
						PlaceholderFragment.this.httpResponseCode = httpUrlConnection.getResponseCode();

						PlaceholderFragment.this.getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								onResponse(textView, imageView);
							}
						});

					} catch (final Throwable throwable) {
						Log.e(PlaceholderFragment.class.getCanonicalName(), Log.getStackTraceString(throwable));
					}
				}
			}).start();

			return rootView;
		}

		/**
		 * This method will be invoked after the HTTP response code is set.
		 */
		public void onResponse(TextView textView, ImageView imageView) {
			textView.setText("HTTP Response Code: " + httpResponseCode);
			imageView.setImageResource(R.drawable.ic_launcher);
		}
	}

}
