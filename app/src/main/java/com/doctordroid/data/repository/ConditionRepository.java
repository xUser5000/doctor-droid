package com.doctordroid.data.repository;

import com.doctordroid.data.remote.Api;
import com.doctordroid.data.remote.RemoteDataSource;
import com.doctordroid.entity.local.LocalCondition;
import com.doctordroid.entity.local.LocalConditionInfo;
import com.doctordroid.entity.remote.response.ConditionResponse;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.Response;

public class ConditionRepository {

    private static final ConditionRepository ourInstance = new ConditionRepository();

    private Api api;
    private Realm realm;

    public static ConditionRepository getInstance() {
        return ourInstance;
    }

    private ConditionRepository() {
        api = RemoteDataSource.getInstance().getApi();
        realm = Realm.getDefaultInstance();
    }

    public boolean isConditionLocal (String conditionId) {
        return realm.where(LocalConditionInfo.class).equalTo("id", conditionId).findFirst() != null;
    }

    public void saveConditionInfo (LocalConditionInfo conditionInfo) {
        realm.executeTransaction(realm -> realm.insert(conditionInfo));
    }

    public LocalConditionInfo getLocalConditionInfo (String conditionId) {
        return realm.where(LocalConditionInfo.class).equalTo("id", conditionId).findFirst();
    }

    public Single<Response<ConditionResponse>> getRemoteConditionInfo (String conditionId) {
        return api
                .getConditionInfo(conditionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
