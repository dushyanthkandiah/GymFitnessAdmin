package OtherClasses;

import android.content.Context;
import android.widget.EditText;


public class Validation {

    private Context c;

    public Validation(Context c) {
        this.c = c;
    }

    public boolean CheckEmptyText(String[] fieldName, EditText[] field) {
        boolean value = true;
        for (int i = 0; i < field.length; i++) {
            Boolean b = CheckTextEmpty(field[i].getText().toString().trim(), fieldName[i]);
            if (!b) {
                field[i].requestFocus();
                value = false;
                break;
            } else {
                value = true;
            }
        }

        return value;
    }

    // Check if text boxes are empty
    private boolean CheckTextEmpty(String text, String type) {

        if (text.equals("")) {
            ShowDialog.showToast(c, "Please Fill your " + type + " Properly");

            return false;
        } else {
            return true;
        }

    }

    // Method for phone number validation
    public boolean PhoneCheck(String number) {
        if (number.length() < 9) {  // will check the char in phone is less than 9
            ShowDialog.showToast(c, "Phone Number must be more than or equal 9 digits");
            return false;
        } else {

            if (!number.matches("^[0-9\\-]*$")) {
                ShowDialog.showToast(c, "Phone number cannot contain any other symbol other than '-'");
                return false;
            } else {
                return true;
            }
        }
    }

    // Method for Email Validation
    public boolean EmailCheck(String email) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!email.matches(emailPattern)) {
            ShowDialog.showToast(c, "Please Re-Check the Email address");
            return false;
        } else {
            return true;
        }

    }


    // Method for password validation
    public boolean PasswordCheck(String word, String confirm) {

        if (word.length() < 6) {
            ShowDialog.showToast(c, "Password should be more than or equal to 6 character");
            return false;
        } else if (!word.equals(confirm)) {
            ShowDialog.showToast(c, "Your Passwords doesn't match each other");
            return false;
        } else {
            return true;
        }


    }

    // Method for password validation
    public boolean PasswordCheck(String word) {

        if (word.length() < 6) {
            ShowDialog.showToast(c, "Password should be more than or equal to 6 character");
            return false;
        } else {
            return true;
        }

    }


}
