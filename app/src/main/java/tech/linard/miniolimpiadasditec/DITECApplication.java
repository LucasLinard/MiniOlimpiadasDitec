package tech.linard.miniolimpiadasditec;

import android.app.Application;
import android.util.Log;

/**
 * Created by lucas on 08/04/17.
 */

public class DITECApplication extends Application {
    private static final String TAG = DITECApplication.class.getSimpleName();
    private static DITECApplication instance = null;

    private User currentUser;
    private Group currenGroup;
    private Modalidade curreModalidade;

    public static DITECApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "App onCreate");
    }


    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Group getCurrenGroup() {
        return currenGroup;
    }

    public void setCurrenGroup(Group currenGroup) {
        this.currenGroup = currenGroup;
    }

    public Modalidade getCurreModalidade() {
        return curreModalidade;
    }

    public void setCurreModalidade(Modalidade curreModalidade) {
        this.curreModalidade = curreModalidade;
    }
}

