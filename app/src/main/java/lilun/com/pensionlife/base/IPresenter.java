package lilun.com.pensionlife.base;

/**
 * Created by youke on 2016/12/29.
 * P层顶级接口
 */
public interface IPresenter<T>  {
    void bindView(T view);
    void unBindView();
}
