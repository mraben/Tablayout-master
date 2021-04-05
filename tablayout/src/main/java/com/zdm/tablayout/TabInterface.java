package com.zdm.tablayout;

import android.view.View;

/**
 * 描述：
 */

public class TabInterface {

    public interface OnItemClickListener {
        void OnItemClickListener(View v, int position);
    }


    public interface OnItemBindViewDataListener {
        /**
         * 会在tablayout的切换按钮逻辑代码之后调用次接口，不会覆盖tablayout的逻辑代码
         * @param holder
         * @param tabEntity   当前item的实体类
         * @param selectPosition  当前选中的item 此索引为选中的索引
         * @param position    当前的索引
         */
        void OnItemBindViewDataListener(TabRecylerAdapter.ViewHolder holder,TabEntity tabEntity, int selectPosition, int position);
    }

    /**
     * 会覆盖TabLayout的方法 需要自己实现切换效果
     */
    public interface OnTablayoutItemClickListener {
        void OnItemClickListener(View v, int position, int count);
    }

    /**
     * 实现ViewPager的OnPageChangeListener 会覆盖TabLayout的方法
     */
    public interface OnPageChangeListener {

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }

    /**
     * 实现ViewPager的OnPageChangeListener中的onPageSelected   不会覆盖TabLayout的方法
     */
    public interface OnPageSelectedListener {

        public void onPageSelected(int position);
    }
}

