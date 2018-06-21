package knf.kuma.retrofit;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;

import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import knf.kuma.commons.BypassUtil;
import knf.kuma.commons.Network;
import knf.kuma.commons.NoSSLOkHttpClient;
import knf.kuma.commons.PatternUtil;
import knf.kuma.database.CacheDB;
import knf.kuma.database.dao.AnimeDAO;
import knf.kuma.database.dao.RecentsDAO;
import knf.kuma.pojos.AnimeObject;
import knf.kuma.pojos.RecentObject;
import knf.kuma.pojos.Recents;
import pl.droidsonroids.retrofit2.JspoonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import xdroid.toaster.Toaster;

@Singleton
public class Repository {
    public Repository() {
    }

    public void reloadRecents(Context context) {
        if (Network.isConnected()) {
            getFactoryBack("https://animeflv.net/").getRecents(BypassUtil.getStringCookie(context), BypassUtil.userAgent).enqueue(new Callback<Recents>() {
                @Override
                public void onResponse(@NonNull Call<Recents> call, @NonNull Response<Recents> response) {
                    try {
                        if (response.isSuccessful() && response.code() == 200) {
                            final List<RecentObject> objects = RecentObject.create(response.body().list);
                            RecentsDAO dao = CacheDB.INSTANCE.recentsDAO();
                            dao.clear();
                            dao.setCache(objects);
                        } else {
                            onFailure(call, new Exception("HTTP " + String.valueOf(response.code())));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onFailure(call, e);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Recents> call, @NonNull Throwable t) {
                    Toaster.toast("Error al obtener - " + t.getMessage());
                    t.printStackTrace();
                    Crashlytics.logException(t);
                }
            });
        }
    }

    public LiveData<AnimeObject> getAnime(Context context, final String link, final boolean persist) {
        String base = link.substring(0, link.lastIndexOf("/") + 1);
        String rest = link.substring(link.lastIndexOf("/") + 1);
        final MutableLiveData<AnimeObject> data = new MutableLiveData<>();
        final AnimeDAO dao = CacheDB.INSTANCE.animeDAO();
        if (!Network.isConnected() && dao.existLink(link))
            return CacheDB.INSTANCE.animeDAO().getAnime(link);
        getFactory(base).getAnime(BypassUtil.getStringCookie(context), BypassUtil.userAgent, rest).enqueue(new Callback<AnimeObject.WebInfo>() {
            @Override
            public void onResponse(@NonNull Call<AnimeObject.WebInfo> call, @NonNull Response<AnimeObject.WebInfo> response) {
                if (response.body() == null || response.code() != 200) {
                    data.setValue(CacheDB.INSTANCE.animeDAO().getAnimeRaw(link));
                    return;
                }
                AnimeObject animeObject = new AnimeObject(link, response.body());
                if (persist)
                    dao.insert(animeObject);
                data.setValue(animeObject);
            }

            @Override
            public void onFailure(@NonNull Call<AnimeObject.WebInfo> call, @NonNull Throwable t) {
                t.printStackTrace();
                data.setValue(CacheDB.INSTANCE.animeDAO().getAnimeRaw(link));
            }
        });
        return data;
    }

    public LiveData<PagedList<AnimeObject>> getAnimeDir(Context context) {
        switch (PreferenceManager.getDefaultSharedPreferences(context).getInt("dir_order", 0)) {
            default:
            case 0:
                return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getAnimeDir(), 25).build();
            case 1:
                return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getAnimeDirVotes(), 25).build();
        }
    }

    public LiveData<PagedList<AnimeObject>> getOvaDir(Context context) {
        switch (PreferenceManager.getDefaultSharedPreferences(context).getInt("dir_order", 0)) {
            default:
            case 0:
                return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getOvaDir(), 25).build();
            case 1:
                return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getOvaDirVotes(), 25).build();
        }
    }

    public LiveData<PagedList<AnimeObject>> getMovieDir(Context context) {
        switch (PreferenceManager.getDefaultSharedPreferences(context).getInt("dir_order", 0)) {
            default:
            case 0:
                return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getMovieDir(), 25).build();
            case 1:
                return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getMovieDirVotes(), 25).build();
        }
    }

    public LiveData<PagedList<AnimeObject>> getSearch() {
        return getSeacrh("");
    }

    public LiveData<PagedList<AnimeObject>> getSeacrh(String query) {
        if (query.equals("")) {
            return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getAll(), 25).build();
        } else if (query.trim().matches("^#\\d+$")) {
            return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearchID(query.replace("#", "")), 25).build();
        } else if (PatternUtil.isCustomSearch(query)) {
            return getFiltered(query, null);
        } else {
            return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearch("%" + query + "%"), 25).build();
        }
    }

    @NonNull
    private LiveData<PagedList<AnimeObject>> getFiltered(String query, @Nullable String genres) {
        String f_query = PatternUtil.getCustomSearch(query).trim();
        if (!f_query.equals(""))
            f_query = "%" + f_query + "%";
        else f_query = "%";
        switch (PatternUtil.getCustomAttr(query).toLowerCase()) {
            case "emision":
                if (genres == null)
                    return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearchS(f_query, "En emisión"), 25).build();
                else
                    return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearchSG(f_query, "En emisión", genres), 25).build();
            case "finalizado":
                if (genres == null)
                    return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearchS(f_query, "Finalizado"), 25).build();
                else
                    return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearchSG(f_query, "Finalizado", genres), 25).build();
            case "anime":
                if (genres == null)
                    return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearchTY(f_query, "Anime"), 25).build();
                else
                    return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearchTYG(f_query, "Anime", genres), 25).build();
            case "ova":
                if (genres == null)
                    return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearchTY(f_query, "OVA"), 25).build();
                else
                    return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearchTYG(f_query, "OVA", genres), 25).build();
            case "pelicula":
                if (genres == null)
                    return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearchTY(f_query, "Película"), 25).build();
                else
                    return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearchTYG(f_query, "Película", genres), 25).build();
            default:
                return genres == null ?
                        new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearch(f_query), 25).build() :
                        new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearchTG(f_query, genres), 25).build();
        }
    }

    public LiveData<PagedList<AnimeObject>> getSeacrh(String query, String genres) {
        if (query.equals("")) {
            return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearchG(genres), 25).build();
        } else if (PatternUtil.isCustomSearch(query)) {
            return getFiltered(query, genres);
        } else {
            return new LivePagedListBuilder<>(CacheDB.INSTANCE.animeDAO().getSearchTG("%" + query + "%", genres), 25).build();
        }
    }

    private Factory getFactory(String link) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .client(NoSSLOkHttpClient.get())
                .addConverterFactory(JspoonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(Factory.class);
    }

    private Factory getFactoryBack(String link) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .client(NoSSLOkHttpClient.get())
                .addConverterFactory(JspoonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .build();
        return retrofit.create(Factory.class);
    }
}
