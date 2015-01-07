package im.zico.wingtwitter.ui.view;

/**
 * Created by tinyao on 12/31/14.
 */
import android.graphics.Bitmap;
import com.squareup.picasso.Transformation;

/**
 * Transformate the loaded image to avoid OutOfMemoryException
 */
public class MediaPicTransform implements Transformation {

    int maxWidth;
    int maxHeight;

    public MediaPicTransform(int min, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int targetWidth, targetHeight;
        double aspectRatio;

        if (source.getWidth() > source.getHeight()) {
            targetWidth = maxWidth;
            aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            targetHeight = (int) (targetWidth * aspectRatio);
        } else {
            targetHeight = maxHeight;
            aspectRatio = (double) source.getWidth() / (double) source.getHeight();
            targetWidth = (int) (targetHeight * aspectRatio);
        }

        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return maxWidth + "x" + maxHeight;
    }

}