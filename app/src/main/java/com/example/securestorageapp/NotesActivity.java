package com.example.securestorageapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securestorageapp.adapters.NoteAdapter;
import com.example.securestorageapp.database.DatabaseManager;
import com.example.securestorageapp.database.Note;
import com.example.securestorageapp.security.EncryptedFileManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesActivity extends AppCompatActivity {
    
    private EditText etNoteTitle;
    private EditText etNoteContent;
    private Button btnSaveNote;
    private RecyclerView rvNotes;
    private TextView tvUsername;
    
    private DatabaseManager databaseManager;
    private EncryptedFileManager encryptedFileManager;
    private NoteAdapter noteAdapter;
    private List<Note> notesList;
    
    private int userId;
    private String username;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        
        // Récupération des données de l'intent
        userId = getIntent().getIntExtra("USER_ID", -1);
        username = getIntent().getStringExtra("USERNAME");
        
        if (userId == -1 || username == null) {
            Toast.makeText(this, "Erreur: utilisateur non identifié", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Initialisation des gestionnaires
        databaseManager = new DatabaseManager(this);
        encryptedFileManager = new EncryptedFileManager(this);
        
        // Initialisation des vues
        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteContent = findViewById(R.id.etNoteContent);
        btnSaveNote = findViewById(R.id.btnSaveNote);
        rvNotes = findViewById(R.id.rvNotes);
        tvUsername = findViewById(R.id.tvUsername);
        
        // Configuration du RecyclerView
        notesList = new ArrayList<>();
        noteAdapter = new NoteAdapter(notesList);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setAdapter(noteAdapter);
        
        // Affichage du nom d'utilisateur
        tvUsername.setText("Notes de " + username);
        
        // Configuration des écouteurs
        btnSaveNote.setOnClickListener(v -> saveNote());
        
        // Chargement des notes
        loadNotes();
    }
    
    private void saveNote() {
        String title = etNoteTitle.getText().toString();
        String content = etNoteContent.getText().toString();
        
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Création de la note
        Note note = new Note(userId, title, content);
        
        // Insertion de la note dans la base de données
        databaseManager.insertNote(note, new DatabaseManager.DatabaseCallback<Long>() {
            @Override
            public void onSuccess(Long noteId) {
                // Journalisation de l'action dans un fichier chiffré
                logAction("Note créée: " + title);
                
                runOnUiThread(() -> {
                    // Mise à jour de l'interface
                    note.setId(noteId.intValue());
                    notesList.add(0, note);
                    noteAdapter.notifyItemInserted(0);
                    rvNotes.scrollToPosition(0);
                    
                    // Effacement des champs
                    etNoteTitle.setText("");
                    etNoteContent.setText("");
                    
                    Toast.makeText(NotesActivity.this, "Note enregistrée avec succès", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> Toast.makeText(NotesActivity.this, "Erreur lors de l'enregistrement de la note: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
    
    private void loadNotes() {
        databaseManager.getNotesByUserId(userId, new DatabaseManager.DatabaseCallback<List<Note>>() {
            @Override
            public void onSuccess(List<Note> notes) {
                runOnUiThread(() -> {
                    notesList.clear();
                    notesList.addAll(notes);
                    noteAdapter.notifyDataSetChanged();
                    
                    // Journalisation de l'action dans un fichier chiffré
                    logAction("Notes chargées: " + notes.size() + " notes");
                });
            }
            
            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> Toast.makeText(NotesActivity.this, "Erreur lors du chargement des notes: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
    
    private void logAction(String action) {
        // Création du message de log
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());
        String logMessage = timestamp + " - " + username + " - " + action + "\n";
        
        // Nom du fichier de log
        String logFilename = "user_" + userId + "_log.txt";
        
        // Lecture du contenu existant
        String existingLog = encryptedFileManager.readFromEncryptedFile(logFilename);
        if (existingLog == null) {
            existingLog = "";
        }
        
        // Ajout du nouveau message
        String newLog = logMessage + existingLog;
        
        // Écriture dans le fichier chiffré
        encryptedFileManager.writeToEncryptedFile(logFilename, newLog);
    }
}
