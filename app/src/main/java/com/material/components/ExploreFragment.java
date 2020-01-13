package com.material.components;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.JsonObject;
import com.material.components.adapter.AdapterListNews;
import com.material.components.api.SpektaAPI;
import com.material.components.api.SpektaInterface;
import com.material.components.data.DataGenerator;
import com.material.components.helper.DownloadImageTask;
import com.material.components.model.Image;
import com.material.components.model.News;
import com.material.components.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExploreFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private ExploreFragment.AdapterImageSlider adapterImageSlider;

    private String mParam1;
    private String mParam2;

    private Runnable runnable = null;
    private Handler handler = new Handler();

    private OnFragmentInteractionListener mListener;


    private RecyclerView recyclerView;
    private AdapterListNews mAdapter;

    private static String[] array_image_place = {
            "http://spektasolusi.com:1337/uploads/644969759f84409fab2d210f0ce4cdde.jpg",
            "http://spektasolusi.com:1337/uploads/f0562757c2694064bb9da9e1269fa9cf.jpg"
    };

    private static String[] array_title_place = {
            "Seagate Support Indonesia",
            "Quiz - Februari 2020"
    };

    private static String[] array_brief_place = {
            "Your One Stop Solution",
            "Klik untuk Detail dan Pemenang"
    };

    public ExploreFragment() {
    }

    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final View dView = getView();
        // diff between login and not login
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean isLogin = sharedPref.getBoolean("isLogin", false);
        ImageView stat01_icon = dView.findViewById(R.id.stat01_icon);
        TextView stat01_title = dView.findViewById(R.id.stat01_title);
        TextView stat01_value = dView.findViewById(R.id.stat01_value);
        if (isLogin) {
            stat01_icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_person));
            stat01_title.setText("title-login");
            stat01_value.setText("value-login");
        } else {
            stat01_icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_phone));
            stat01_title.setText("title-no login");
            stat01_value.setText("value-no login");
        }
        new DownloadImageTask((ImageView) dView.findViewById(R.id.dist_image01)).execute("http://spektasolusi.com:1337/uploads/955bb2a3974b449db7f8bcaadc742d6f.jpg");
        new DownloadImageTask((ImageView) dView.findViewById(R.id.dist_image02)).execute("http://spektasolusi.com:1337/uploads/6034c8dbe5974697817a795f8cc466fa.gif");
        new DownloadImageTask((ImageView) dView.findViewById(R.id.dist_image03)).execute("http://spektasolusi.com:1337/uploads/852481d0e5584ceb8f7273887871ac49.jpg");

        recyclerView = (RecyclerView) dView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        List<News> newsItems = DataGenerator.getNewsData(getContext(), 10);

        //set data and list adapter
        mAdapter = new AdapterListNews(getContext(), newsItems, R.layout.item_news_light);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListNews.OnItemClickListener() {
            @Override
            public void onItemClick(View view, News obj, int position) {
                Snackbar.make(dView.findViewById(R.id.parent_view), "Item " + obj.title + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        layout_dots = (LinearLayout) dView.findViewById(R.id.layout_dots);
        viewPager = (ViewPager) dView.findViewById(R.id.pager);
        adapterImageSlider = new ExploreFragment.AdapterImageSlider(getActivity(), new ArrayList<Image>());

        final List<Image> bannerItems = new ArrayList<>();
        for (int i = 0; i < array_image_place.length; i++) {
            Image obj = new Image();
            obj.url = array_image_place[i];
            obj.name = array_title_place[i];
            obj.brief = array_brief_place[i];
            bannerItems.add(obj);
        }

        adapterImageSlider.setItems(bannerItems);
        viewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        viewPager.setCurrentItem(0);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        ((TextView) dView.findViewById(R.id.title)).setText(bannerItems.get(0).name);
        ((TextView) dView.findViewById(R.id.brief)).setText(bannerItems.get(0).brief);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                TextView title = dView.findViewById(R.id.title);
                TextView brief = dView.findViewById(R.id.brief);
                if (title!=null) title.setText(bannerItems.get(pos).name);
                if (brief!=null) brief.setText(bannerItems.get(pos).brief);
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        startAutoSlider(adapterImageSlider.getCount());

        SpektaInterface api = new SpektaAPI().getInstance();
        Call<List<JsonObject>> call = api.getArticles();
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                if(response.isSuccessful()) {
                    List<JsonObject> objList = response.body();
                    List<News> items = new ArrayList<>();
                    // update banner on top
                    for (JsonObject obj:objList) {
                        News news = new News();
                        news.image = "http://spektasolusi.com:1337/" + obj.getAsJsonObject("image").get("url").getAsString();
                        news.title = obj.get("description").getAsString();
                        news.subtitle = obj.get("title").getAsString();
                        news.date = obj.get("createdAt").getAsString();
                        items.add(news);
                        Image objAdd = new Image();
                        objAdd.url = news.image;
                        objAdd.name = obj.get("title").getAsString();
                        objAdd.brief = news.date;
                        bannerItems.add(objAdd);
                        System.out.println("------------------>  " + obj.get("title") + ", image: " + obj.getAsJsonObject("image").get("url").getAsString());
                    }
                    adapterImageSlider.setItems(bannerItems);
                    viewPager.setAdapter(adapterImageSlider);

                    // displaying selected image first
                    viewPager.setCurrentItem(0);
                    addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
                    ((TextView) dView.findViewById(R.id.title)).setText(bannerItems.get(0).name);
                    ((TextView) dView.findViewById(R.id.brief)).setText(bannerItems.get(0).brief);
                    startAutoSlider(adapterImageSlider.getCount());

                    //set data and list adapter
                    mAdapter = new AdapterListNews(getContext(), items, R.layout.item_news_light);
                    recyclerView.setAdapter(mAdapter);
                    // on item list clicked
                    mAdapter.setOnItemClickListener(new AdapterListNews.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, News obj, int position) {
                            Snackbar.make(dView.findViewById(R.id.parent_view), "Item " + obj.subtitle + " clicked", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    System.out.println(response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        SpektaInterface spektaApi = new SpektaAPI().getInstance();
        JsonObject loginData = new JsonObject();
        loginData.addProperty("identifier", "admin");
        loginData.addProperty("password", "password");
        Call<JsonObject> callLogin = spektaApi.login(loginData);
        callLogin.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    JsonObject obj = response.body();
                    System.out.println("---- JWT -------------->  " + obj.get("jwt"));
                } else {
                    System.out.println(response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

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
        if (runnable != null) handler.removeCallbacks(runnable);
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        Context dContext = getContext();
        if (dContext != null) {
            ImageView[] dots = new ImageView[size];

            layout_dots.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new ImageView(dContext);
                int width_height = 15;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
                params.setMargins(10, 10, 10, 10);
                dots[i].setLayoutParams(params);
                dots[i].setImageResource(R.drawable.shape_circle_outline);
                layout_dots.addView(dots[i]);
            }

            if (dots.length > 0) {
                dots[current].setImageResource(R.drawable.shape_circle);
            }
        }
    }

    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 10000);
            }
        };
        handler.postDelayed(runnable, 10000);
    }


    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<Image> items;

        private ExploreFragment.AdapterImageSlider.OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, Image obj);
        }

        public void setOnItemClickListener(ExploreFragment.AdapterImageSlider.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Activity activity, List<Image> items) {
            this.act = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public Image getItem(int pos) {
            return items.get(pos);
        }

        public void setItems(List<Image> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final Image o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);

            ImageView image = (ImageView) v.findViewById(R.id.image);
            MaterialRippleLayout lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
            Tools.displayImageOriginal(act, image, o.url);
            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, o);
                    }
                }
            });

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

    }
}
