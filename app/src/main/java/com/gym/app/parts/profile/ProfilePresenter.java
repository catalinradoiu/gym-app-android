package com.gym.app.parts.profile;

import com.gym.app.data.model.User;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.UserService;
import com.gym.app.utils.MvpObserver;

import javax.inject.Inject;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by flaviuoprea on 11/12/17.
 */

public class ProfilePresenter extends Presenter<ProfileView> {

    @Inject
    UserService userService;

    public ProfilePresenter(ProfileView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    public void getUser(){
        userService.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new MvpObserver<User>(this){
                    @Override
                    public void onNext(User value) {
                        getView().showUser(value);
                    }
                });
    }

    public void updateUser(String name, String password, MultipartBody.Part picture){
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody methodBody = RequestBody.create(MediaType.parse("text/plain"), "PUT");

        userService.updateUser(methodBody, nameBody, passwordBody, picture)
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().updateMessage();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().showError();
                    }
                });
    }



}
