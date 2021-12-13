package uk.ac.shef.oak.com4510.ui.home;

import android.icu.text.SimpleDateFormat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    private MutableLiveData<String> mTimeText;

    public HomeViewModel() throws ParseException {
        mText = new MutableLiveData<>();
        mTimeText = new MutableLiveData<>();
//        mText.setValue("This is home fragment");


        SimpleDateFormat dateFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat("EEE, d MMM yyyy \n h:m:s aa", Locale.ENGLISH);
//            Date date = dateFormat.parse("Fri Aug 28 18:08:30 CST 2015");


            String date = dateFormat.format(new Date());
            mTimeText.setValue(date);
        }

    }

    public LiveData<String> getText() {
        return mText;
    }


    public LiveData<String> getmTimeText() {
        return mTimeText;
    }


}