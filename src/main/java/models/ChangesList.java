package models;

/**
 * Created by hshankar on 12/13/19.
 */
public class ChangesList {
    private AppendOps append;
    private CreateOps create;
    private RemoveOps remove;

    public AppendOps getAppend() {
        return append;
    }

    public void setAppend(AppendOps append) {
        this.append = append;
    }

    public CreateOps getCreate() {
        return create;
    }

    public void setCreate(CreateOps create) {
        this.create = create;
    }

    public RemoveOps getRemove() {
        return remove;
    }

    public void setRemove(RemoveOps remove) {
        this.remove = remove;
    }
}
