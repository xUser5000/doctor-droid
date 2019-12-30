package com.doctordroid.presentation.chat;

import androidx.lifecycle.ViewModel;

import com.doctordroid.common.util.ChatUtil;
import com.doctordroid.data.remote.ApiError;
import com.doctordroid.data.remote.RemoteDataSource;
import com.doctordroid.data.repository.ChatRepository;
import com.doctordroid.data.repository.MessageRepository;
import com.doctordroid.entity.local.Answer;
import com.doctordroid.entity.local.Chat;
import com.doctordroid.entity.local.LocalEvidence;
import com.doctordroid.entity.local.LocalQuestion;
import com.doctordroid.entity.remote.model.RemoteEvidence;
import com.doctordroid.entity.remote.request.DiagnosisRequest;
import com.doctordroid.entity.remote.request.ParseRequest;
import com.doctordroid.entity.remote.response.DiagnosisResponse;
import com.doctordroid.entity.remote.response.ParseResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.rx.ObjectChange;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Response;

@Getter
@Setter
public class ChatViewModel extends ViewModel {

    public static final class YesNoAnswer {
        public static final String YES = "present";
        public static final String NO = "present";
        public static final String DONT_KNOW = "unknown";
    }

    private final ChatRepository chatRepository = ChatRepository.getInstance();
    private final MessageRepository messageRepository = MessageRepository.getInstance();

    private String chatId;

    private ChatInterface chatInterface;

    Observable<ObjectChange<RealmObject>> getChat(String chatId) {
        return chatRepository.getChat(chatId);
    }

    // delete all chat messages and start from the beginning
    void deleteAllMessages() {
        chatRepository.deleteChatData(chatId);
    }

    // process free text
    Single<ParseResponse> parse(String freeText) {

        Answer answer = new Answer(System.currentTimeMillis(), freeText);
        messageRepository.addAnswer(chatId, answer);

        ParseRequest request = new ParseRequest(freeText);
        return Single.create(emitter -> messageRepository.parse(request).subscribe(new SingleObserver<Response<ParseResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(Response<ParseResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    ApiError error = RemoteDataSource.parseError(response);
                    emitter.onError(new Throwable("Something went wrong on the server: " + response.code()));
                    return;
                }

                ParseResponse body = response.body();

                // add the next remoteQuestion
                long time = System.currentTimeMillis();
                if (body.getMentions().isEmpty() || !body.isObvious()) {
                    LocalQuestion localQuestion1 = new LocalQuestion(time, LocalQuestion.Type.FREE_TEXT, "I have not understood some words");
                    LocalQuestion localQuestion2 = new LocalQuestion(time + 1, LocalQuestion.Type.FREE_TEXT, "Please be more accurate");

                    messageRepository.addQuestion(chatId, localQuestion1);
                    messageRepository.addQuestion(chatId, localQuestion2);
                } else {
                    LocalQuestion localQuestion = new LocalQuestion(time, LocalQuestion.Type.INFO, "Got it !");

                    // add the localQuestion to the chat
                    messageRepository.addQuestion(chatId, localQuestion);

                    // add the localEvidence info
                    RealmList<LocalEvidence> localEvidenceRealmList = ChatUtil.toLocalEvidenceList(body.getMentions());
                    chatRepository.setEvidence(chatId, localEvidenceRealmList);

                    Chat chat = chatRepository.findChatById(chatId);
                    diagnose(chat);
                }

                emitter.onSuccess(body);
            }

            @Override
            public void onError(Throwable e) {
                emitter.onError(new Throwable("You are offline"));
            }
        }));
    }

    private void diagnose (Chat chat) {
        List<RemoteEvidence> remoteEvidenceList = ChatUtil.toRemoteEvidenceList(chat.getLocalEvidence());
        DiagnosisRequest request = new DiagnosisRequest(chat.getGender(), chat.getAge(), remoteEvidenceList);

        chatInterface.startLoading();
        messageRepository.diagnose(request).subscribe(new SingleObserver<Response<DiagnosisResponse>>() {

            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onSuccess(Response<DiagnosisResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    ApiError error = RemoteDataSource.parseError(response);
                    return;
                }

                DiagnosisResponse body = response.body();

                chatRepository.setConditions(chatId, ChatUtil.toLocalConditionsList(body.getRemoteConditions()));

                Chat chat = chatRepository.findChatById(chatId);

                if (ChatUtil.isCompleted(chat)) return;

                LocalQuestion localQuestion = ChatUtil.toLocalQuestion(body.getRemoteQuestion());
                messageRepository.addQuestion(chatId, localQuestion);
            }

            @Override
            public void onError(Throwable e) {
                chatInterface.showToast("You are offline");
            }
        });
    }

    void answerYesNoQuestion (String realText, String response) {
        Answer answer = new Answer(System.currentTimeMillis(), realText);
        messageRepository.addAnswer(chatId, answer);
        Chat chat = chatRepository.findChatById(chatId);
        String conditionId = chat.getLocalQuestions().last().getId();
        LocalEvidence localEvidence = new LocalEvidence(conditionId, response);
        RealmList<LocalEvidence> localEvidenceRealmList = new RealmList<>();
        localEvidenceRealmList.addAll(chat.getLocalEvidence());
        localEvidenceRealmList.add(localEvidence);
        chatRepository.setEvidence(chatId, localEvidenceRealmList);
        diagnose(chat);
    }
}