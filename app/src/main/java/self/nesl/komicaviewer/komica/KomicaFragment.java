package self.nesl.komicaviewer.komica;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import self.nesl.forumviewer.ui.board_komica.KomicaChildFragment;
import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Web;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KomicaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KomicaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KomicaFragment extends Fragment{
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final Web web = new Web("Komica");

    // Rename and change types of parameters
    private Web mWeb;

    private OnFragmentInteractionListener mListener;

    public KomicaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment KomicaFragment.
     */
    // Rename and change types and number of parameters
    public static KomicaFragment newInstance() {
        KomicaFragment fragment = new KomicaFragment();
        String domain="https://www.komica.org/";

        web.setDomainUrl(domain)
                .setMenuUrl(domain+"bbsmenu.html")
                .setTop50BoardUrl(domain+"mainmenu2018.html")
                .setAllBoardPrefName("komica_board_urls")
                .setTop50BoardPrefName("komica_top50_board_urls");
        Bundle args = new Bundle();
        args.putSerializable(String.valueOf(web), web);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWeb = (Web)getArguments().getSerializable(String.valueOf(web));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_tab, container, false);

        // nesl
        // ViewPager-Fragment
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.addAll(Arrays.asList(KomicaChildFragment.newInstance(0,web),KomicaChildFragment.newInstance(1,web)));
        BoardlistFragmentAdapter myFragmentAdapter = new BoardlistFragmentAdapter(getActivity(),fragmentList,getChildFragmentManager());
        /*
        getChildFragmentManager(): https://blog.csdn.net/TaooLee/article/details/50633619
        getFragmentManager到的是activity对所包含fragment的Manager，
        而如果是fragment嵌套fragment，那么就需要利用getChildFragmentManager()了。
         */

        // ViewPager
        ViewPager viewPager = v.findViewById(R.id.view_pager);
        viewPager.setAdapter(myFragmentAdapter);
        TabLayout tabs = v.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        return v;
    }

    // Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class BoardlistFragmentAdapter extends FragmentPagerAdapter {

        //    @StringRes
        private String[] TAB_TITLES;
        private final Context mContext;
        private List<Fragment> fragmentList;

        public BoardlistFragmentAdapter(Context context, List<Fragment> fragmentList, FragmentManager fm) {
            super(fm);
            mContext = context;
            TAB_TITLES=mContext.getResources().getStringArray(R.array.tab_komica_boardlist);
            this.fragmentList=fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a KomicaBoardsFragment (defined as a static inner class below).
            return fragmentList.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return fragmentList.size();
        }
    }
}
