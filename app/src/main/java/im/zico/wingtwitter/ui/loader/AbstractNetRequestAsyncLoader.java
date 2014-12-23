package im.zico.wingtwitter.ui.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import im.zico.wingtwitter.type.AsyncTaskLoaderResult;

/**
 * Created by tinyao on 12/23/14.
 */
public abstract class AbstractNetRequestAsyncLoader<T> extends AsyncTaskLoader<AsyncTaskLoaderResult<T>> {

    private AsyncTaskLoaderResult<T> result;
    private Bundle args;

    public AbstractNetRequestAsyncLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    protected abstract T loadData();

    @Override
    public AsyncTaskLoaderResult<T> loadInBackground() {
        return null;
    }

    public void setArgs(Bundle args) {
        if (result != null) {
            throw new IllegalArgumentException("can't setArgs after loader executes");
        }
        this.args = args;
    }

}
