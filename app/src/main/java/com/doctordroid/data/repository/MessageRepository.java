package com.doctordroid.data.repository;

import android.util.Log;

import com.doctordroid.data.remote.Api;
import com.doctordroid.data.remote.RemoteDataSource;
import com.doctordroid.entity.local.Answer;
import com.doctordroid.entity.local.Chat;
import com.doctordroid.entity.local.LocalQuestion;
import com.doctordroid.entity.remote.request.DiagnosisRequest;
import com.doctordroid.entity.remote.request.ParseRequest;
import com.doctordroid.entity.remote.response.DiagnosisResponse;
import com.doctordroid.entity.remote.response.ParseResponse;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.Response;

public class MessageRepository {

    private static final MessageRepository ourInstance = new MessageRepository();

    private Api api;
    private Realm realm;

    public static MessageRepository getInstance() {
        return ourInstance;
    }

    private MessageRepository() {
        api = RemoteDataSource.getInstance().getApi();
        realm = Realm.getDefaultInstance();
    }

    private Chat findChatById (String chatId) {
        return realm.where(Chat.class).equalTo("id", chatId).findFirst();
    }

    public void addAnswer (String chatId, Answer answer) {
        Chat chat = findChatById(chatId);
        realm.executeTransaction(realm -> chat.getAnswers().add(answer));
    }

    public void addQuestion (String chatId, LocalQuestion localQuestion) {
        Chat chat = findChatById(chatId);
        realm.executeTransaction(realm -> chat.getLocalQuestions().add(localQuestion));
    }

    public Single<Response<ParseResponse>> parse (ParseRequest request) {
        return api
                .parseFreeText(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Response<DiagnosisResponse>> diagnose (DiagnosisRequest request) {
        return api
                .diagnose(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
