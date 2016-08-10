package com.mottmacdonald.android.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by KelvinLo on 8/10/2016.
 */
public class showTimePicker {
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute= c.get(Calendar.MINUTE);
//            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of DatePickerDialog and return it

            return new TimePickerDialog(getActivity(), this, hour, minute,true);
        }

//        currentCalendar.get(Calendar.HOUR_OF_DAY),
//                currentCalendar.get(Calendar.MINUTE),
//                currentCalendar.get(Calendar.MINUTE),

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        }
    }
}
