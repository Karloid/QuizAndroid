package com.example.Game24Hours;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: krld
 * Date: 24.12.12
 * Time: 22:21
 * To change this template use File | Settings | File Templates.
 */
public class SettingsActivity extends QuizActivity {
    static final int DATE_DIALOG_ID = 0;
    static final int PASSWORD_DIALOG_ID = 1;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        final SharedPreferences prefs = getSharedPreferences(GAME_PREFERENCES, Context.MODE_PRIVATE);
        //   prefs.edit().clear().commit();
        Button pickDate = (Button) findViewById(R.id.button_dob);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        final EditText nickname = (EditText) findViewById(R.id.editText_nickname);
        final EditText email = (EditText) findViewById(R.id.editText_email);
        if (prefs.contains(GAME_PREFERENCES_NICKNAME)) {
            nickname.setText(prefs.getString(GAME_PREFERENCES_NICKNAME, ""));
        }
        if (prefs.contains(GAME_PREFERENCES_EMAIL)) {
            email.setText(prefs.getString(GAME_PREFERENCES_EMAIL, ""));
        }

        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                prefs.edit().putString(GAME_PREFERENCES_NICKNAME, nickname.getText().toString()).commit();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                prefs.edit().putString(GAME_PREFERENCES_EMAIL, email.getText().toString()).commit();
            }
        });
        Button passwordButton = (Button) findViewById(R.id.button_password);
        passwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(PASSWORD_DIALOG_ID);
              //  Toast.makeText(SettingsActivity.this, "TODO: Launch create password dialog", Toast.LENGTH_LONG).show();
            }
        });
        final Spinner gender = (Spinner) findViewById(R.id.spinner_gender);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
        gender.setSelection(0);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                prefs.edit().putInt(GAME_PREFERENCES_GENDER, i).commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("game24:", String.valueOf(getSharedPreferences(GAME_PREFERENCES, Context.MODE_PRIVATE).getAll()));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final SharedPreferences prefs = getSharedPreferences(GAME_PREFERENCES, Context.MODE_PRIVATE);
        final TextView dob = (TextView) findViewById(R.id.date_status);
        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        Time dateOfBirth = new Time();
                        dateOfBirth.set(dayOfMonth, monthOfYear, year);
                        long dtDob = dateOfBirth.toMillis(true);
                        dob.setText(DateFormat.format("MMMM dd, yyyy", dtDob));
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putLong(GAME_PREFERENCES_DOB, dtDob);
                        editor.commit();

                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                }, 0, 0, 0);
                return dateDialog;
            case PASSWORD_DIALOG_ID:
                //build dialog
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.password_dialog, (ViewGroup) findViewById(R.id.root));
                final EditText pwd1 = (EditText) layout.findViewById(R.id.editText_pwd1);
                final EditText pwd2 = (EditText) layout.findViewById(R.id.editText_pwd2);
                final TextView error = (TextView) layout.findViewById(R.id.textView_pwdStatus);
                pwd2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String strPass1 = pwd1.getText().toString().toString();
                        String strPass2 = pwd2.getText().toString().toString();
                        if (strPass1.equals(strPass2)) {
                            error.setText("ok");
                        } else {
                            error.setText("don't match");
                        }
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(layout);
                builder.setTitle("Set password");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextView passwordInfo = (TextView) findViewById(R.id.textView_pwdStatus);
                        String strPassword1 = pwd1.getText().toString();
                        String strPassword2 = pwd2.getText().toString();
                        if (strPassword1.equals(strPassword2)) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(GAME_PREFERENCES_PASSWORD, strPassword1);
                            editor.commit();
                        } else {
                            Log.d(DEBUG_TAG, "password do not match");
                        }
                        SettingsActivity.this.removeDialog(PASSWORD_DIALOG_ID);
                    }
                });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SettingsActivity.this.removeDialog(PASSWORD_DIALOG_ID);
                    }
                });
                AlertDialog passwordDialog = builder.create();
                return passwordDialog;

        }

        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        final SharedPreferences prefs = getSharedPreferences(GAME_PREFERENCES, Context.MODE_PRIVATE);
        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog dateDialog = (DatePickerDialog) dialog;
                int iDay, iMonth, iYear;
                if (prefs.contains(GAME_PREFERENCES_DOB)) {
                    long msBirthDate = prefs.getLong(GAME_PREFERENCES_DOB, 0l);
                    Time dateOfBirth = new Time();
                    dateOfBirth = new Time();
                    dateOfBirth.set(msBirthDate);
                    iDay = dateOfBirth.monthDay;
                    iMonth = dateOfBirth.month;
                    iYear = dateOfBirth.year;
                } else {
                    Calendar cal = Calendar.getInstance();
                    iDay = cal.get(Calendar.DAY_OF_MONTH);
                    iMonth = cal.get(Calendar.MONTH);
                    iYear = cal.get(Calendar.YEAR);
                }
                dateDialog.updateDate(iYear, iMonth, iDay);
                return;
        }
    }
}