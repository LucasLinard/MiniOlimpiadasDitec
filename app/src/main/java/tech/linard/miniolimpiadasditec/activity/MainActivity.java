package tech.linard.miniolimpiadasditec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import tech.linard.miniolimpiadasditec.Modalidade;
import tech.linard.miniolimpiadasditec.R;
import tech.linard.miniolimpiadasditec.SubModalidade;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mUser;
    // Realtime Database
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private ChildEventListener mChildEventListener;

    private ListView mListModalidades;
    private ModalidadeAdapter mModalidadeAdapter;
    private List<Modalidade> listModalidades;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Esportes");
        setSupportActionBar(toolbar);


        listModalidades = new ArrayList<>();
        mListModalidades = (ListView) findViewById(R.id.list_modalidades);
        mModalidadeAdapter = new ModalidadeAdapter(this,
                                                    R.layout.modalidades_list_item,
                                                    listModalidades);
        mListModalidades.setAdapter(mModalidadeAdapter);
        mListModalidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Modalidade currentModalidade = (Modalidade) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ModalidadeDetailActivity.class);
                intent.putExtra("modalidade", currentModalidade.getModalidadeId());
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());
                    Toast.makeText(MainActivity.this, "LOGADO!", Toast.LENGTH_SHORT).show();

                } else {
                    // User is signed out Start LOGIN ACTIVITY
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(MainActivity.this, "SAIU!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
                }
                // ...
            }
        };
        // listener para receber a lista de modalidades
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("modalidades");
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Modalidade currentModalidade = dataSnapshot.getValue(Modalidade.class);
                mModalidadeAdapter.add(currentModalidade);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mModalidadeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mModalidadeAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        mReference.addChildEventListener(mChildEventListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        mReference.removeEventListener(mChildEventListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                cadastraModalidades();
                break;
            case R.id.action_logoff:
                logoff();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cadastraModalidades() {
        String[] modalidades = getResources().getStringArray(R.array.modalidades_array);
        for (int x = 0; x<modalidades.length; x++) {
            Modalidade mModalidade = new Modalidade();
            mModalidade.setModalidadeId(x);
            mModalidade.setName(modalidades[x]);
            mDatabase = FirebaseDatabase.getInstance();
            mReference = mDatabase.getReference("modalidades");
            mReference.child(String.valueOf(x)).setValue(mModalidade);
        }
//        cadastraSubModalidades();

    }

    private void cadastraSubModalidades() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("submodalidades");
        String[] submodalidades = getResources().getStringArray(R.array.submodalidades_array);
        for (int x=0; x<submodalidades.length;x++) {
            mDatabase = FirebaseDatabase.getInstance();
            mReference = mDatabase.getReference("submodalidades");
            SubModalidade currentSubModalidade = new SubModalidade();
            currentSubModalidade.setIdSubModalidade(x);
            currentSubModalidade.setDescricao(submodalidades[x]);
            mReference.child(String.valueOf(x)).setValue(currentSubModalidade);
        }

    }

    private void logoff() {

        mAuth.signOut();
    }

}
