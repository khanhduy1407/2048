package nkduy.game.G2048;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;

public class MenuGame extends AppCompatActivity {

    private MenuImageAdapter adapter;
    private RecyclerView rcy;
    private List<Integer> id = new ArrayList<>();
    private List<String> text = new ArrayList<>();
    private Button startgame ;
    private Button loadgame;

    private ImageView left;
    private ImageView right;
    int position;
    boolean pause = true;

    private void initdata() {
        id.add(R.drawable.test1);
        id.add(R.drawable.test2);
        id.add(R.drawable.test3);
        text.add("3x3");
        text.add("4x4");
        text.add("5x5");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(MenuGame.this, BackgroundSoundService.class);
        stopService(intent);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Intent svc=new Intent(this, BackgroundSoundService.class);
        //startService(svc);
        position= 0;
        setContentView(R.layout.activity_menu_game);
        initdata();
        rcy = (RecyclerView)this.findViewById(R.id.rcy);
        startgame = (Button)this.findViewById(R.id.startgame) ;
        loadgame = (Button)this.findViewById(R.id.loadgame) ;
        left = (ImageView)this.findViewById(R.id.left);
        right  = (ImageView)this.findViewById(R.id.right);

        left.setBackgroundResource(R.drawable.left);
        AnimationDrawable progressAnimation =(AnimationDrawable)left.getBackground();
        progressAnimation.start();

        right.setBackgroundResource(R.drawable.right);
        AnimationDrawable progressAnimation1 =(AnimationDrawable)right.getBackground();
        progressAnimation1.start();

        left.setVisibility(View.INVISIBLE);

        adapter = new MenuImageAdapter(this, id,text);

        LinearLayoutManager layoutManager = new
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rcy.setLayoutManager(layoutManager);
        rcy.setAdapter(adapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rcy);

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        if(settings.getInt("type",-1) == position) {
            loadgame.setEnabled(true);
        } else {
            loadgame.setEnabled(false);
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("hasState")) {
                loadgame.setEnabled(true);
            } else {
                loadgame.setEnabled(false);

            }
        }

        startgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause = false;
                Intent intent = new Intent(MenuGame.this ,MainActivity.class);
                intent.putExtra("type",position);
                intent.putExtra("new",0);
                startActivity(intent);
              //  finish();
            }
        });

        loadgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause = false;
                Intent intent = new Intent(MenuGame.this ,MainActivity.class);
                intent.putExtra("type",position);
                intent.putExtra("new",1);
                startActivity(intent);
                // finish();
            }
        });

        rcy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){

                    position = ((LinearLayoutManager)recyclerView.getLayoutManager())
                            .findFirstVisibleItemPosition();
                    if(settings.getInt("type",-1) == position) {
                        loadgame.setEnabled(true);
                    } else {
                        loadgame.setEnabled(false);
                    }

                    if(position ==0) {
                        left.setVisibility(View.INVISIBLE);

                    } else {
                        left.setVisibility(View.VISIBLE);
                    }

                    if(position ==2) {
                        right.setVisibility(View.INVISIBLE);
                    } else {
                        right.setVisibility(View.VISIBLE);
                    }
                  //  onPageChanged(position);
                }
            }
        });

        adapter.setClickListener(new MenuImageAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //
            }
        });

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
