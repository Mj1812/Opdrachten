package domain;

import gui.IQuizGameApplication;

public class Account {
    //region Fields
    private int id;
    private String name;
    private String password;
    private IQuizGameApplication application;
    //endregion

    //region Constructors
    public Account(int id, String name, String password, IQuizGameApplication application) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.application = application;
    }

    public Account(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public Account(int id,String name, IQuizGameApplication application) {
        this.id = id;
        this.name = name;
        this.application = application;

    }

    public Account(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //endregion

    //region Properties
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public IQuizGameApplication getApplication() {
        return application;
    }
    //endregion
}
