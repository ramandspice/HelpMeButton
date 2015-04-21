package com.helme.helpmebutton.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.helme.helpmebutton.R;
import com.helme.helpmebutton.application.AppState;
import com.helme.helpmebutton.rest.PostClient;
import com.helme.helpmebutton.rest.requests.LoginRequest;
import com.helme.helpmebutton.rest.requests.SignUpRequest;
import com.helme.helpmebutton.rest.responses.LoginFailedResponse;
import com.helme.helpmebutton.rest.responses.LoginResponse;
import com.helme.helpmebutton.rest.responses.WebResponse;
import com.helme.helpmebutton.util.GSON;

public class SignUpActivity extends Activity {

    private TextView mFirstNameView;
    private TextView mLastNameView;
    private TextView mEmailView;
    private TextView mPhoneView;
    private TextView mDobView;
    private TextView mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private UserSignUpTask mSignUpTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirstNameView = (TextView) findViewById(R.id.signup_form_first_name);
        mLastNameView = (TextView) findViewById(R.id.signup_form_last_name);
        mEmailView = (TextView) findViewById(R.id.signup_form_email);
        mPhoneView = (TextView) findViewById(R.id.signup_form_phone);
        mDobView = (TextView) findViewById(R.id.signup_form_dob);
        mPasswordView= (TextView) findViewById(R.id.signup_password);
        mProgressView = findViewById(R.id.signup_progress);
        mLoginFormView = findViewById(R.id.signup_form_scrollview);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.signup || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        Button signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void attemptSignUp() {
        if (mSignUpTask != null) {
            return;
        }

        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mEmailView.setError(null);
        mPhoneView.setError(null);
        mDobView.setError(null);
        mPasswordView.setError(null);

        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String phoneNumber = mPhoneView.getText().toString();
        String dob = mDobView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(firstName)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mSignUpTask = new UserSignUpTask(firstName, lastName, email, phoneNumber, dob, password);
            mSignUpTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private class UserSignUpTask extends AsyncTask<Void, Void, WebResponse> {

        private final String mFirstName;
        private final String mLastName;
        private final String mDob;
        private final String mPhoneNumber;
        private final String mEmail;
        private final String mPassword;

        UserSignUpTask(String firstName, String lastName, String email, String phoneNumber,
                       String dob, String password) {
            mFirstName = firstName;
            mLastName = lastName;
            mDob = dob;
            mPhoneNumber = phoneNumber;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected WebResponse doInBackground(Void... params) {
            SignUpRequest request = new SignUpRequest(mFirstName, mLastName, mEmail, mPhoneNumber, mDob, mPassword);
            PostClient client = new PostClient(request.getParams(), SignUpRequest.getMethodName());
            client.executeRequest();

            return new WebResponse(client.getResponseString(), client.getStatuscode());
        }

        @Override
        protected void onPostExecute(WebResponse webResponse) {
            mSignUpTask = null;
            showProgress(false);

            if (webResponse.getResponseCode() == 200) {
                try
                {
                    LoginResponse loginResponse = GSON.getInstance().fromJson(webResponse.getResponseEntity(), LoginResponse.class);
                    AppState.getInstance().setAccessToken(loginResponse.getAccessToken());
                    AppState.getInstance().setRefreshToken(loginResponse.getRefreshToken());

//                    proceedLogin();
                }
                catch (Exception e)
                {
                    mPasswordView.setError(getString(R.string.error_login_failed));
                    mPasswordView.requestFocus();
                    e.printStackTrace();
                }
            } else if(webResponse.getResponseCode() == 401) {
                try
                {
                    LoginFailedResponse failedResponse = GSON.getInstance().fromJson(webResponse.getResponseEntity(), LoginFailedResponse.class);
                    mPasswordView.setError(failedResponse.getErrorDescription());
                    mPasswordView.requestFocus();
                }
                catch (Exception e) {
                    mPasswordView.setError(getString(R.string.error_login_failed));
                    mPasswordView.requestFocus();
                    e.printStackTrace();
                }
            } else {
                mPasswordView.setError(getString(R.string.error_login_failed));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mSignUpTask = null;
            showProgress(false);
        }
    }
}























