package tech.linard.miniolimpiadasditec.activity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.linard.miniolimpiadasditec.Equipe;
import tech.linard.miniolimpiadasditec.Modalidade;
import tech.linard.miniolimpiadasditec.R;
import tech.linard.miniolimpiadasditec.SubModalidade;

public class ListaEquipes extends AppCompatActivity {

    private static final String TAG = ListaEquipes.class.getSimpleName()
            ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mUser;
    // Realtime Database
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private Query mQuery;


    private ChildEventListener mChildEventListener;

    private ListView mListViewEquipes;
    private EquipeAdapter mEquipeAdapter;
    private List<Equipe> mListEquipes;

    private int modalidade;
    private int submodalidade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_equipes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Equipes");

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Inscrição", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                criaNovaEquipe(modalidade, submodalidade);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        modalidade = intent.getIntExtra("modalidade", 99);
        submodalidade = intent.getIntExtra("submodalidade", 99);

        // Firebase AUTH.
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
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Equipe currentEquipe = dataSnapshot.getValue(Equipe.class);
                Log.d(TAG, currentEquipe.toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }

            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        };

        mQuery = mReference.child("equipes").orderByChild("submodalidadeId").equalTo(submodalidade);

    }

    private void criaNovaEquipe(int modalidade, int submodalidade) {
        Equipe currentEquipe = new Equipe();
        DatabaseReference baseReference = mDatabase.getReference("equipes").push();
        currentEquipe.setUID(baseReference.toString());
        currentEquipe.setModalidadeId(modalidade);
        currentEquipe.setSubmodalidadeId(submodalidade);
        Map<String,Boolean> participantes = new HashMap<String,Boolean>(20);
        participantes.put(mUser.getUid(),true);
        currentEquipe.setParticipantes(participantes);
        baseReference.setValue(currentEquipe);
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
