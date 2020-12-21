package nure.kyrychenko_oleh_volodymyrovych_4.Controls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;

import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import nure.kyrychenko_oleh_volodymyrovych_4.Model.INote;
import nure.kyrychenko_oleh_volodymyrovych_4.Listeners.OnTaskCompletedListener;
import nure.kyrychenko_oleh_volodymyrovych_4.Model.Importance;
import nure.kyrychenko_oleh_volodymyrovych_4.Tasks.NetworkImageRetriever;
import nure.kyrychenko_oleh_volodymyrovych_4.Util.Util;

public class NoteView extends GridLayout implements OnTaskCompletedListener<Bitmap> {
    private static final int IMAGE_SIZE = 80;
    private static final int IMAGE_PADDING = 1;
    private static final int BORDER_WIDTH = 4;
    private static final int INDICATOR_RADIUS = 16;
    private static final int INDICATOR_PADDING = 5;

    private static final Paint BORDER_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final Paint INDICATOR_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final Paint TEXT_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);

    private INote note;

    private ImageView image;
    private TextView title;
    private TextView description;
    private TextView destinationDate;
    private TextView destinationTime;

    public INote getNote() {
        return note;
    }

    public void setNote(INote note) {
        this.note = note;
        updateData();
    }

    public NoteView(Context context) {
        super(context);
        init(context);
    }

    public NoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NoteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        float half = BORDER_WIDTH / 2.0f;
        float borderRight = width - BORDER_WIDTH;
        float borderBottom = height - BORDER_WIDTH;

        canvas.drawRect(half, half, borderRight, borderBottom, BORDER_PAINT);

        if(note != null) {
            Importance importance = note.getImportance();

            float indicatorX = borderRight - INDICATOR_PADDING - INDICATOR_RADIUS;
            float indicatorY = BORDER_WIDTH + INDICATOR_PADDING + INDICATOR_RADIUS;

            if(importance != null) {
                switch (note.getImportance()) {
                    case LOW:
                        INDICATOR_PAINT.setColor(Color.rgb(0, 255, 0));
                        break;
                    case MEDIUM:
                        INDICATOR_PAINT.setColor(Color.rgb(255, 255, 0));
                        break;
                    case HIGH:
                        INDICATOR_PAINT.setColor(Color.rgb(255, 0, 0));
                        break;
                }
                canvas.drawCircle(indicatorX, indicatorY, INDICATOR_RADIUS, INDICATOR_PAINT);
            }
            canvas.drawCircle(indicatorX, indicatorY, INDICATOR_RADIUS, BORDER_PAINT);

            Date creationDate = note.getCreationDateTime();

            if(creationDate != null) {
                String creationText = Util.FormatDate(creationDate);
                canvas.drawText(creationText, indicatorX - 150, indicatorY, TEXT_PAINT);
            }
        }
    }

    @Override
    public void onTaskCompleted(Bitmap data) {
        image.setImageBitmap(data);
    }

    public void updateData() {
        String link = note.getImageUri();

        if(link != null && !link.isEmpty()) {
            Uri uri = Uri.parse(link);
            if(link.startsWith("http://") || link.startsWith("https://")) {
                NetworkImageRetriever imageRetriever = new NetworkImageRetriever(this);
                imageRetriever.execute(uri);
            } else {
                image.setImageURI(uri);
            }
        }

        title.setText(note.getTitle());
        description.setText(note.getDescription());

        Date destDate = note.getDestinationDate();
        if(destDate != null) {
            destinationDate.setText(Util.FormatDate(destDate));
        }

        Date destTime = note.getDestinationTime();
        if(destTime != null) {
            destinationTime.setText(" " + Util.FormatTime(destTime));
        }
    }

    private void init(Context context) {
        BORDER_PAINT.setColor(Color.rgb(0, 0, 0));
        BORDER_PAINT.setStyle(Paint.Style.STROKE);
        BORDER_PAINT.setStrokeWidth(BORDER_WIDTH);

        TEXT_PAINT.setColor(Color.rgb(50, 50, 50));
        TEXT_PAINT.setTextSize(20);

        setWillNotDraw(false);

        setRowCount(3);
        setColumnCount(3);

        image = initImage(context);
        title = initTextBlock(context, 0, 1, 1, 2);
        description = initTextBlock(context, 1, 1, 1, 2);
        destinationDate = initTextBlock(context, 2, 1, 1, 1);
        destinationTime = initTextBlock(context, 2, 2, 1, 1);
    }

    private ImageView initImage(Context context)
    {
        ImageView image = new ImageView(context);

        LayoutParams imageLayoutParams = new LayoutParams();
        int imageSize = Util.Dp2Px(IMAGE_SIZE, context);

        imageLayoutParams.width = imageSize;
        imageLayoutParams.height = imageSize;

        imageLayoutParams.columnSpec = spec(0,1);
        imageLayoutParams.rowSpec = spec(0, 3);

        image.setLayoutParams(imageLayoutParams);

        int imagePadding = Util.Dp2Px(IMAGE_PADDING, context);
        image.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);

        this.addView(image);
        return image;
    }

    private TextView initTextBlock(Context context, int rowStart, int colStart, int rowSize, int colSize)
    {
        TextView text = new TextView(context);

        LayoutParams textLayoutParams = new LayoutParams();

        textLayoutParams.columnSpec = spec(colStart, colSize);
        textLayoutParams.rowSpec = spec(rowStart, rowSize);

        text.setLayoutParams(textLayoutParams);

        this.addView(text);
        return text;
    }
}
