package noteapp.hinkuan.quicknote2610.config;



public interface Constants {

    int DELAY_TIME = 1000 * 2;

    int ATTACHMENT_TYPE_IMAGE = 0;
    int ATTACHMENT_TYPE_AUDIO = 1;


    String FILE_NAME_DATE_FORMAT = "yyyyMMdd_HHmmss_SSS";

    String TIME_FORMAT_Y_M_D = "yyyy/MM/dd";
    String TIME_FORMAT_Y_M_D_H_M = "yyyy/MM/dd HH:mm";
    String TIME_FORMAT_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";

    String INTENT_FROM_WELCOME_ACTIVITY = "from_welcome_activity";
    String INTENT_NOTE_ITEM = "note_item";
    String INTENT_QUICK_ACTION = "quick_action";
    String INTENT_QUICK_ACTION_CAMERA = "quick_action_camera";
    String INTENT_QUICK_ACTION_RECORD = "quick_action_record";
    String INTENT_QUICK_ACTION_FREEHAND = "quick_action_freehand";
    String INTENT_IMAGE_PATH = "image_path";
    String INTENT_VOICE_RECOGNIZED = "voice_recognized";

    int RECENT_EDIT_MAX_NUM = 10;

    String SHARED_PREFERENCE_NAME = "quick_note";
}
