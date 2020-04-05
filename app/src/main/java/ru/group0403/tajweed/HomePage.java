package ru.group0403.tajweed.quran;

import java.util.ArrayList;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import ru.group0403.tajweed.R;


public class HomePage extends AppCompatActivity implements CustomAdapter.ListEvent {

    private ListView loadedSurahListView;
    ArrayList<Pair<String, String>> adapterList;
    private HomePage CustomListView;
    private CustomAdapter adapter;
    private ArrayList<Surah> surahList;
    LinearLayout layout;
    // for maintaining the versecount of each surah, It will be used my
    // mediaPlayer to locate the particular verse in the translated file. For
    // more details view translated files in the files
    private ArrayList<Integer> surahVerseCount;
    private int prevTheme;

    /**
     * This function initializes all the variables such as views, buttons,
     * listsView ...
     */
    private void initiliazeVariables() {
        layout = (LinearLayout) findViewById(R.id.home_page_outer_layout);

        AssestsReader reader = new AssestsReader(getApplicationContext());
        surahList = reader.getSurahsList();
        adapterList = new ArrayList<Pair<String, String>>();
        surahVerseCount = new ArrayList<Integer>();

        for (Surah surah : surahList) {
            String first = surah.getSurahNumber() + ". " + surah.getName();
            adapterList.add(new Pair<String, String>(first, surah
                    .getArabicName()));

            surahVerseCount.add(surah.getVerseCount());
        }
        CustomListView = this;
        loadedSurahListView = (ListView) findViewById(R.id.home_page_surahs_list);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        prevTheme = -1;

        initiliazeVariables();
        // MyPreferenceHandler.setSurahPositionPreference(getApplicationContext(),
        // 0);
    }

    @Override
    protected void onResume() {
        int theme = MyPreferenceHandler.getTheme(getApplicationContext());
        // change the theme if the choice of theme has been changed. This may
        // triggerd when user returns to homepage after reviewing settings
        if (theme != prevTheme) {
            switch (theme) {
                case MyPreferenceHandler.DARK_THEME:
                    layout.setBackgroundColor(Color
                            .parseColor(CONSTANT.BLACK_BACKGROUND_COLOUR));
                    loadedSurahListView.setDivider(new ColorDrawable(Color
                            .parseColor(CONSTANT.BLACK_DIVIDER)));
                    break;
                case MyPreferenceHandler.MUSHAF_THEME:
                    layout.setBackgroundColor(Color
                            .parseColor(CONSTANT.MUSHAF_BACKGROUND_COLOUR));
                    loadedSurahListView.setDivider(new ColorDrawable(Color
                            .parseColor(CONSTANT.WHITE_DIVIDER)));

                    break;
                case MyPreferenceHandler.WHITE_THEME:
                    layout.setBackgroundColor(Color
                            .parseColor(CONSTANT.WHITE_BACKGROUND_COLOUR));
                    loadedSurahListView.setDivider(new ColorDrawable(Color
                            .parseColor(CONSTANT.WHITE_DIVIDER)));

                    break;
            }
            loadedSurahListView.setDividerHeight(1);
            Resources res = getResources();
            adapter = new CustomAdapter(CustomListView, surahList, res);
            adapter.listEventListener = this;
            loadedSurahListView.setAdapter(adapter);
            loadedSurahListView.setSelection(MyPreferenceHandler
                    .getSurahPositionPreference(getApplicationContext()));

        }
        super.onResume();

    }



    /*
     * Called when list item is called.
     */
    public void onItemClick(int mPosition) {
        int position = loadedSurahListView.getFirstVisiblePosition();
        MyPreferenceHandler.setSurahPositionPreference(HomePage.this, position);

        ReciterDataManger manager = new ReciterDataManger(
                getApplicationContext());
        Surah surah = surahList.get(mPosition);

        //If surah contains atleast one audio , then play otherwise throw a toast message
        if (manager.getDownloadedReciterList(surah).size() > 0) {
            Intent intent = new Intent(HomePage.this, MyMediaPlayer.class);
            intent.putExtra("surah", surah);
            intent.putExtra("surahVerseCount", surahVerseCount);
            startActivity(intent);
        } else {

            Logger.makeToast(
                    HomePage.this,
                    "Surah "
                            + surah.getName()
                            + getString(R.string.fffddd));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private static final int RC_EXTERNAL_SAVE = 1;
    private static final int RC_EXTERNAL_DELETE = 2;
    private Surah tempSurah;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isSavingSurah = requestCode == RC_EXTERNAL_SAVE;
        boolean isDeletingSurah = requestCode == RC_EXTERNAL_DELETE;

        if (isSavingSurah || isDeletingSurah) {
            if (grantResults.length == 1 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED && tempSurah != null) {
                if(isSavingSurah)
                    onDownloadClick(tempSurah);
                else
                    onDeleteClick(tempSurah);
            } else {
                Logger.makeToast(this, "Вы должны дать разрешение для сохранения и удаления суры из телефона");
            }
        }
    }

    @Override
    public void onDownloadClick(Surah surah) {
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED) {
            MyDialogBuilder.buildDownloadDialog(this, surah);
        } else {
            tempSurah=surah;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RC_EXTERNAL_SAVE);
        }
    }

    @Override
    public void onDeleteClick(Surah surah) {
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED) {
            MyDialogBuilder.buildDeleteSurah(this, surah);
        } else {
            tempSurah=surah;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RC_EXTERNAL_SAVE);
        }
    }
}
