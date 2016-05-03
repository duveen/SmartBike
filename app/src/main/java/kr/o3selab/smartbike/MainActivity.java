package kr.o3selab.smartbike;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import kr.o3selab.smartbike.fragment.ConnectSensorFragment;
import kr.o3selab.smartbike.fragment.ControlLedFragment;
import kr.o3selab.smartbike.fragment.MainFragment;
import kr.o3selab.smartbike.fragment.SpeedMeterFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 초기 생성자
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("스마트자전거");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // 초기 Fragment 설정
        FragmentManager fm = getSupportFragmentManager();

        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, DB.MAIN_TAG);
        fragmentTransaction.commit();
    }

    /**
     * BackButton 클릭시 호출되는 콜백 메소드
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // BackStackEntryCount에 아무것도 없을때 종료하기.
            if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
                new AlertDialog.Builder(this)
                        .setTitle("종료하시겠습니까?")
                        .setNegativeButton("취소", null)
                        .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.this.moveTaskToBack(true);
                                MainActivity.this.finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(0);
                            }
                        })
                        .show();
            } else super.onBackPressed();
        }
    }

    /**
     * 네비게이션 메뉴 클릭시 호출되는 콜백 메소드
     *
     * @param item 메뉴 아이템 정보
     * @return 정상 true, 비정상 false
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_home:
                selectFrag(DB.MAIN_FRAG);
                break;
            case R.id.nav_sppedmeter:
                selectFrag(DB.SPEED_FRAG);
                break;
            case R.id.nav_connect_led:
                selectFrag(DB.LED_FRAG);
                break;
            case R.id.nav_connect_sensor:
                selectFrag(DB.SENSOR_FRAG);
                break;
            default:
                selectFrag(DB.MAIN_FRAG);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    /**
     * Fragment 메뉴 선택시 Frag 이동
     *
     * @author (Duveen)
     * @param position Fragment 위치
     */
    public void selectFrag(int position){
        Fragment nextFragment;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        switch(position) {

            case DB.MAIN_FRAG:
                if(currentFragment.getTag().equals(DB.MAIN_TAG))
                    return;
                clearStack();
                nextFragment = new MainFragment();
                fragmentTransaction.replace(R.id.container, nextFragment, DB.MAIN_TAG);
                break;

            case DB.SPEED_FRAG:
                nextFragment = getSupportFragmentManager().findFragmentByTag(DB.SPEED_TAG);
                if (nextFragment == null) {
                    if(currentFragment.getTag().equals(DB.MAIN_TAG)) {
                        nextFragment = new SpeedMeterFragment();
                        fragmentTransaction.replace(R.id.container, nextFragment, DB.SPEED_TAG);
                        fragmentTransaction.addToBackStack(null);
                    }
                    else {
                        if(currentFragment.getTag().equals(DB.SPEED_TAG)) return;
                        else {
                            nextFragment = new SpeedMeterFragment();
                            fragmentTransaction.replace(R.id.container, nextFragment, DB.SPEED_TAG);
                        }
                    }
                } else {
                    if(currentFragment.getTag().equals(DB.MAIN_TAG)) {
                        fragmentTransaction.replace(R.id.container, nextFragment, DB.SPEED_TAG);
                        fragmentTransaction.addToBackStack(null);
                    } else {
                        fragmentTransaction.replace(R.id.container, nextFragment, DB.SPEED_TAG);
                    }
                }
                break;

            case DB.LED_FRAG:
                nextFragment = getSupportFragmentManager().findFragmentByTag(DB.LED_TAG);
                if (nextFragment == null) {
                    if(currentFragment.getTag().equals(DB.MAIN_TAG)) {
                        nextFragment = new ControlLedFragment();
                        fragmentTransaction.replace(R.id.container, nextFragment, DB.LED_TAG);
                        fragmentTransaction.addToBackStack(null);
                    }
                    else {
                        if(currentFragment.getTag().equals(DB.LED_TAG)) return;
                        else {
                            nextFragment = new ControlLedFragment();
                            fragmentTransaction.replace(R.id.container, nextFragment, DB.LED_TAG);
                        }
                    }
                } else {
                    if(currentFragment.getTag().equals(DB.MAIN_TAG)) {
                        fragmentTransaction.replace(R.id.container, nextFragment, DB.LED_TAG);
                        fragmentTransaction.addToBackStack(null);
                    } else {
                        fragmentTransaction.replace(R.id.container, nextFragment, DB.LED_TAG);
                    }
                }

                break;

            case DB.SENSOR_FRAG:
                nextFragment = getSupportFragmentManager().findFragmentByTag(DB.SENSOR_TAG);
                if (nextFragment == null) {
                    if(currentFragment.getTag().equals(DB.MAIN_TAG)) {
                        nextFragment = new ConnectSensorFragment();
                        fragmentTransaction.replace(R.id.container, nextFragment, DB.SENSOR_TAG);
                        fragmentTransaction.addToBackStack(null);
                    }
                    else {
                        if(currentFragment.getTag().equals(DB.SENSOR_TAG)) return;
                        else {
                            nextFragment = new ConnectSensorFragment();
                            fragmentTransaction.replace(R.id.container, nextFragment, DB.SENSOR_TAG);
                        }
                    }
                } else {
                    if(currentFragment.getTag().equals(DB.MAIN_TAG)) {
                        fragmentTransaction.replace(R.id.container, nextFragment, DB.SENSOR_TAG);
                        fragmentTransaction.addToBackStack(null);
                    } else {
                        fragmentTransaction.replace(R.id.container, nextFragment, DB.SENSOR_TAG);
                    }
                }

                break;

            default:
                clearStack();
                nextFragment = new MainFragment();
                fragmentTransaction.replace(R.id.container, nextFragment, DB.MAIN_TAG);
                break;
        }

        fragmentTransaction.commit();
    }

    /**
     * Fragment Stack 제거
     *
     * @author Duveen
     */
    private void clearStack() {
        final FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}
