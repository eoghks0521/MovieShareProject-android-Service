// Generated code from Butter Knife. Do not modify!
package life.knowledge4.videotrimmersample;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RegistActivity_ViewBinding implements Unbinder {
  private RegistActivity target;

  private View view2131361840;

  @UiThread
  public RegistActivity_ViewBinding(RegistActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RegistActivity_ViewBinding(final RegistActivity target, View source) {
    this.target = target;

    View view;
    target.tv1 = Utils.findRequiredViewAsType(source, R.id.tv1, "field 'tv1'", EditText.class);
    target.tv2 = Utils.findRequiredViewAsType(source, R.id.tv2, "field 'tv2'", EditText.class);
    target.tv3 = Utils.findRequiredViewAsType(source, R.id.tv3, "field 'tv3'", EditText.class);
    view = Utils.findRequiredView(source, R.id.button, "method 'bt1_Click'");
    view2131361840 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.bt1_Click();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    RegistActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tv1 = null;
    target.tv2 = null;
    target.tv3 = null;

    view2131361840.setOnClickListener(null);
    view2131361840 = null;
  }
}
