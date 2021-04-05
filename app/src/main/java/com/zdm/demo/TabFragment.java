package com.zdm.demo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

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

public class TabFragment extends Fragment {

    @BindView(R.id.text)
    TextView text;
    private int position;
    private int type;

    public static Fragment getTabFragment(int index,int type) {
        TabFragment fragment = new TabFragment();
        fragment.position = index;
        fragment.type = type;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, null);
        ButterKnife.bind(this, view);


        text.setText(type==1?Constants.title[position]:Constants.viewPagerTitle[position]);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
