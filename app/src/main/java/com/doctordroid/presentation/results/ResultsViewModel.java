package com.doctordroid.presentation.results;

import androidx.lifecycle.ViewModel;

import com.doctordroid.common.util.ChatUtil;
import com.doctordroid.data.remote.ApiError;
import com.doctordroid.data.remote.RemoteDataSource;
import com.doctordroid.data.repository.ChatRepository;
import com.doctordroid.data.repository.ConditionRepository;
import com.doctordroid.entity.local.Chat;
import com.doctordroid.entity.local.LocalConditionInfo;
import com.doctordroid.entity.remote.response.ConditionResponse;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class ResultsViewModel extends ViewModel {

    private final ChatRepository chatRepository = ChatRepository.getInstance();
    private final ConditionRepository conditionRepository = ConditionRepository.getInstance();

    Chat getChat (String chatId) {
        return chatRepository.findChatById(chatId);
    }

    Single<LocalConditionInfo> getConditionInfo (String conditionId) {
        return Single.create(emitter -> {
            if (conditionRepository.isConditionLocal(conditionId))
                emitter.onSuccess(conditionRepository.getLocalConditionInfo(conditionId));
            else {
                conditionRepository.getRemoteConditionInfo(conditionId).subscribe(new SingleObserver<Response<ConditionResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(Response<ConditionResponse> response) {
                        if (!response.isSuccessful() || response.body() == null) {
                            ApiError error = RemoteDataSource.parseError(response);
                            emitter.onError(new Throwable(error.getMessage()));
                            return;
                        }

                        LocalConditionInfo localConditionInfo = ChatUtil.toLocalConditionInfo(response.body());
                        emitter.onSuccess(localConditionInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        emitter.onError(new Throwable("You are offline"));
                    }
                });
            }
        });
    }

}
