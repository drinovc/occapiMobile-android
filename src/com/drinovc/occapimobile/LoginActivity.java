package com.drinovc.occapimobile;

import helpers.App;
import helpers.C;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import jsonentities.KpiGroup;
import jsonentities.User;

import com.fasterxml.jackson.databind.ObjectMapper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	public static final String EXTRA_EMAIL = "com.drinovc.extra.EMAIL";
	public static final String EXTRA_PASS = "com.drinovc.extra.PASS";

	private UserLoginTask mAuthTask = null;
	private SharedPreferences sharedPref;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

		// Set up the login form.
//		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
//		mEmailView.setText(mEmail);

//		mPassword = getIntent().getStringExtra(EXTRA_PASS);
		mPasswordView = (EditText) findViewById(R.id.password);
//		mPasswordView.setText(mPassword);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		boolean rememberMe = sharedPref.getBoolean(SettingsActivity.KEY_PREF_REMEMBER_ME, true);
		boolean autoLogin = sharedPref.getBoolean(SettingsActivity.KEY_PREF_AUTO_LOGIN, false);
		String email = sharedPref.getString(SettingsActivity.KEY_EMAIL, "");
		String pass = sharedPref.getString(SettingsActivity.KEY_PASS, "");
		
		if(rememberMe) {
			mEmailView.setText(email);
			if(autoLogin) {
				mPasswordView.setText(pass);
				attemptLogin();
			}
		}
		else {
			mEmailView.setText("");
			mPasswordView.setText("");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
	
		//showProgress(false);
		
		boolean rememberMe = sharedPref.getBoolean(SettingsActivity.KEY_PREF_REMEMBER_ME, true);
		boolean autoLogin = sharedPref.getBoolean(SettingsActivity.KEY_PREF_AUTO_LOGIN, false);
		
		if(rememberMe) {
			if(!autoLogin) {
				mPasswordView.setText("");
			}
		}
		else {
			mEmailView.setText("");
			mPasswordView.setText("");
		}
	}
	
	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} 

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, String, KpiGroup[]> {
		String token;
		
		@Override
		protected KpiGroup[] doInBackground(Void... params) {
			try {
				URL url = new URL(C.apiURL + "login/" + mEmail + "/" + mPassword);
				ObjectMapper mapper = new ObjectMapper();
				User user = mapper.readValue(new InputStreamReader(url.openStream()), User.class);
				
				if(user.getStatus() && user.getToken() != null && !user.getToken().isEmpty() && !user.getToken().equals("null")) {
					// store user
					boolean rememberMe = sharedPref.getBoolean(SettingsActivity.KEY_PREF_REMEMBER_ME, true);
					if(rememberMe) {
						SharedPreferences.Editor editor = sharedPref.edit();
						editor.putString(SettingsActivity.KEY_EMAIL, mEmail);
						editor.putString(SettingsActivity.KEY_PASS, mPassword);
						editor.commit();
					}
					
					// store user data
					((App)getApplication().getApplicationContext()).setUser(user);
					
					// get kpi groups
					publishProgress("Loading KPI Groups");
					url = new URL(C.apiURL + "kpigroup/user/" + user.getToken());
					mapper = new ObjectMapper();
					return mapper.readValue(new InputStreamReader(url.openStream()), KpiGroup[].class);
				}
			} 
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onProgressUpdate(String... progress) {        
	    	mLoginStatusMessageView.setText(progress[0]);
	    }

		@Override
		protected void onPostExecute(final KpiGroup[] result) {			
			if (result != null) {
				if(result.length > 0) {
					ArrayList<KpiGroup> kpiGroups = new ArrayList<KpiGroup>();
					for(KpiGroup kpiGroup : result) {
						kpiGroups.add(kpiGroup);
					}
					((App)getApplicationContext()).setKpiGroups(kpiGroups);
					
					// show Kpi Groups view
					/*
					Intent intent = new Intent(LoginActivity.this, KpiGroupsActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.right_to_left, R.anim.right_to_left_exit);
					*/
					
					Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
					startActivity(intent);
				}
				else {
					Toast.makeText(getApplicationContext(), "No Kpi Groups", Toast.LENGTH_SHORT).show();
				}
			} 
			else {
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}			
			mAuthTask = null;
			showProgress(false);
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
