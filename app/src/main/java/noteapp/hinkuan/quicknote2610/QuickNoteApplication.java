package noteapp.hinkuan.quicknote2610;

import android.app.Application;

import noteapp.hinkuan.quicknote2610.config.QuickNote;

public class QuickNoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        QuickNote.init(this);
    }
}
