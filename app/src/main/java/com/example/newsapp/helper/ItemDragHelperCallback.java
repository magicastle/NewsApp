package com.example.newsapp.helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.Interfaces.OnMyChannelItemMoveListener;

public class ItemDragHelperCallback extends ItemTouchHelper.Callback {
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags;
        dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if(viewHolder.getItemViewType() != target.getItemViewType())
            return false;
//        if(recyclerView.getAdapter() instanceof OnMyChannelItemMoveListener){
//        }
        OnMyChannelItemMoveListener onMyChannelItemMoveListener = (OnMyChannelItemMoveListener)recyclerView.getAdapter();
        onMyChannelItemMoveListener.onItemMvoe(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
         return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }
}
