package com.aizenberg.intech.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aizenberg.intech.R;
import com.aizenberg.intech.activity.MainActivity;
import com.aizenberg.intech.adapter.BaseHolderAdapter;
import com.aizenberg.intech.adapter.BasePaginableHolderAdapter;
import com.aizenberg.intech.adapter.MelodiesAdapter;
import com.aizenberg.intech.adapter.decorator.ItemOffsetDecoration;
import com.aizenberg.intech.core.IntechException;
import com.aizenberg.intech.core.model.Melodies;
import com.aizenberg.intech.core.model.Melody;
import com.aizenberg.intech.core.model.RestFault;
import com.aizenberg.intech.core.network.service.MelodiesRestService;
import com.aizenberg.intech.fragment.common.EndlessRecyclerOnScrollListener;
import com.aizenberg.intech.view.PositionRecyclerView;
import com.aizenberg.support.fsm.Switcher;
import com.aizenberg.support.viewinjector.annotation.Id;
import com.aizenberg.support.viewinjector.annotation.Layout;

import org.greenrobot.eventbus.Subscribe;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by Yuriy Aizenberg
 */
@Layout(R.layout.melodies_fragment)
public class MelodiesFragment extends BaseFragment {

    public static final int GRID_MODE = 0;
    public static final int LIST_MODE = 1;

    @Id(R.id.recycler_view)
    private PositionRecyclerView recyclerView;
    @Id(R.id.progress_bar_lazy)
    private View lazyProgressView;
    private boolean isLoading;
    private MelodiesAdapter melodiesAdapter;
    private int currentMode = GRID_MODE;
    private ItemOffsetDecoration decor;
    private EndlessRecyclerOnScrollListener listener;

    @Override
    protected void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        recyclerView.setHasFixedSize(true);
        melodiesAdapter = new MelodiesAdapter(getActivity());
        melodiesAdapter.setHasStableIds(true);
        melodiesAdapter.setItemClickListener(new BaseHolderAdapter.IItemClickListener<Melody>() {
            @Override
            public void onItemClick(Melody data) {
                Bundle args = new Bundle();
                args.putSerializable("data", data);
                Switcher.obtainSwitcher(MainActivity.class).switchTo(PlayerFragment.class, args);
            }
        });
        changeMode(currentMode, isInLandscape());
        if (currentMode == GRID_MODE) {
            setGridIcon();
        } else {
            setListIcon();
        }
        initEndlessListener();
        executeService(new MelodiesRestService(BasePaginableHolderAdapter.LIMIT, melodiesAdapter.getOffset()));
    }

    private void initEndlessListener() {
        listener = new EndlessRecyclerOnScrollListener(recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore() {
                if (melodiesAdapter.isStopped()) return;
                if (isLoading) return;
                isLoading = true;
                lazyProgressView.setVisibility(View.VISIBLE);
                executeService(new MelodiesRestService(BasePaginableHolderAdapter.LIMIT, melodiesAdapter.getOffset()), true);
            }
        };
        recyclerView.addOnScrollListener(listener);
    }

    private void changeMode(int visualMode, boolean isInLandscape) {
        RecyclerView.LayoutManager layoutManager;
        int spanCount;
        if (visualMode == GRID_MODE) {
            spanCount = isInLandscape ? 3 : 2;
            layoutManager = new GridLayoutManager(getActivity(), spanCount);
        } else if (visualMode == LIST_MODE) {
            spanCount = 1;
            layoutManager = new LinearLayoutManager(getActivity());
        } else {
            throw new IntechException(MessageFormat.format("Illegal visual mode {0}. Can be {1} or {2}", visualMode, GRID_MODE, LIST_MODE));
        }
        if (decor != null) {
            recyclerView.removeItemDecoration(decor);
        } else {
            decor = new ItemOffsetDecoration(15, spanCount);
        }
        recyclerView.addItemDecoration(decor);

        if (listener != null) {
            listener.setmLinearLayoutManager(layoutManager);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(melodiesAdapter);
        melodiesAdapter.notifyDataSetChanged(); //Redraw all items for avoiding image lagging
        recyclerView.scrollToPosition(recyclerView.getCurrentPosition());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        changeMode(currentMode, isInLandscape());
    }

    @Subscribe
    public void onMelodiesReady(Melodies melodies) {
        stopLoading();
        List<Melody> melodyList = melodies.getMelodies();
        if (melodyList.size() < BasePaginableHolderAdapter.LIMIT) {
            melodiesAdapter.setStopped(true);
        }
        melodiesAdapter.setData(melodyList);
    }

    @Override
    public void onFailure(RestFault restFault) {
        stopLoading();
        super.onFailure(restFault);
    }

    private boolean isInLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void stopLoading() {
        isLoading = false;
        lazyProgressView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupLeftImage(null, null);
        setupCenterText(R.string.melodies);
    }

    private void setGridIcon() {
        setupRightImage(R.drawable.ic_grid_on_black_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListIcon();
                currentMode = LIST_MODE;
                changeMode(currentMode, isInLandscape());
            }
        });
    }

    private void setListIcon() {
        setupRightImage(R.drawable.ic_list_black_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGridIcon();
                currentMode = GRID_MODE;
                changeMode(currentMode, isInLandscape());
            }
        });
    }

}
