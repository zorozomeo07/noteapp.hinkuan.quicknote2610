package noteapp.hinkuan.quicknote2610.entity;

import android.net.Uri;


public class Attachment {

    private Uri uri;

    private String path;

    private int type;

    public Attachment() {
    }

    public Attachment(Uri uri, String path, int type) {
        this.uri = uri;
        this.path = path;
        this.type = type;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
