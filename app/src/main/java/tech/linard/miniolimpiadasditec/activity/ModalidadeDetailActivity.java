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
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import tech.linard.miniolimpiadasditec.Modalidade;
import tech.linard.miniolimpiadasditec.R;
import tech.linard.miniolimpiadasditec.SubModalidade;

public class ModalidadeDetailActivity extends AppCompatActivity {

    private static final String TAG = ModalidadeDetailActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mUser;

    // Realtime Database
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private Query mQuery;

    private int mModalidadeId;
    private SubModalidade[] subModalidades;

    private List<SubModalidade> listsubmodalidades;

    private ListView mListSubModalidades;
    private SubModalidadeAdapter mSubModalidadeAdapter;

    private ChildEventListener mChildEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modalidade_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Modalidades");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());
                } else {
                    // User is signed out Start LOGIN ACTIVITY
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
                }
                // ...
            }
        };
        Intent intent  = getIntent();
        mDatabase = FirebaseDatabase.getInstance();
        mModalidadeId = intent.getIntExtra("modalidade", 99);
        Log.d(TAG, String.valueOf(mModalidadeId));

        listsubmodalidades = new ArrayList<>();
        mListSubModalidades = (ListView) findViewById(R.id.list_submodalidades);
        mSubModalidadeAdapter = new SubModalidadeAdapter(this
                , R.layout.submodalidades_list_item
                ,listsubmodalidades);
        mListSubModalidades.setAdapter(mSubModalidadeAdapter);
        mListSubModalidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubModalidade currentModalidade = (SubModalidade) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ListaEquipes.class);
                intent.putExtra("modalidade", currentModalidade.getIdModalidade());
                intent.putExtra("submodalidade", currentModalidade.getIdSubModalidade());
                startActivity(intent);
            }
        });
        mReference = mDatabase.getReference();
        if (mModalidadeId != 99) {
            mQuery = mReference.child("submodalidades").orderByChild("idModalidade").equalTo(mModalidadeId);
            Log.d(TAG, "entou no busca modalidades");
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    SubModalidade currentSubModalidade = dataSnapshot.getValue(SubModalidade.class);
                    mSubModalidadeAdapter.add(currentSubModalidade);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSubModalidadeAdapter.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQuery.addChildEventListener(mChildEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mQuery.removeEventListener(mChildEventListener);
    }
}
