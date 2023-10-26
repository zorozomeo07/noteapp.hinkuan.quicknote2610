package noteapp.hinkuan.quicknote2610.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import noteapp.hinkuan.quicknote2610.R;
import noteapp.hinkuan.quicknote2610.db.NoteDatabaseHelper;
import noteapp.hinkuan.quicknote2610.entity.Note;

import noteapp.hinkuan.quicknote2610.config.Constants;


public class RecentEditFragment extends BaseNoteListFragment {
    @Override
    View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    @Override
    void initData() {

    }

    @Override
    List<Note> onGetNotes() {
        mNotes = NoteDatabaseHelper.getInstance().getRecentlyNotes(Constants.RECENT_EDIT_MAX_NUM);
        return mNotes;
    }

    @Override
    void onNoteItemLongClick(View v, int position) {

    }

    @Override
    void initView() {

    }

    @Override
    void onRefreshData() {
        mNotes = NoteDatabaseHelper.getInstance().getRecentlyNotes(Constants.RECENT_EDIT_MAX_NUM);
        mAdapter.refreshData(mNotes);
    }
}
