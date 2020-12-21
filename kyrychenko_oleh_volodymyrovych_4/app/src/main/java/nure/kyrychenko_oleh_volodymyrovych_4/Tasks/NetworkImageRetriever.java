package nure.kyrychenko_oleh_volodymyrovych_4.Tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import nure.kyrychenko_oleh_volodymyrovych_4.Listeners.OnTaskCompletedListener;

public class NetworkImageRetriever extends AsyncTask<Uri, Float, Bitmap> {
    private OnTaskCompletedListener<Bitmap> listener;

    public NetworkImageRetriever(OnTaskCompletedListener<Bitmap> listener) {
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(Uri... uris) {
        try {
            URL url = URI.create(uris[0].toString()).toURL();

            URLConnection conn = url.openConnection();
            conn.connect();

            InputStream istream = conn.getInputStream();
            BufferedInputStream bufferedIstream = new BufferedInputStream(istream);

            Bitmap img = BitmapFactory.decodeStream(bufferedIstream);

            bufferedIstream.close();
            istream.close();

            return img;
        } catch (IOException ex) {
            return null;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        listener.onTaskCompleted(bitmap);
    }
}
