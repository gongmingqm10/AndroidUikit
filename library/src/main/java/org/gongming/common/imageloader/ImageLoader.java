package org.gongming.common.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageLoader {

    private static ImageLoader instance;
    private static Context context;
    private final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private final int corePoolSize = CPU_COUNT + 1;
    private final int maximumPoolSize = CPU_COUNT * 2 + 1;
    private final int keepAliveTime = 1;
    private final int IMAGE_LOAD_MESSAGE = 200;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == IMAGE_LOAD_MESSAGE) {
                ImageView target = (ImageView) msg.obj;
                Bitmap bitmap = bitmapLruCache.get((String) target.getTag());
                if (bitmap != null) target.setImageBitmap(bitmap);
            }

        }
    };
    private ThreadPoolExecutor threadPoolExecutor;
    private LruCache<String, Bitmap> bitmapLruCache;

    private ImageLoader() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        bitmapLruCache = new LruCache<String, Bitmap>(cacheSize);
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    }

    public static void init(Context mContext) {
        context = mContext;
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    public void load(final String urlString, final ImageView imageView) {
        load(urlString, 0, imageView);
    }

    public void load(final String urlString, int placeholder, final ImageView imageView) {
        if (placeholder != 0) imageView.setImageResource(placeholder);
        if (urlString == null || "".equals(urlString)) return;
        Bitmap cachedBitmap = bitmapLruCache.get(urlString);
        if (cachedBitmap != null) {
            imageView.setImageBitmap(cachedBitmap);
            return;
        }
        imageView.setTag(urlString);

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    if (bitmap == null) return;
                    bitmapLruCache.put(urlString, bitmap);
                    if(imageView.getTag().equals(urlString)) {
                        handler.sendMessage(handler.obtainMessage(IMAGE_LOAD_MESSAGE, imageView));
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
