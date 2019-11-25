package com.doctordroid.common.util;

import com.doctordroid.entity.local.Answer;
import com.doctordroid.entity.local.Chat;
import com.doctordroid.entity.local.LocalConditionInfo;
import com.doctordroid.entity.local.LocalEvidence;
import com.doctordroid.entity.local.LocalCondition;
import com.doctordroid.entity.local.LocalQuestion;
import com.doctordroid.entity.remote.model.Item;
import com.doctordroid.entity.remote.model.RemoteCondition;
import com.doctordroid.entity.remote.model.RemoteEvidence;
import com.doctordroid.entity.remote.model.RemoteQuestion;
import com.doctordroid.entity.remote.response.ConditionResponse;
import com.doctordroid.entity.remote.response.ParseResponse;
import com.doctordroid.presentation.chat.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.realm.RealmList;

public final class ChatUtil {

    public static Message toMessage (LocalQuestion localQuestion) {
        return new Message(localQuestion.getTime(), localQuestion.getText(), Message.Sender.ROBOT);
    }

    public static Message toMessage (Answer answer) {
        return new Message(answer.getTime(), answer.getText(), Message.Sender.USER);
    }

    public static List<Message> sort (List<Message> messages) {
        Collections.sort(messages);
        return messages;
    }

    public static List<Message> getMessages (Chat chat) {
        List<Message> messages = new ArrayList<>();
        for (Answer answer: chat.getAnswers()) messages.add(toMessage(answer));
        for (LocalQuestion localQuestion : chat.getLocalQuestions()) messages.add(toMessage(localQuestion));
        return sort(messages);
    }

    public static boolean isCompleted (Chat chat) {
        for (LocalCondition localCondition : chat.getLocalConditions()) {
            if (localCondition.getProbability() > 0.9) return true;
        }
        return false;
    }

    public static LocalQuestion toLocalQuestion (RemoteQuestion remoteQuestion) {
        Item item = remoteQuestion.getItems().get(0);
        LocalQuestion localQuestion = new LocalQuestion(
                System.currentTimeMillis(),
                LocalQuestion.Type.SINGLE,
                remoteQuestion.getText()
        );
        localQuestion.setId(item.getId());
        return localQuestion;
    }

    public static RealmList<LocalEvidence> toLocalEvidenceList (List<ParseResponse.Mention> mentions) {
        RealmList<LocalEvidence> localEvidenceList = new RealmList<>();
        for (ParseResponse.Mention mention: mentions) {
            LocalEvidence localEvidence = new LocalEvidence(mention.id, mention.choice_id);
            localEvidenceList.add(localEvidence);
        }
        return localEvidenceList;
    }

    public static List<RemoteEvidence> toRemoteEvidenceList (RealmList<LocalEvidence> localEvidenceRealmList) {
        List<RemoteEvidence> remoteEvidenceList = new ArrayList<>();
        for (LocalEvidence localEvidence: localEvidenceRealmList) {
            RemoteEvidence remoteEvidence = new RemoteEvidence(localEvidence.getId(), localEvidence.getChoice_id());
            remoteEvidenceList.add(remoteEvidence);
        }
        return remoteEvidenceList;
    }

    public static RealmList<LocalCondition> toLocalConditionsList (List<RemoteCondition> remoteConditions) {
        RealmList<LocalCondition> conditionsList = new RealmList<>();
        for (RemoteCondition remoteCondition: remoteConditions) {
            LocalCondition localCondition = new LocalCondition(
                    remoteCondition.getId(),
                    remoteCondition.getName(),
                    remoteCondition.getProbability()
            );
            conditionsList.add(localCondition);
        }
        return conditionsList;
    }

    public static List<RemoteCondition> toRemoteConditionsList (RealmList<LocalCondition> localConditions) {
        List<RemoteCondition> remoteConditions = new ArrayList<>();
        for (LocalCondition localCondition: localConditions) {
            RemoteCondition remoteCondition = new RemoteCondition(
                    localCondition.getId(),
                    localCondition.getName(),
                    localCondition.getProbability()
            );
            remoteConditions.add(remoteCondition);
        }
        return remoteConditions;
    }

    public static String fromStringListToString (List<String> stringList) {
        StringBuilder builder = new StringBuilder();
        for (String str: stringList) builder.append(str).append(" ");
        String text = builder.toString();
        text = text.trim().replace(" ", ", ");
        return text;
    }

    public static LocalConditionInfo toLocalConditionInfo (ConditionResponse remoteCondition) {
        LocalConditionInfo localCondition = new LocalConditionInfo();
        localCondition.setId(remoteCondition.getId());
        localCondition.setName(remoteCondition.getName());
        localCondition.setGenderFilter(remoteCondition.getGenderFilter());
        localCondition.setAcuteness(remoteCondition.getAcuteness());
        RealmList<String> categories = new RealmList<>();
        categories.addAll(remoteCondition.getCategories());
        localCondition.setCategories(categories);
        localCondition.setHint(remoteCondition.getExtras().hint);
        localCondition.setPrevalence(remoteCondition.getPrevalence());
        localCondition.setSeverity(remoteCondition.getSeverity());
        return localCondition;
    }
}
