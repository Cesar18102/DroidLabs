package nure.kyrychenko_oleh_volodymyrovych_4.Util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public final class ImageSelector {
    public static final int SELECT_IMAGE_MODE = 1;

    public static void selectImage(Activity activity) {
        Intent getIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(
                Intent.ACTION_OPEN_DOCUMENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{ pickIntent });

        activity.startActivityForResult(chooserIntent, SELECT_IMAGE_MODE);
    }

    public static Uri checkResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != SELECT_IMAGE_MODE || resultCode != Activity.RESULT_OK || data == null) {
            return null;
        }

        return data.getData();
    }
}
