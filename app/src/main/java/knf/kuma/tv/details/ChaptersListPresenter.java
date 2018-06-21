package knf.kuma.tv.details;

import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.RowPresenter;

public class ChaptersListPresenter extends ListRowPresenter {

    private int position = 0;

    public ChaptersListPresenter(int position) {
        this.position = position;
    }

    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder holder, Object item) {
        super.onBindRowViewHolder(holder, item);
        ViewHolder vh = (ListRowPresenter.ViewHolder) holder;
        vh.getGridView().setSelectedPosition(position);
    }
}
