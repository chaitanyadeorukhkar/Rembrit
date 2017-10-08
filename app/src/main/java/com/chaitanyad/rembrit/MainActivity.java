package com.chaitanyad.rembrit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    public static DatabaseHelper db;
    public static EditText remtext;
    Button btnset;
    Button btnat;
    Button btnunt;
    Button btnwhen;
    Button btnbef;
    Button btnaft;
    public boolean isFirstStart;
    GridLayout gltimetext;
    long notifId;
    TextView noupcoming;
    static MainActivity mainActivity;
    ListView upcomingList;
    SimpleCursorAdapter cursorAdapter;
    CustomAdapter customAdapter;
    Calendar calendar_when;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Rembrit");
        db = new DatabaseHelper(this);
        remtext = (EditText) findViewById(R.id.remtext);
        upcomingList = (ListView) findViewById(R.id.upcomingListView);
        noupcoming = (TextView) findViewById(R.id.txtNoupcoming);
        btnset = (Button) findViewById(R.id.btnset);
        btnat = (Button) findViewById(R.id.btnat);
        btnbef = (Button) findViewById(R.id.btnbef);
        btnaft = (Button) findViewById(R.id.btnaft);
        btnwhen = (Button) findViewById(R.id.btnwhen);
        btnunt = (Button) findViewById(R.id.btnunt);
        gltimetext = (GridLayout) findViewById(R.id.gltimetext);
        calendar_when = Calendar.getInstance();
        remtext.setText("");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, FirstRunActivity.class);
                    startActivity(i);
                    //  Make a new preferences editor

                }
            }
        });

// Start the thread
        t.start();
        remtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    gltimetext.setVisibility(View.VISIBLE);
                } else
                    gltimetext.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    gltimetext.setVisibility(View.GONE);
                else
                    gltimetext.setVisibility(View.VISIBLE);
            }
        });
        btnat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Dialogat dialogat = new Dialogat();
                dialogat.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                dialogat.show(fm, "a");
            }
        });

        btnunt.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          setReminder(v.getContext(), Calendar.getInstance().getTimeInMillis(), remtext.getText().toString());

                                          db.insertData(remtext.getText().toString(), Calendar.getInstance().getTimeInMillis()); // INSERT Reminder into DB
                                          remtext.setText("");
                                          populateListView();
                                      }
                                  }
        );


        btnbef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Dialogbefore dialogbefore = new Dialogbefore();
                dialogbefore.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                dialogbefore.show(fm, "a");
            }
        });


        btnwhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Dialogwhen dialogwhen = new Dialogwhen();
                dialogwhen.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                dialogwhen.show(fm, "a");
            }
        });

        btnaft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Dialogafter dialogafter = new Dialogafter();
                dialogafter.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                dialogafter.show(fm, "a");
            }
        });

        populateListView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), FirstRunActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        populateListView();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        populateListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    @Override
    protected void onStop() {
        getInstance().finish();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public static class Dialogat extends DialogFragment {
        public int globHour, globMin, globYear, globMonth, globDay;
        ;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().setTitle("Set a reminder");
            View rootView = inflater.inflate(R.layout.timedatepicker, container, false);

            final Spinner attimespinner = (Spinner) rootView.findViewById(R.id.attimespinner);
            final ArrayList<String> attimespinnerarray = new ArrayList<>();
            attimespinnerarray.add("Pick a time...");
            final ArrayAdapter attimeadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, attimespinnerarray);
            attimeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            attimespinner.setAdapter(attimeadapter);


            final Spinner atdatespinner = (Spinner) rootView.findViewById(R.id.atdatespinner);
            final ArrayList<String> atdatespinnerarray = new ArrayList<>();
            //atdatespinnerarray.add("Select date");
            atdatespinnerarray.add("Today");
            atdatespinnerarray.add("Tomorrow");
            atdatespinnerarray.add("Custom");
            final ArrayAdapter atdateadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, atdatespinnerarray);
            atdateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            atdatespinner.setAdapter(atdateadapter);

            final Calendar calendar = Calendar.getInstance();
            final Calendar calendar2 = Calendar.getInstance(); //for converting into milliseconds
            attimespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getSelectedItem().toString().equals("Pick a time...")) {
                        TimePickerDialog timePickerDialog;
                        timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                globHour = hourOfDay;
                                globMin = minute;
                                attimespinnerarray.clear();
                                String seltime = converttime(hourOfDay, minute);
                                attimeadapter.add(seltime);
                                attimeadapter.add("Pick a time...");
                                attimespinner.setSelection(0);


                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                        timePickerDialog.setTitle("Pick time");

                        timePickerDialog.show();
                        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                attimespinnerarray.clear();
                                attimespinnerarray.add("Select time");
                                attimespinnerarray.add("Pick a time...");
                                attimespinner.setSelection(0);
                            }
                        });
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            atdatespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getSelectedItem().toString().equals("Today")) {
                        globYear = calendar.get(Calendar.YEAR);
                        globMonth = calendar.get(Calendar.MONTH);
                        globDay = calendar.get(Calendar.DAY_OF_MONTH);
                        //Toast.makeText(getContext(),""+globDay+"-"+globMonth+"-"+globYear,Toast.LENGTH_LONG).show();

                    }
                    if (parent.getSelectedItem().toString().equals("Tomorrow")) {
                        GregorianCalendar gc = new GregorianCalendar();
                        gc.add(Calendar.DATE, 1);
                        globYear = gc.get(Calendar.YEAR);
                        globMonth = gc.get(Calendar.MONTH);

                        globDay = gc.get(Calendar.DAY_OF_MONTH);
                        //Toast.makeText(getContext(),""+globDay+"-"+globMonth+"-"+globYear,Toast.LENGTH_LONG).show();
                    }
                    if (parent.getSelectedItem().toString().equals("Custom")) {
                        DatePickerDialog datePickerDialog;
                        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                                globYear = year;
                                globMonth = monthOfYear;
                                globDay = dayOfMonth;
                                atdatespinnerarray.clear();
                                monthOfYear = monthOfYear + 1;
                                atdatespinnerarray.add(dayOfMonth + "-" + monthOfYear + "-" + year);
                                atdatespinnerarray.add("Today");
                                atdatespinnerarray.add("Tomorrow");
                                atdatespinnerarray.add("Custom");
                                atdatespinner.setSelection(0);
                                //Toast.makeText(getContext(),""+globDay+"-"+globMonth+"-"+globYear,Toast.LENGTH_LONG).show();


                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                atdatespinner.setSelection(0);
                            }
                        });
                        datePickerDialog.show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            Button btnatsave, btnatcancel;
            btnatsave = (Button) rootView.findViewById(R.id.btnatsave);
            btnatcancel = (Button) rootView.findViewById(R.id.btnatcancel);

            btnatcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });

            btnatsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendar2.set(globYear, globMonth, globDay, globHour, globMin, 0);// SET THE REMINDER CODE
                    long remtime = calendar2.getTimeInMillis();

                    /*SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - hh:mm");
                    String dateString = formatter.format(new Date(remtime));
                    Toast.makeText(getContext(),""+dateString,Toast.LENGTH_LONG).show();*/
                    Calendar testCalendar = Calendar.getInstance();
                    if (testCalendar.getTimeInMillis() < remtime) {

                        setReminder(v.getContext(), remtime, remtext.getText().toString());

                        db.insertData(remtext.getText().toString(), remtime); // INSERT Reminder into DB
                        getDialog().dismiss();
                        Toast.makeText(getContext(), "Reminder set!", Toast.LENGTH_SHORT).show();
                        remtext.setText("");
                        getInstance().populateListView();
                    } else {
                        Toast.makeText(getContext(), "Please choose a valid time.", Toast.LENGTH_SHORT).show();
                    }


                }
            });


            return rootView;
        }


    }


    public static class Dialogwhen extends DialogFragment {
        public int globHour, globMin, globYear, globMonth, globDay;


        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

            getDialog().setTitle("Set a reminder");
            View rootView = inflater.inflate(R.layout.whendialog_customview, container, false);


            final Spinner whendatespinner = (Spinner) rootView.findViewById(R.id.whendatespinner);
            final ArrayList<String> whendatespinnerarray = new ArrayList<>();
            //atdatespinnerarray.add("Select date");
            whendatespinnerarray.add("Today");
            whendatespinnerarray.add("Tomorrow");
            whendatespinnerarray.add("Custom");
            final ArrayAdapter whendateadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, whendatespinnerarray);
            whendateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            whendatespinner.setAdapter(whendateadapter);

            final Calendar calendar = Calendar.getInstance();
            final Calendar calendar2 = Calendar.getInstance(); //for converting into milliseconds

            whendatespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getSelectedItem().toString().equals("Today")) {
                        globYear = calendar.get(Calendar.YEAR);
                        globMonth = calendar.get(Calendar.MONTH);
                        globDay = calendar.get(Calendar.DAY_OF_MONTH);
                        //Toast.makeText(getContext(),""+globDay+"-"+globMonth+"-"+globYear,Toast.LENGTH_LONG).show();

                    }
                    if (parent.getSelectedItem().toString().equals("Tomorrow")) {
                        GregorianCalendar gc = new GregorianCalendar();
                        gc.add(Calendar.DATE, 1);
                        globYear = gc.get(Calendar.YEAR);
                        globMonth = gc.get(Calendar.MONTH);

                        globDay = gc.get(Calendar.DAY_OF_MONTH);
                        //Toast.makeText(getContext(),""+globDay+"-"+globMonth+"-"+globYear,Toast.LENGTH_LONG).show();
                    }
                    if (parent.getSelectedItem().toString().equals("Custom")) {
                        DatePickerDialog datePickerDialog;
                        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                                globYear = year;
                                globMonth = monthOfYear;
                                globDay = dayOfMonth;
                                whendatespinnerarray.clear();
                                monthOfYear = monthOfYear + 1;
                                whendatespinnerarray.add(dayOfMonth + "-" + monthOfYear + "-" + year);
                                whendatespinnerarray.add("Today");
                                whendatespinnerarray.add("Tomorrow");
                                whendatespinnerarray.add("Custom");
                                whendatespinner.setSelection(0);
                                //Toast.makeText(getContext(),""+globDay+"-"+globMonth+"-"+globYear,Toast.LENGTH_LONG).show();


                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                whendatespinner.setSelection(0);
                            }
                        });
                        datePickerDialog.show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            Button btnwhensave, btnwhencancel;
            btnwhensave = (Button) rootView.findViewById(R.id.btnwhensave);
            btnwhencancel = (Button) rootView.findViewById(R.id.btnwhencancel);

            btnwhencancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });

            final RadioGroup whenRadioGroup;
            whenRadioGroup = (RadioGroup) rootView.findViewById(R.id.whenRbGroup);
            final RadioButton[] whenSelectedRb = new RadioButton[1];
            whenRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    whenSelectedRb[0] = (RadioButton) group.findViewById(checkedId);
                    //Toast.makeText(getContext(), whenSelectedRb[0].getText().toString(), Toast.LENGTH_LONG).show();
                    Cursor cursor;

                    if (whenSelectedRb[0].getText().equals("When I wake up")) {

                        cursor = db.getAllRows_routine(DatabaseHelper.COL2_2_H, DatabaseHelper.COL2_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                        Toast.makeText(getContext(), "ok", Toast.LENGTH_LONG).show();


                    } else if (whenSelectedRb[0].getText().equals("When I leave for work")) {

                        cursor = db.getAllRows_routine(DatabaseHelper.COL3_2_H, DatabaseHelper.COL3_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));

                    } else if (whenSelectedRb[0].getText().equals("When I reach work")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL4_2_H, DatabaseHelper.COL4_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (whenSelectedRb[0].getText().equals("When I have lunch")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL5_2_H, DatabaseHelper.COL5_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (whenSelectedRb[0].getText().equals("When I leave from work")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL6_2_H, DatabaseHelper.COL6_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (whenSelectedRb[0].getText().equals("When I reach home")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL7_2_H, DatabaseHelper.COL7_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (whenSelectedRb[0].getText().equals("When I have dinner")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL8_2_H, DatabaseHelper.COL8_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (whenSelectedRb[0].getText().equals("When I sleep")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL9_2_H, DatabaseHelper.COL9_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else {
                        FragmentManager fm = mainActivity.getSupportFragmentManager();
                        Dialogbefore dialogbefore = new Dialogbefore();
                        dialogbefore.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                        dialogbefore.show(fm, "a");

                    }


                }
            });
            btnwhensave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendar2.set(globYear, globMonth, globDay, globHour, globMin, 0);// SET THE REMINDER CODE
                    long remtime = calendar2.getTimeInMillis();

                    /*SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - hh:mm");
                    String dateString = formatter.format(new Date(remtime));
                    Toast.makeText(getContext(),""+dateString,Toast.LENGTH_LONG).show();*/
                    Calendar testCalendar = Calendar.getInstance();
                    if (testCalendar.getTimeInMillis() < remtime) {
                        setReminder(v.getContext(), remtime, remtext.getText().toString());

                        db.insertData(remtext.getText().toString(), remtime); // INSERT Reminder into DB

                        getDialog().dismiss();
                        Toast.makeText(getContext(), "Reminder set!", Toast.LENGTH_SHORT).show();
                        remtext.setText("");
                        getInstance().populateListView();

                    } else {
                        Toast.makeText(getContext(), "Please choose a valid time.", Toast.LENGTH_SHORT).show();
                    }

                }
            });


            return rootView;
        }

    }


    public static class Dialogafter extends DialogFragment {
        public int globHour, globMin, globYear, globMonth, globDay;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().setTitle("Set a reminder");
            View rootView = inflater.inflate(R.layout.afterdialog_customview, container, false);


            final Spinner afterdatespinner = (Spinner) rootView.findViewById(R.id.afterdatespinner);
            final ArrayList<String> afterdatespinnerarray = new ArrayList<>();
            //atdatespinnerarray.add("Select date");
            afterdatespinnerarray.add("Today");
            afterdatespinnerarray.add("Tomorrow");
            afterdatespinnerarray.add("Custom");
            final ArrayAdapter afterdateadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, afterdatespinnerarray);
            afterdateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            afterdatespinner.setAdapter(afterdateadapter);

            final Calendar calendar = Calendar.getInstance();
            final Calendar calendar2 = Calendar.getInstance(); //for converting into milliseconds

            afterdatespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getSelectedItem().toString().equals("Today")) {
                        globYear = calendar.get(Calendar.YEAR);
                        globMonth = calendar.get(Calendar.MONTH);
                        globDay = calendar.get(Calendar.DAY_OF_MONTH);
                        //Toast.makeText(getContext(),""+globDay+"-"+globMonth+"-"+globYear,Toast.LENGTH_LONG).show();

                    }
                    if (parent.getSelectedItem().toString().equals("Tomorrow")) {
                        GregorianCalendar gc = new GregorianCalendar();
                        gc.add(Calendar.DATE, 1);
                        globYear = gc.get(Calendar.YEAR);
                        globMonth = gc.get(Calendar.MONTH);

                        globDay = gc.get(Calendar.DAY_OF_MONTH);
                        //Toast.makeText(getContext(),""+globDay+"-"+globMonth+"-"+globYear,Toast.LENGTH_LONG).show();
                    }
                    if (parent.getSelectedItem().toString().equals("Custom")) {
                        DatePickerDialog datePickerDialog;
                        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                                globYear = year;
                                globMonth = monthOfYear;
                                globDay = dayOfMonth;
                                afterdatespinnerarray.clear();
                                monthOfYear = monthOfYear + 1;
                                afterdatespinnerarray.add(dayOfMonth + "-" + monthOfYear + "-" + year);
                                afterdatespinnerarray.add("Today");
                                afterdatespinnerarray.add("Tomorrow");
                                afterdatespinnerarray.add("Custom");
                                afterdatespinner.setSelection(0);
                                //Toast.makeText(getContext(),""+globDay+"-"+globMonth+"-"+globYear,Toast.LENGTH_LONG).show();


                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                afterdatespinner.setSelection(0);
                            }
                        });
                        datePickerDialog.show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            Button btnaftersave, btnaftercancel;
            btnaftersave = (Button) rootView.findViewById(R.id.btnaftersave);
            btnaftercancel = (Button) rootView.findViewById(R.id.btnaftercancel);

            btnaftercancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });

            final RadioGroup afterRadioGroup;
            afterRadioGroup = (RadioGroup) rootView.findViewById(R.id.afterRbGroup);
            final RadioButton[] afterSelectedRb = new RadioButton[1];
            afterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    afterSelectedRb[0] = (RadioButton) group.findViewById(checkedId);
//                    Toast.makeText(getContext(), afterSelectedRb[0].getText().toString(), Toast.LENGTH_LONG).show();
                    Cursor cursor;
                    if (afterSelectedRb[0].getText().equals("After I wake up")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL2_2_H, DatabaseHelper.COL2_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (afterSelectedRb[0].getText().equals("After I leave for work")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL3_2_H, DatabaseHelper.COL3_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (afterSelectedRb[0].getText().equals("After I reach work")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL4_2_H, DatabaseHelper.COL4_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (afterSelectedRb[0].getText().equals("After I have lunch")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL5_2_H, DatabaseHelper.COL5_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (afterSelectedRb[0].getText().equals("After I leave from work")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL6_2_H, DatabaseHelper.COL6_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (afterSelectedRb[0].getText().equals("After I reach home")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL7_2_H, DatabaseHelper.COL7_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (afterSelectedRb[0].getText().equals("After I have dinner")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL8_2_H, DatabaseHelper.COL8_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (afterSelectedRb[0].getText().equals("After I sleep")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL9_2_H, DatabaseHelper.COL9_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else {
                        FragmentManager fm = mainActivity.getSupportFragmentManager();
                        Dialogafter dialogafter = new Dialogafter();
                        dialogafter.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                        dialogafter.show(fm, "a");
                    }


                }
            });
            btnaftersave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    calendar2.set(globYear, globMonth, globDay, globHour, globMin, 0);// SET THE REMINDER CODE
                    long remtime = calendar2.getTimeInMillis();
                    remtime = remtime + 600000;

                    /*SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - hh:mm");
                    String dateString = formatter.format(new Date(remtime));
                    Toast.makeText(getContext(),""+dateString,Toast.LENGTH_LONG).show();*/
                    Calendar testCalendar = Calendar.getInstance();
                    if (testCalendar.getTimeInMillis() < remtime) {

                        setReminder(v.getContext(), remtime, remtext.getText().toString());

                        db.insertData(remtext.getText().toString(), remtime); // INSERT Reminder into DB
getDialog().dismiss();
                        Toast.makeText(getContext(), "Reminder set!", Toast.LENGTH_SHORT).show();
                        remtext.setText("");
                        getInstance().populateListView();

                    } else {
                        Toast.makeText(getContext(), "Please choose a valid time.", Toast.LENGTH_SHORT).show();
                    }


                }
            });


            return rootView;
        }

    }


    public static class Dialogbefore extends DialogFragment {
        public int globHour, globMin, globYear, globMonth, globDay;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().setTitle("Set a reminder");
            View rootView = inflater.inflate(R.layout.beforedialog_customview, container, false);


            final Spinner beforedatespinner = (Spinner) rootView.findViewById(R.id.beforedatespinner);
            final ArrayList<String> beforedatespinnerarray = new ArrayList<>();
            //atdatespinnerarray.add("Select date");
            beforedatespinnerarray.add("Today");
            beforedatespinnerarray.add("Tomorrow");
            beforedatespinnerarray.add("Custom");
            final ArrayAdapter beforedateadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, beforedatespinnerarray);
            beforedateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            beforedatespinner.setAdapter(beforedateadapter);

            final Calendar calendar = Calendar.getInstance();
            final Calendar calendar2 = Calendar.getInstance(); //for converting into milliseconds

            beforedatespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getSelectedItem().toString().equals("Today")) {
                        globYear = calendar.get(Calendar.YEAR);
                        globMonth = calendar.get(Calendar.MONTH);
                        globDay = calendar.get(Calendar.DAY_OF_MONTH);
                        //Toast.makeText(getContext(),""+globDay+"-"+globMonth+"-"+globYear,Toast.LENGTH_LONG).show();

                    }
                    if (parent.getSelectedItem().toString().equals("Tomorrow")) {
                        GregorianCalendar gc = new GregorianCalendar();
                        gc.add(Calendar.DATE, 1);
                        globYear = gc.get(Calendar.YEAR);
                        globMonth = gc.get(Calendar.MONTH);

                        globDay = gc.get(Calendar.DAY_OF_MONTH);
                        //Toast.makeText(getContext(),""+globDay+"-"+globMonth+"-"+globYear,Toast.LENGTH_LONG).show();
                    }
                    if (parent.getSelectedItem().toString().equals("Custom")) {
                        DatePickerDialog datePickerDialog;
                        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                                globYear = year;
                                globMonth = monthOfYear;
                                globDay = dayOfMonth;
                                beforedatespinnerarray.clear();
                                monthOfYear = monthOfYear + 1;
                                beforedatespinnerarray.add(dayOfMonth + "-" + monthOfYear + "-" + year);
                                beforedatespinnerarray.add("Today");
                                beforedatespinnerarray.add("Tomorrow");
                                beforedatespinnerarray.add("Custom");
                                beforedatespinner.setSelection(0);
                                //Toast.makeText(getContext(),""+globDay+"-"+globMonth+"-"+globYear,Toast.LENGTH_LONG).show();


                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                beforedatespinner.setSelection(0);
                            }
                        });
                        datePickerDialog.show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            Button btnbeforesave, btnbeforecancel;
            btnbeforesave = (Button) rootView.findViewById(R.id.btnbeforesave);
            btnbeforecancel = (Button) rootView.findViewById(R.id.btnbeforecancel);

            btnbeforecancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });

            final RadioGroup beforeRadioGroup;
            beforeRadioGroup = (RadioGroup) rootView.findViewById(R.id.beforeRbGroup);
            final RadioButton[] beforeSelectedRb = new RadioButton[1];
            beforeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    beforeSelectedRb[0] = (RadioButton) group.findViewById(checkedId);
                    //Toast.makeText(getContext(), beforeSelectedRb[0].getText().toString(), Toast.LENGTH_SHORT).show();
                    Cursor cursor;
                    if (beforeSelectedRb[0].getText().equals("Before I wake up")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL2_2_H, DatabaseHelper.COL2_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (beforeSelectedRb[0].getText().equals("Before I leave for work")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL3_2_H, DatabaseHelper.COL3_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (beforeSelectedRb[0].getText().equals("Before I reach work")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL4_2_H, DatabaseHelper.COL4_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (beforeSelectedRb[0].getText().equals("Before I have lunch")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL5_2_H, DatabaseHelper.COL5_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (beforeSelectedRb[0].getText().equals("Before I leave from work")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL6_2_H, DatabaseHelper.COL6_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (beforeSelectedRb[0].getText().equals("Before I reach home")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL7_2_H, DatabaseHelper.COL7_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (beforeSelectedRb[0].getText().equals("Before I have dinner")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL8_2_H, DatabaseHelper.COL8_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else if (beforeSelectedRb[0].getText().equals("Before I sleep")) {
                        cursor = db.getAllRows_routine(DatabaseHelper.COL9_2_H, DatabaseHelper.COL9_2_M);
                        cursor.moveToFirst();
                        globHour = Integer.parseInt(cursor.getString(0));
                        globMin = Integer.parseInt(cursor.getString(1));
                    } else {
                        FragmentManager fm = mainActivity.getSupportFragmentManager();
                        Dialogbefore dialogbefore = new Dialogbefore();
                        dialogbefore.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                        dialogbefore.show(fm, "a");
                    }


                }
            });
            btnbeforesave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendar2.set(globYear, globMonth, globDay, globHour, globMin, 0);// SET THE REMINDER CODE
                    long remtime = calendar2.getTimeInMillis();
                    remtime = remtime - 600000;

                    /*SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - hh:mm");
                    String dateString = formatter.format(new Date(remtime));
                    Toast.makeText(getContext(),""+dateString,Toast.LENGTH_LONG).show();*/
                    Calendar testCalendar = Calendar.getInstance();
                    if (testCalendar.getTimeInMillis() < remtime) {

                        setReminder(v.getContext(), remtime, remtext.getText().toString());

                        db.insertData(remtext.getText().toString(), remtime); // INSERT Reminder into DB
                        getDialog().dismiss();
                        Toast.makeText(getContext(), "Reminder set!", Toast.LENGTH_SHORT).show();
                        remtext.setText("");
                        getInstance().populateListView();

                    } else {
                        Toast.makeText(getContext(), "Please choose a valid time.", Toast.LENGTH_SHORT).show();
                    }


                }
            });


            return rootView;
        }

    }

    public static String converttime(int hourOfDay, int minute) {
        if (hourOfDay > 12) {
            hourOfDay = hourOfDay - 12;
            return "" + hourOfDay + ":" + minute + " PM";
        } else if (hourOfDay == 12) {
            return "" + hourOfDay + ":" + minute + " PM";
        } else if (hourOfDay == 0) {
            return "12:" + minute + " AM";
        } else {
            return "" + hourOfDay + ":" + minute + " AM";
        }
    }

    public static void setReminder(Context context, long remtime, String remtext) {
        /*Intent intent =new Intent(context.getApplicationContext(),MainActivity.class); //open app on click
        Intent actionIntent=new Intent(context.getApplicationContext(),MainActivity.class);
        TaskStackBuilder taskStackBuilder=TaskStackBuilder.create(context.getApplicationContext()); //open app on click
        taskStackBuilder.addParentStack(MainActivity.class); //open app on click
        taskStackBuilder.addNextIntent(intent); //open app on click
        PendingIntent pendingIntent=taskStackBuilder.getPendingIntent(123,PendingIntent.FLAG_UPDATE_CURRENT); //open app on click

        PendingIntent actionPendingIntent=PendingIntent.getActivity(context.getApplicationContext(),222,actionIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder nBuilder=new NotificationCompat.Builder(context.getApplicationContext());
        nBuilder.setContentTitle("Take book bank");
        //nBuilder.setContentText("Take book bank");

        nBuilder.setSmallIcon(android.R.drawable.ic_popup_reminder);
        nBuilder.setContentIntent(pendingIntent); //open app on click
        nBuilder.setDefaults(Notification.DEFAULT_SOUND);
        nBuilder.setVibrate(new long[]{100,1000});
        nBuilder.setLights(Color.GREEN,400,400);
        nBuilder.addAction(R.drawable.ic_done_black_24dp,"Done",actionPendingIntent);
        Notification notification=nBuilder.build();
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(1,notification);*/

        Intent intentAlarm = new Intent(context.getApplicationContext(), AlarmReceiver.class);


        intentAlarm.putExtra("a", remtext);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, remtime, PendingIntent.getBroadcast(context.getApplicationContext(), 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
        Toast.makeText(context,"in set rem",Toast.LENGTH_SHORT).show();

    }

    public void populateListView() {
        Cursor cursor = db.getAllRows();
        String[] fromFieldNames = new String[]{DatabaseHelper._ID, DatabaseHelper.COL2, DatabaseHelper.COL4};
        int[] toViewIDs = new int[]{R.id.upcomingID, R.id.upcomingtext, R.id.upcomingtime};
        customAdapter = new CustomAdapter(getBaseContext(), R.layout.upcoming_view, cursor, fromFieldNames, toViewIDs, 0);
        noupcoming.setVisibility(View.GONE);
        upcomingList.setAdapter(customAdapter);
        upcomingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup vg = (ViewGroup) view;
                TextView upcomingtext;
                upcomingtext = (TextView) vg.findViewById(R.id.upcomingtext);

                TextView upcomingId;
                upcomingId = (TextView) vg.findViewById(R.id.upcomingID);

                showDeleteDialog(MainActivity.getInstance(), upcomingtext.getText().toString(), upcomingId.getText().toString());
            }
        });
        if (cursor.getCount() == 0) {
            noupcoming.setVisibility(View.VISIBLE);
        }

    }


    public void showDeleteDialog(Activity activity, String delremtext, final String delId) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.deletereminder);
        Button deleteyes = (Button) dialog.findViewById(R.id.deleteyes);
        Button deletecan = (Button) dialog.findViewById(R.id.deletecan);
        TextView text = (TextView) dialog.findViewById(R.id.delremindertext);
        text.setText(delremtext);


        deletecan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        deleteyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteRow(delId);
                Intent actionIntent = new Intent(getBaseContext(), AlarmReceiver.class);
                //PendingIntent cancelPendingIntent = PendingIntent.getActivity(getApplicationContext(),Integer.parseInt(delId), actionIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(), Integer.parseInt(delId), actionIntent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(cancelPendingIntent);
                dialog.dismiss();
                // customAdapter.notifyDataSetChanged();


                populateListView();

            }
        });

        dialog.show();

    }

    public static MainActivity getInstance() {
        return mainActivity;
    }


    public class NotificationDismiss extends AppCompatActivity {
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Toast.makeText(getApplicationContext(), "Notification dismiss", Toast.LENGTH_LONG).show();
            notificationManager.cancel(141);
        }
    }


}
