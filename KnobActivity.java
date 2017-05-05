package com.flipflic.app.ui.knobs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.crashlytics.android.Crashlytics;
import com.flipflic.app.R;
import com.flipflic.app.ui.navigation.NavigationDrawerFragment;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import io.fabric.sdk.android.Fabric;
import javax.inject.Inject;

public class KnobActivity extends AppCompatActivity implements OnFragmentInteractionListener, HasFragmentInjector{

  @Inject DispatchingAndroidInjector<Fragment> fragmentInjector;

  @BindView(R.id.left_drawer) FrameLayout drawerList;
  @BindView(R.id.button_menu) ImageButton buttonMenu;
  @BindView(R.id.navigation) DrawerLayout drawerLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    Fabric.with(this, new Crashlytics());
    logUserToFabric();

    setContentView(R.layout.activity_knob);

    ButterKnife.bind(this);

    NavigationDrawerFragment drawer = new NavigationDrawerFragment();

    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.add(R.id.left_drawer, drawer);
    transaction.commit();


    FragmentTransaction pagerTransaction = getFragmentManager().beginTransaction();
    pagerTransaction.add(R.id.content_frame, new KnobPagerFragment());
    pagerTransaction.commit();

  }

  @OnClick(R.id.button_menu) public void onButtonMenuClick(){
    drawerLayout.openDrawer(Gravity.LEFT);
  }

  @Override public void onFragmentInteraction(KnobType type) {
    Fragment nextFragment;
    switch (type){
      case SCHEDULE:
        nextFragment = new SetScheduleFragment();
        break;
      default:

        nextFragment =  FlipListFragment.newInstance(type);
        break;
    }


    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.add(R.id.content_frame, nextFragment);
    transaction.commit();
  }

  @Override public void backToPager() {
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.remove(getFragmentManager().findFragmentById(R.id.content_frame)).commit();
  }

  private void logUserToFabric() {
    Crashlytics.setUserIdentifier(Build.VERSION.RELEASE);
  }

  @Override public DispatchingAndroidInjector<Fragment> fragmentInjector() {
    return fragmentInjector;
  }

}
