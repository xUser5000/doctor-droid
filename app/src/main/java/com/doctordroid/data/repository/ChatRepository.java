package com.doctordroid.data.repository;

import com.doctordroid.entity.local.Chat;
import com.doctordroid.entity.local.LocalEvidence;
import com.doctordroid.entity.local.LocalCondition;
import com.doctordroid.entity.local.LocalQuestion;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.rx.CollectionChange;
import io.realm.rx.ObjectChange;

public class ChatRepository {

    private static ChatRepository instance;

    private Realm realm;

    private ChatRepository () {
        realm = Realm.getDefaultInstance();
    }

    public static ChatRepository getInstance () {
        if (instance == null) instance = new ChatRepository();
        return instance;
    }

    public Chat findChatById (String chatId) {
        return realm.where(Chat.class).equalTo("id", chatId).findFirst();
    }

    public Observable<CollectionChange<RealmResults<Chat>>> getChats () {
        return realm.where(Chat.class).findAll().asChangesetObservable();
    }

    public void addChat (Chat chat) {
        realm.executeTransaction(realm -> realm.insert(chat));
    }

    public void deleteChat (String chatId) {
        realm.executeTransaction(realm -> findChatById(chatId).deleteFromRealm());
    }

    public void deleteChatData (String chatId) {
        realm.executeTransaction(realm -> {
            Chat chat = findChatById(chatId);
            chat.setAnswers(new RealmList<>());
            chat.setLocalConditions(new RealmList<>());
            chat.setLocalEvidence(new RealmList<>());
            RealmList<LocalQuestion> localQuestions = new RealmList<>();
            localQuestions.add(chat.getLocalQuestions().get(0));
            localQuestions.add(chat.getLocalQuestions().get(1));
            chat.setLocalQuestions(localQuestions);
        });
    }

    public Observable<ObjectChange<RealmObject>> getChat (String chatId) {
        return realm.where(Chat.class).equalTo("id", chatId).findFirst().asChangesetObservable();
    }

    public void setEvidence (String chatId, RealmList<LocalEvidence> localEvidenceList) {
        realm.executeTransaction(realm -> {
            Chat chat = findChatById(chatId);
            chat.setLocalEvidence(new RealmList<>());
            chat.getLocalEvidence().addAll(localEvidenceList);
        });
    }

    public void setConditions (String chatId, RealmList<LocalCondition> localConditionList) {
        realm.executeTransaction(realm -> {
            Chat chat = findChatById(chatId);
            chat.setLocalConditions(new RealmList<>());
            chat.getLocalConditions().addAll(localConditionList);
        });
    }

}
