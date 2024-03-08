// MenuHandler.java
package com.nirv.proj1;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

public class MenuHandler {

    private Context context;

    public MenuHandler(Context context) {
        this.context = context;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(context);
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mainMenuLogOutButton) {
            logout();
            return true;
        }
        return false;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}
