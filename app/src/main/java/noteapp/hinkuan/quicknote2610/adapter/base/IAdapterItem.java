package noteapp.hinkuan.quicknote2610.adapter.base;


public interface IAdapterItem<T> {

    void bindDataToView(T data, int position);
}
