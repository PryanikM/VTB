package com.example.vtb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.PhoneNumberUnderscoreSlotsParser;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class ClientInfo extends AppCompatActivity {

    private EditText surname;
    private EditText name;
    private EditText patronymic;
    private EditText birthDate;
    private EditText phoneNumber;

    private Button button;

    private MaskImpl maskNumber;
    private FormatWatcher formatWatcherNumber;

    private MaskImpl maskBirthDate;
    private FormatWatcher formatWatcherBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_info);

        surname = (EditText) findViewById(R.id.surname);
        name = (EditText) findViewById(R.id.name);
        patronymic = (EditText) findViewById(R.id.patronymic);
        birthDate = (EditText) findViewById(R.id.birth_date_time);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        button = (Button) findViewById(R.id.rent_button);

        birthDate.addTextChangedListener(textWatcher());
        phoneNumber.addTextChangedListener(textWatcher());
        patronymic.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                // TODO Auto-generated method stub
                if (keyCode == event.KEYCODE_ENTER) {

                    birthDate.requestFocus();
                    return true;
                }

                return false;
            }
        });

        maskNumber = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER);
        maskNumber.setHideHardcodedHead(true);
        formatWatcherNumber = new MaskFormatWatcher(maskNumber);
        formatWatcherNumber.installOn(phoneNumber);

        Slot[] slots = new PhoneNumberUnderscoreSlotsParser().parseSlots("__.__.____");
        maskBirthDate = MaskImpl.createTerminated(slots);
        maskBirthDate.setHideHardcodedHead(true);
        formatWatcherBirthDate = new MaskFormatWatcher(maskBirthDate);
        formatWatcherBirthDate.installOn(birthDate);

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence cs, int start,
                                       int end, Spanned spanned, int dStart, int dEnd) {
                // TODO Auto-generated method stub
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(cs.charAt(i))) {
                        return "";
                    }
                }
                if (cs.toString().matches("[a-zA-Zа-яА-Я]+")) {
                    return cs;
                }
                return "";
            }
        };

        name.setFilters(new InputFilter[]{filter});
        surname.setFilters(new InputFilter[]{filter});
        patronymic.setFilters(new InputFilter[]{filter});


    }

    public void buttonClick(View view) {

        if (checkEditText()){
            makeJSON();
        }
    }

    private boolean checkEditText() {
        if (name.getText().length() > 0 &&
                surname.getText().length() > 0 &&
                patronymic.getText().length() > 0 &&
                birthDate.getText().length() == 10 &&
                phoneNumber.getText().length() == 18) {
            return true;
        } else {
            return false;
        }

    }

    public TextWatcher textWatcher() {

        return new TextWatcher() {

            CountDownTimer timer = null;

            @Override
            public void afterTextChanged(Editable s) {
                //do some thigs
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do some thigs
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (birthDate.getText().hashCode() == s.hashCode() &&
                        birthDate.getText().length() == 10) {
                    phoneNumber.requestFocus();
                } else if (phoneNumber.getText().hashCode() == s.hashCode() &&
                        phoneNumber.getText().length() == 18) {
                    phoneNumber.clearFocus();
                }
            }
        };


    }

    private void makeJSON(){
        Log.d("JSON", surname.getText().toString());
        Log.d("JSON", name.getText().toString());
        Log.d("JSON", patronymic.getText().toString());
        Log.d("JSON", formatWatcherBirthDate.getMask().toUnformattedString());
        Log.d("JSON", formatWatcherNumber.getMask().toUnformattedString());

    }
}
