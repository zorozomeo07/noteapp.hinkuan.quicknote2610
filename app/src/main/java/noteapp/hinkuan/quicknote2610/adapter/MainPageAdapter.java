package noteapp.hinkuan.quicknote2610.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


import noteapp.hinkuan.quicknote2610.R;
import noteapp.hinkuan.quicknote2610.adapter.base.BaseAdapter;
import noteapp.hinkuan.quicknote2610.adapter.base.IAdapterItem;
import noteapp.hinkuan.quicknote2610.entity.Note;
import noteapp.hinkuan.quicknote2610.util.CommonUtil;
import tech.huqi.quicknote.core.view.ImageTextView;


public class MainPageAdapter extends BaseAdapter<Note, MainPageAdapter.MainPageNoteItemView> {
    private static boolean isGridMode = true;

    public MainPageAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    protected MainPageNoteItemView inflateView(Context context, ViewGroup parent) {
        return new MainPageNoteItemView(context);
    }

    public void setIsGridMode(boolean isGridMode) {
        this.isGridMode = isGridMode;
    }

    public class MainPageNoteItemView extends LinearLayout implements IAdapterItem<Note> {
        private TextView tvNoteTitle;
        private ImageTextView tvNoteMainBody;
        private TextView tvNoteDate;

        public MainPageNoteItemView(Context context) {
            super(context);
            View v = LayoutInflater.from(context).inflate(R.layout.recycle_main_page_note_item, this, true);
            tvNoteTitle = v.findViewById(R.id.tv_note_title);
            tvNoteMainBody = v.findViewById(R.id.tv_note_main_body);
            tvNoteDate = v.findViewById(R.id.tv_note_date);
            adjustBodyLayoutParams();
        }

        @Override
        public void bindDataToView(Note note, int position) {
            tvNoteTitle.setText(note.getTitle());
            tvNoteMainBody.setText(note.getContent());
            tvNoteDate.setText(note.getDate());
        }

        private void adjustBodyLayoutParams() {
            ViewGroup.LayoutParams params = tvNoteMainBody.getLayoutParams();
            params.width = CommonUtil.getScreenWidthPixels(mContext);
            if (isGridMode) {
                params.height = params.width / 2;
            } else {
                params.height = params.width / 6;
            }
            tvNoteMainBody.setLayoutParams(params);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return true;
        }
    }
}
