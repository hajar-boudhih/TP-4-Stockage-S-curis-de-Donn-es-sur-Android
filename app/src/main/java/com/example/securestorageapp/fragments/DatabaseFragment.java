package com.example.securestorageapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.securestorageapp.R;
import com.example.securestorageapp.database.DatabaseManager;
import com.example.securestorageapp.database.Note;
import com.example.securestorageapp.database.User;

import java.util.List;

public class DatabaseFragment extends Fragment {

    private DatabaseManager databaseManager;
    private EditText etUsername, etEmail, etNoteTitle, etNoteContent;
    private TextView tvDbResults;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_database, container, false);

        databaseManager = new DatabaseManager(requireContext());

        etUsername = view.findViewById(R.id.etUsername);
        etEmail = view.findViewById(R.id.etEmail);
        etNoteTitle = view.findViewById(R.id.etNoteTitle);
        etNoteContent = view.findViewById(R.id.etNoteContent);
        tvDbResults = view.findViewById(R.id.tvDbResults);

        Button btnAddUser = view.findViewById(R.id.btnAddUser);
        Button btnAddNote = view.findViewById(R.id.btnAddNote);
        Button btnRefresh = view.findViewById(R.id.btnRefresh);

        btnAddUser.setOnClickListener(v -> addUser());
        btnAddNote.setOnClickListener(v -> addNote());
        btnRefresh.setOnClickListener(v -> refreshData());

        return view;
    }

    private void addUser() {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();

        if (username.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(username, email, "hashed_password_placeholder");
        databaseManager.insertUser(user, new DatabaseManager.DatabaseCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Utilisateur ajouté avec ID: " + result, Toast.LENGTH_SHORT).show();
                    refreshData();
                });
            }

            @Override
            public void onError(Exception e) {
                getActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void addNote() {
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Veuillez d'abord sélectionner ou ajouter un utilisateur", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = etNoteTitle.getText().toString();
        String content = etNoteContent.getText().toString();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = new Note(currentUser.getId(), title, content);
        databaseManager.insertNote(note, new DatabaseManager.DatabaseCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Note ajoutée avec ID: " + result, Toast.LENGTH_SHORT).show();
                    refreshData();
                });
            }

            @Override
            public void onError(Exception e) {
                getActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void refreshData() {
        databaseManager.getAllUsers(new DatabaseManager.DatabaseCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                StringBuilder sb = new StringBuilder("Utilisateurs et Notes:\n\n");
                if (users.isEmpty()) {
                    sb.append("Aucun utilisateur trouvé.");
                } else {
                    // Pour simplifier, on prend le dernier utilisateur comme utilisateur courant
                    currentUser = users.get(users.size() - 1);
                    for (User user : users) {
                        sb.append("U: ").append(user.getUsername()).append(" (").append(user.getEmail()).append(")\n");
                        // Ici on pourrait charger les notes pour chaque utilisateur, 
                        // mais pour la démo on va juste afficher l'utilisateur courant
                    }
                    
                    databaseManager.getNotesByUserId(currentUser.getId(), new DatabaseManager.DatabaseCallback<List<Note>>() {
                        @Override
                        public void onSuccess(List<Note> notes) {
                            sb.append("\nNotes de ").append(currentUser.getUsername()).append(":\n");
                            for (Note note : notes) {
                                sb.append("- ").append(note.getTitle()).append(": ").append(note.getContent()).append("\n");
                            }
                            getActivity().runOnUiThread(() -> tvDbResults.setText(sb.toString()));
                        }

                        @Override
                        public void onError(Exception e) {
                            getActivity().runOnUiThread(() -> tvDbResults.setText(sb.toString() + "\nErreur lors du chargement des notes."));
                        }
                    });
                }
                getActivity().runOnUiThread(() -> tvDbResults.setText(sb.toString()));
            }

            @Override
            public void onError(Exception e) {
                getActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}
