package com.zdm.tablayout;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * ......................我佛慈悲....................
 * ......................_oo0oo_.....................
 * .....................o8888888o....................
 * .....................88" . "88....................
 * .....................(| -_- |)....................
 * .....................0\  =  /0....................
 * ...................___/`---'\___..................
 * ..................' \\|     |// '.................
 * ................./ \\|||  :  |||// \..............
 * .............../ _||||| -卍-|||||- \..............
 * ..............|   | \\\  -  /// |   |.............
 * ..............| \_|  ''\---/''  |_/ |.............
 * ..............\  .-\__  '-'  ___/-. /.............
 * ............___'. .'  /--.--\  `. .'___...........
 * .........."" '<  `.___\_<|>_/___.' >' ""..........
 * ........| | :  `- \`.;`\ _ /`;.`/ - ` : | |.......
 * ........\  \ `_.   \_ __\ /__ _/   .-` /  /.......
 * ....=====`-.____`.___ \_____/___.-`___.-'=====....
 * ......................`=---='.....................
 * <p>
 * ..................佛祖开光 ,永无BUG................
 * <p>
 * <p>
 * <p>
 * <p>
 * 描述:
 */

public class TabLayout extends LinearLayout {

    private int tabResId;
    private int tabCount;
    private boolean isScroll;
    //使用实体类中的颜色
    private boolean useEntityColor;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private TabRecylerAdapter tabAdapter;
    private List<Fragment> fragmentList;
    private int containerViewId;
    private ViewPager viewPager;
    private TabViewPagerAdapter pagerAdapter;
    private int lineSelectColor;
    private int lineUnSelectColor;
    private int offset = 50;
    private TabInterface.OnTablayoutItemClickListener tabItemClickListener;
    private TabInterface.OnItemClickListener itemClickListener;
    private TabInterface.OnPageChangeListener onPageChangeListener;
    private TabInterface.OnPageSelectedListener onPageSelectedListener;
    private TabInterface.OnItemBindViewDataListener itemBindViewDataListener;
    private int titleSelectColor;
    private int titleUnSelectColor;
    private int subtitleSelectColor;
    private int subtitleUnSelectColor;
    private int titleUnSelectSize;
    private int subtitleUnSelectSize;
    private int subtitleSelectSize;
    private int titleSelectSize;
    private boolean isHaveBulge;
    private int bulgeIndex;


    public void addOnItemClickListener(TabInterface.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void addItemBindViewDataListener(TabInterface.OnItemBindViewDataListener itemBindViewDataListener) {
        this.itemBindViewDataListener = itemBindViewDataListener;
    }

    public void setOnTabItemClickListener(TabInterface.OnTablayoutItemClickListener itemClickListener) {
        this.tabItemClickListener = tabItemClickListener;
    }

    public void setOnPageChangeListener(TabInterface.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void addOnPageSelectedListener(TabInterface.OnPageSelectedListener onPageSelectedListener) {
        this.onPageSelectedListener = onPageSelectedListener;
    }

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabLayout);

        tabResId = typedArray.getResourceId(R.styleable.TabLayout_tabResId, -1);

        isScroll = typedArray.getBoolean(R.styleable.TabLayout_isScroll, false);

        useEntityColor = typedArray.getBoolean(R.styleable.TabLayout_useEntityColor, false);

        //        isHaveBulge = typedArray.getBoolean(R.styleable.TabLayout_isHaveBulge, false);
        //        bulgeIndex = typedArray.getInt(R.styleable.TabLayout_bulgeIndex, 0);

        lineSelectColor = typedArray.getColor(R.styleable.TabLayout_lineSelectColor, Color.parseColor("#ffffff"));
        lineUnSelectColor = typedArray.getColor(R.styleable.TabLayout_lineUnSelectColor, Color.TRANSPARENT);

        titleSelectColor = typedArray.getColor(R.styleable.TabLayout_titleSelectColor, 0);
        titleUnSelectColor = typedArray.getColor(R.styleable.TabLayout_titleUnSelectColor, titleSelectColor);
        subtitleSelectColor = typedArray.getColor(R.styleable.TabLayout_subtitleSelectColor, 0);
        subtitleUnSelectColor = typedArray.getColor(R.styleable.TabLayout_subtitleUnSelectColor, subtitleSelectColor);

        titleSelectSize = (int) typedArray.getDimension(R.styleable.TabLayout_titleSelectSize, 0);
        titleUnSelectSize = (int) typedArray.getDimension(R.styleable.TabLayout_titleUnSelectSize, titleSelectSize);
        subtitleSelectSize = (int) typedArray.getDimension(R.styleable.TabLayout_subtitleSelectSize, 0);
        subtitleUnSelectSize = (int) typedArray.getDimension(R.styleable.TabLayout_subtitleUnSelectSize, subtitleSelectSize);

        typedArray.recycle();

        if (tabResId == -1) {
            throw (new NullPointerException("tabResId资源未发现"));
        }
        //设置了item凸出，则当前的tablayout无法滑动
        if (isHaveBulge)
            isScroll = false;

        mRecycler = new RecyclerView(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRecycler.setLayoutParams(params);
        addView(mRecycler);

        tabAdapter = new TabRecylerAdapter(getContext(), tabResId);
        mRecycler.setAdapter(tabAdapter);
        //移除更新动画
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);

        tabAdapter.setOnItemClickListener(new TabInterface.OnItemClickListener() {
            @Override
            public void OnItemClickListener(View v, int position) {
                tabAdapter.selectPosition(position);
                //自定义item点击事件
                if (tabItemClickListener != null) {
                    tabItemClickListener.OnItemClickListener(v, position, tabCount);
                } else if (viewPager == null) {
                    relationFragment(position);
                } else {
                    viewPager.setCurrentItem(position);
                }

                if(itemClickListener!=null){
                    itemClickListener.OnItemClickListener(v,position);
                }
            }
        });


        tabAdapter.setOnItemBindViewDataListener(mItemBindViewDataListener);
    }

    /**
     * 设置条目数据
     */
    TabInterface.OnItemBindViewDataListener mItemBindViewDataListener = new TabInterface.OnItemBindViewDataListener() {
        @Override
        public void OnItemBindViewDataListener(TabRecylerAdapter.ViewHolder holder, TabEntity tabEntity,
                                               int selectPosition, int position) {

            if (selectPosition == position) {
                //title文字颜色
                if (holder.title != null) {
                    if (useEntityColor) {
                        holder.title.setTextColor(tabEntity.getTitleSelectColor());
                    } else if (titleSelectColor != 0) {
                        holder.title.setTextColor(titleSelectColor);
                    }
                }
                //title文字大小切换
                if (holder.title != null && titleSelectSize > 0) {
                    holder.title.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSelectSize);
                }

                if (holder.subtitle != null) {
                    if (useEntityColor) {
                        holder.subtitle.setTextColor(tabEntity.getTitleSelectColor());
                    } else if (subtitleSelectColor != 0) {
                        holder.subtitle.setTextColor(subtitleSelectColor);
                    }
                }

                if (holder.subtitle != null && subtitleSelectSize > 0) {
                    holder.subtitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, subtitleSelectSize);
                }

                if (holder.image != null)
                    holder.image.setImageResource(tabEntity.getSelectimgResId());

                if (holder.line != null)
                    holder.line.setBackgroundColor(useEntityColor ? tabEntity.getLineSelectColor() : lineSelectColor);
            } else {
                //title文字颜色
                if (holder.title != null) {
                    if (useEntityColor) {
                        holder.title.setTextColor(tabEntity.getTitleSelectColor());
                    } else if (titleUnSelectColor != 0) {
                        holder.title.setTextColor(titleUnSelectColor);
                    }
                }

                //title文字大小切换
                if (holder.title != null && titleUnSelectSize > 0) {
                    holder.title.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleUnSelectSize);
                }

                if (holder.subtitle != null) {
                    if (useEntityColor) {
                        holder.subtitle.setTextColor(tabEntity.getTitleSelectColor());
                    } else if (subtitleUnSelectColor != 0) {
                        holder.subtitle.setTextColor(subtitleUnSelectColor);
                    }
                }

                if (holder.subtitle != null && subtitleUnSelectSize > 0) {
                    holder.subtitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, subtitleUnSelectSize);
                }

                if (holder.image != null)
                    holder.image.setImageResource(tabEntity.getUnSelectimgResId());

                if (holder.line != null)
                    holder.line.setBackgroundColor(useEntityColor ? tabEntity.getLineUnSelectColor() : lineUnSelectColor);
            }

            if (holder.title != null)
                holder.title.setText(tabEntity.getTitle());

            if (holder.subtitle != null)
                holder.subtitle.setText(tabEntity.getSubTitle());

            if (itemBindViewDataListener != null) {
                itemBindViewDataListener.OnItemBindViewDataListener(holder, tabEntity, selectPosition, position);
            }
        }
    };

    //关联ViewPager
    private void relationViewPager() {
        if (viewPager == null)
            return;
        FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        pagerAdapter = new TabViewPagerAdapter(fm, fragmentList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(mPageChangeListener);
    }

    ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageSelected(position);
            } else {
                setTablayoutCurrentItem(position);
                //额外处理其他操作
                if (onPageSelectedListener != null) {
                    onPageSelectedListener.onPageSelected(position);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };

    /**
     * 设置选中并滚动到选中的item
     * @param position
     */
    private void setTablayoutCurrentItem(int position) {
        if (layoutManager instanceof LinearLayoutManager) {
            //滚动偏移，避免屏幕显示的item让用户认为后面没有选项卡
            ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(position, offset);
        }
        tabAdapter.selectPosition(position);
    }

    //关联fragment
    private void relationFragment(int position) {
        if (fragmentList == null || fragmentList.isEmpty())
            return;

        addShowHideFragment(fragmentList.get(position));
    }

    Fragment currentFragment;
    List<Fragment> hasAddedFragment = new ArrayList<>();

    private void addShowHideFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = ((AppCompatActivity) getContext())
                .getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        if (hasAddedFragment.contains(fragment)) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(containerViewId, fragment);
            hasAddedFragment.add(fragment);
        }
        fragmentTransaction.commit();
        currentFragment = fragment;
    }

    /**
     * 不绑定fragment viewpager
     *
     * @param list
     */
    public void bindViewData(List<TabEntity> list) {
        tabCount = list.size();
        if (isScroll) {
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            layoutManager = new GridLayoutManager(getContext(), tabCount);
        }
        mRecycler.setLayoutManager(layoutManager);

        tabAdapter.setList(list);
    }

    /**
     * 数据刷新
     */
    public void notifyDataSetChanged() {
       tabAdapter.notifyDataSetChanged();
    }

    /**
     * 数据刷新
     */
    public void notifyItemChanged(int position) {
        tabAdapter.notifyItemChanged(position);
    }

    /**
     * 绑定fragment
     *
     * @param list
     * @param fragmentList
     * @param containerViewId
     */
    public void bindViewData(List<TabEntity> list, List<Fragment> fragmentList, @IdRes int containerViewId) {
        bindViewData(list);
        this.fragmentList = fragmentList;
        this.containerViewId = containerViewId;
    }

    /**
     * 绑定viewPager
     *
     * @param list
     * @param viewPager
     * @param fragmentList
     */
    public void bindViewData(List<TabEntity> list, ViewPager viewPager, List<Fragment> fragmentList) {
        bindViewData(list);
        this.viewPager = viewPager;
        this.fragmentList = fragmentList;
    }

    /**
     * 设置默认选中
     *
     * @param position
     */
    public void defaultSelected(int position) {
//        tabAdapter.selectPosition(position);
        setTablayoutCurrentItem(position);
        if (viewPager == null)
            relationFragment(position);
        else {
            relationViewPager();
            viewPager.setCurrentItem(position);
        }
    }

    /**
     * 设置默认选中，并且设置tabLayout滑动的偏移量
     *
     * @param position
     * @param offset
     */
    public void defaultSelected(int position, int offset) {
        defaultSelected(position);
        this.offset = offset;
    }
}