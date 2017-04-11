package tech.linard.miniolimpiadasditec.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tech.linard.miniolimpiadasditec.Group;
import tech.linard.miniolimpiadasditec.R;
import tech.linard.miniolimpiadasditec.User;

public class CadastroActivity extends AppCompatActivity
        implements View.OnClickListener , AdapterView.OnItemSelectedListener

{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase mDatabase;
    private DatabaseReference reference;

    private User mUser;
    private String mMatricula;
    private Group mGroup;

    private Button btnEntrar;
    private Button btnCadastrar;

    private TextView txtMatricula;
    private TextView txtSenha;

    private Spinner spinnerUnidades;

    private final String TAG = CadastroActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        btnCadastrar = (Button) findViewById(R.id.btn_cadastrar);
        btnCadastrar.setOnClickListener(this);
        btnEntrar = (Button) findViewById(R.id.btn_entrar);
        btnEntrar.setOnClickListener(this);
        spinnerUnidades = (Spinner) findViewById(R.id.spinner_unidades);
        spinnerUnidades.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.unidades_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerUnidades.setAdapter(adapter);

        txtMatricula = (TextView) findViewById(R.id.txt_matricula);
        txtSenha = (TextView) findViewById(R.id.txt_senha);
        mUser = new User();
        mGroup = new Group();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    mUser.setUID(user.getUid());
                    cadastraGrupos();
                    atualizaGrupos();
                    cadastraUsuario();
                    finish();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    private void atualizaGrupos() {
        mDatabase = FirebaseDatabase.getInstance();
        // Remove o usuário atual de todos os grupos
        for (int x = 0; x<=14; x++) {
            reference = mDatabase.getReference("groups");
            reference.child(String.valueOf(x))
                    .child("members")
                    .child(mUser.getMatricula()).setValue(false);
        }
        reference = mDatabase.getReference("groups");
        reference.child(String.valueOf(mGroup.getGroup_id()))
                .child("members")
                .child(mUser.getMatricula()).setValue(true);
    }

    private void cadastraGrupos() {
        String[] grupos = getResources().getStringArray(R.array.unidades_array);
        for (int x = 0; x<grupos.length; x++) {
            Group mGroup = new Group();
            mGroup.setGroup_id(x);
            mGroup.setName(grupos[x]);
            mDatabase = FirebaseDatabase.getInstance();
            reference = mDatabase.getReference("groups");
            reference.child(String.valueOf(x)).setValue(mGroup);
        }

    }

    private void cadastraUsuario() {
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference("users/" + mUser.getMatricula());
        reference.child("matricula").setValue(mUser.getMatricula());
        reference.child("email").setValue(mUser.getEmail());
        reference.child("uid").setValue(mUser.getUID());
        //
        for (int x=0; x<=14;x++) {
            reference.child("groups")
                    .child(String.valueOf(x))
                    .setValue(false);
        }
        reference.child("groups")
                 .child(String.valueOf(mGroup.getGroup_id()))
                 .setValue(true);

        reference = mDatabase.getReference("groups");
        reference.child(String.valueOf(mGroup.getGroup_id()))
                .child("members")
                .child(mUser.getMatricula()).setValue(true);
    }

    @Override
    public void onClick(View v) {
        mMatricula = String.valueOf(txtMatricula.getText()).toUpperCase();
        boolean matriculaValida = validarMatricula(mMatricula );
        String senha = String.valueOf(txtSenha.getText());
        boolean senhaValida = validarSenha(senha);
        String email = "";
        if (matriculaValida && senhaValida) {
            email = mMatricula  + "@bb.com.br";
            mUser.setMatricula(mMatricula);
            mUser.setEmail(email);
        }
        switch (v.getId()) {

            case R.id.btn_cadastrar:
                if (matriculaValida) {
                    if (senhaValida) {
                        signIn(email, senha);
                    } else {
                        Toast.makeText(this, "Digite uma senha com 6 ou mais dígitos.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Digite uma matrícula válida.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_entrar:
                if (matriculaValida) {
                    if (senhaValida) {
                        logIn(email, senha);
                    } else {
                        Toast.makeText(this, "Digite uma senha com 6 ou mais dígitos.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Digite uma matrícula válida.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private Boolean validarSenha(String senha) {
        boolean result = false;
        if (senha.length() > 5 && !senha.isEmpty()){
            result = true;
        }
        return  result;
    }


    private Boolean validarMatricula(String matricula) {
        boolean result = false;
        if (!matricula.isEmpty()) {
            if (matricula.substring(0,1).equalsIgnoreCase("F")) {
                String numeroMatricula = matricula.substring(1).trim();
                String regex = "\\d+";
                if (numeroMatricula.matches(regex)
                        && matricula.length() > 2
                        && matricula.length() < 9) {
                    result = true;
                }
            }
        }
        return result;
    }

    private void logIn(final String email, String senha) {
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(CadastroActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(String email, String senha) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(CadastroActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CadastroActivity.this, R.string.auth_success,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mGroup.setName(parent.getItemAtPosition(position).toString());
        mGroup.setGroup_id(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
