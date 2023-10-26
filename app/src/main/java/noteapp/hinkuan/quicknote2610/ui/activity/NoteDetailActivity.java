package noteapp.hinkuan.quicknote2610.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import noteapp.hinkuan.quicknote2610.R;
import noteapp.hinkuan.quicknote2610.config.QuickNote;
import noteapp.hinkuan.quicknote2610.db.NoteDatabaseHelper;
import noteapp.hinkuan.quicknote2610.entity.Attachment;
import noteapp.hinkuan.quicknote2610.entity.Note;
import noteapp.hinkuan.quicknote2610.util.AttachmentHelper;
import noteapp.hinkuan.quicknote2610.util.CommonUtil;
import noteapp.hinkuan.quicknote2610.util.BitmapUtil;
import noteapp.hinkuan.quicknote2610.util.DialogFactory;
import noteapp.hinkuan.quicknote2610.util.PopupWindowFactory;
import noteapp.hinkuan.quicknote2610.util.UriHelper;
import tech.huqi.quicknote.core.view.ImageTextMixtureEditor;

import static noteapp.hinkuan.quicknote2610.config.Constants.INTENT_IMAGE_PATH;
import static noteapp.hinkuan.quicknote2610.config.Constants.INTENT_NOTE_ITEM;
import static noteapp.hinkuan.quicknote2610.config.Constants.INTENT_QUICK_ACTION;
import static noteapp.hinkuan.quicknote2610.config.Constants.INTENT_QUICK_ACTION_CAMERA;
import static noteapp.hinkuan.quicknote2610.config.Constants.INTENT_QUICK_ACTION_FREEHAND;
import static noteapp.hinkuan.quicknote2610.config.Constants.INTENT_QUICK_ACTION_RECORD;
import static noteapp.hinkuan.quicknote2610.config.Constants.INTENT_VOICE_RECOGNIZED;
public class NoteDetailActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final int BOTTOM_SHEET_ITEM_PICTURE = 0;
    private static final int BOTTOM_SHEET_ITEM_FREEHAND = 1;
    private static final int BOTTOM_SHEET_ITEM_RECORD = 2;
    private static final int TAKE_PHOTO = 3;

    private Note mNote;
    private EditText mEtNoteTitle;
    private ImageTextMixtureEditor mImageTextMixtureEditor;
    private Attachment mAttachment = new Attachment();

    private int[] mAttachmentPopMenuIcons;
    private String[] mAttachmentPopMenuTexts;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initData();
    }

    private void initView() {
        mEtNoteTitle = findViewById(R.id.et_note_title);
        mImageTextMixtureEditor = findViewById(R.id.it_mixture);
    }

    private void initData() {
        initInternalData();
        initIntentData();
    }

    private void initInternalData() {
        mAttachmentPopMenuIcons = new int[]{
                R.drawable.ic_photo_black_24dp,
                R.drawable.ic_freehand_black_24dp,
                R.drawable.ic_record_black_24dp};
        mAttachmentPopMenuTexts = new String[]{
                getString(R.string.attachment_pop_menu_album),
                getString(R.string.attachment_pop_menu_freehand),
                getString(R.string.attachment_pop_menu_record)};
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String quickAction = intent.getStringExtra(INTENT_QUICK_ACTION);
            if (!TextUtils.isEmpty(quickAction)) {
                processQuickActionIntent(quickAction);
            } else {
                mNote = intent.getParcelableExtra(INTENT_NOTE_ITEM);
                if (mNote != null) {
                    mEtNoteTitle.setText(mNote.getTitle());
                    mImageTextMixtureEditor.setText(mNote.getContent());
                }
            }
        }
    }

    private void processQuickActionIntent(String quickAction) {
        switch (quickAction) {
            case INTENT_QUICK_ACTION_CAMERA: {
                takePhoto();
            }
            break;
            case INTENT_QUICK_ACTION_RECORD: {
                gotoVoiceShorthandActivity();
            }
            break;
            case INTENT_QUICK_ACTION_FREEHAND: {
                gotoFreehandActivity();
            }
            break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                DialogFactory.createAndShowDialog(this, QuickNote.getString(R.string.is_save_note),
                        null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveNote();
                                finish();
                            }
                        });
            }
            break;
            case R.id.menu_attachment: {
                showAttachmentPopupDialog();
            }
            break;
            case R.id.menu_camera: {
                takePhoto();
            }
            break;
            case R.id.menu_send: {
                CommonUtil.showToastOnUiThread(R.string.purchase_vip_tip);
            }
            break;
            case R.id.menu_delete: {
                NoteDatabaseHelper.getInstance().remove(mNote.getId());
                finish();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAttachmentPopupDialog() {
        ListPopupWindow popupWindow = PopupWindowFactory.createListPopUpWindow(this, findViewById(R.id.menu_attachment),
                mAttachmentPopMenuIcons, mAttachmentPopMenuTexts, new PopupWindowFactory.ListPopUpWindowItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case BOTTOM_SHEET_ITEM_PICTURE: {
                                if (EasyPermissions.hasPermissions(NoteDetailActivity.this,
                                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    openSystemGallery();

                                } else {
                                    EasyPermissions.requestPermissions(NoteDetailActivity.this,
                                            getString(R.string.need_read_storage_permission),
                                            RC_READ_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE);
                                }
                            }
                            break;
                            case BOTTOM_SHEET_ITEM_FREEHAND: {
                                gotoFreehandActivity();
                            }
                            break;
                            case BOTTOM_SHEET_ITEM_RECORD: {
                                gotoVoiceShorthandActivity();
                            }
                            break;
                        }
                    }
                });
        popupWindow.show();
    }

    private void openSystemGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, BOTTOM_SHEET_ITEM_PICTURE);

    }

    private void gotoFreehandActivity() {
        Intent intent = new Intent(NoteDetailActivity.this, FreeHandActivity.class);
        startActivityForResult(intent, BOTTOM_SHEET_ITEM_FREEHAND);
    }

    private void gotoVoiceShorthandActivity() {
        Intent intent = new Intent(NoteDetailActivity.this, VoiceShorthandActivity.class);
        startActivityForResult(intent, BOTTOM_SHEET_ITEM_RECORD);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File image = AttachmentHelper.createNewAttachmentFile(this, "images", ".jpg");
        // 必须使用FileProvider，否则API>=24的设备上运行时，会抛出异常
        // Uri photoUri = Uri.fromFile(image);
        Uri photoUri = AttachmentHelper.getFileProviderUri(this, image);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        mAttachment.setPath(image.getAbsolutePath());
        mAttachment.setUri(photoUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case BOTTOM_SHEET_ITEM_PICTURE: {
                    Uri uri = data.getData();
                    mImageTextMixtureEditor.insertImage(UriHelper.getAbsolutePathByUri(QuickNote.getAppContext(), uri));
                }
                break;
                case BOTTOM_SHEET_ITEM_FREEHAND: {
                    String freePicturePath = data.getStringExtra(INTENT_IMAGE_PATH);
                    CommonUtil.showToastOnUiThread("Đường dẫn vẽ tay: " + freePicturePath);
                    mImageTextMixtureEditor.insertImage(freePicturePath);
                }
                break;
                case BOTTOM_SHEET_ITEM_RECORD: {
                    String speechResult = data.getStringExtra(INTENT_VOICE_RECOGNIZED);
                    mImageTextMixtureEditor.append("\n" + speechResult);
                }
                break;
                case TAKE_PHOTO: {
                    String pictureUri = mAttachment.getPath();
                    BitmapUtil.adjustPhotoImageOrientation(QuickNote.getAppContext(), pictureUri);
                    mImageTextMixtureEditor.insertImage(pictureUri);
                    if (new File(pictureUri).exists()) {
//                        Toast.makeText(getApplicationContext(), pictureUri, Toast.LENGTH_LONG).show();
                    } else {
//                        Toast.makeText(getApplicationContext(), "路径不存在", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private void saveNote() {
        String noteTitle = mEtNoteTitle.getText().toString();
        String noteContent = mImageTextMixtureEditor.getText().toString();
        Note note = new Note();
        if (mNote != null) { // 来自已经存在的Note
            note.setId(mNote.getId());
        }
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setLastModify(System.currentTimeMillis());
        NoteDatabaseHelper.getInstance().save(note);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == RC_READ_EXTERNAL_STORAGE) {
            openSystemGallery();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
