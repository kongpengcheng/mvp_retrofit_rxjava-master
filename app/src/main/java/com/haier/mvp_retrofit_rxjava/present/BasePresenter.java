package com.haier.mvp_retrofit_rxjava.present;

import com.haier.mvp_retrofit_rxjava.date.DataManager;
import com.haier.mvp_retrofit_rxjava.view.BaseView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import rx.subscriptions.CompositeSubscription;

/**
 * BasePresenter
 *
 * @autor kong
 * 此处运用了若引用+配合activtiy的生命周期进行对activity的引用进行销毁，防止内存泄漏
 * created at 2016/6/24 13:52
 */
public abstract class BasePresenter<T extends BaseView> implements IBasePresenter<T> {
    protected Reference<T> mViewRef;
    // private T mView;
    public CompositeSubscription mCompositeSubscription;
    public DataManager mDataManager;
    public boolean isFirstOpen;

    @Override
    public void attachView(T mvpView) {
        mViewRef = new WeakReference<T>(mvpView);
        //  this.mView = mvpView;
        this.mCompositeSubscription = new CompositeSubscription();
        this.mDataManager = DataManager.getInstance();
        this.isFirstOpen = true;
    }


    @Override
    public void detachView() {
        //  this.mView = null;
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        this.mCompositeSubscription.unsubscribe();
        this.mCompositeSubscription = null;
        this.mDataManager = null;
    }

    public abstract void loadData(boolean isLoad);

    /**
     * 判断是否present和 view 是否建立连接
     */
    public boolean isViewAttached() {
//        return mView != null;
        return mViewRef != null && mViewRef.get() != null;
    }

    /**
     * 获取建立连接的view
     */
    public T getBaseView() {
        // return mView;
        return mViewRef.get();
    }


    public void checkViewAttached() {
        if (!isViewAttached()) throw new BaseViewNotAttachedException();
    }

    public boolean isFirstOpen() {
        return isFirstOpen;
    }

    public void setFirstOpen(boolean firstOpen) {
        isFirstOpen = firstOpen;
    }

    public static class BaseViewNotAttachedException extends RuntimeException {
        public BaseViewNotAttachedException() {
            super("Please call IBasePresenter.attachView(IBase) before" +
                    " requesting data to the IBasePresenter");
        }
    }
}
