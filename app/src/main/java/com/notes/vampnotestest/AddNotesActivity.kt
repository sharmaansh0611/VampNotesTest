package com.notes.vampnotestest

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Timestamp

class AddNotesActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var saveNoteBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_notes)

        titleEditText = findViewById(R.id.notes_title_text)
        contentEditText = findViewById(R.id.content_text)
        saveNoteBtn = findViewById(R.id.save_button)

        saveNoteBtn.setOnClickListener { saveNote() }

    }

    private fun saveNote() {
        val noteTitle = titleEditText.text.toString()
        val noteContent = contentEditText.text.toString()

        if (noteTitle.isEmpty()) {
            titleEditText.error = "Title is required"
            return
        }

        //Making a model class here of data

        val note = Note(noteTitle, noteContent, Timestamp.now())
        saveNoteToFirebase(note)
    }

    fun saveNoteToFirebase(note: Note) {
        val documentReference = Utility.getCollectionReferenceForNotes().document()

        documentReference.set(note).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Note is added
                Toast.makeText(this, "Note added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed while adding note", Toast.LENGTH_SHORT).show()
            }
        }
    }
}