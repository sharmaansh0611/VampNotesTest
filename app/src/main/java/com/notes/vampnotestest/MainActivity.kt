package com.notes.vampnotestest

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {

    private lateinit var addNoteBtn : FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var menuBtn: ImageButton
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        addNoteBtn = findViewById(R.id.add_note_btn)
        recyclerView = findViewById(R.id.recycler_view)
        menuBtn = findViewById(R.id.menu_button)

        addNoteBtn.setOnClickListener{
            startActivity(Intent(this, AddNotesActivity::class.java))
        }
        menuBtn.setOnClickListener { showMenu() }
        setupRecyclerView()


    }

    private fun showMenu() {
        //TODO display menu
        val popupMenu = PopupMenu(this, menuBtn)
        popupMenu.menu.add("Logout")
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener { menuItem ->
            if (menuItem.title == "Logout") {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginPage::class.java))
                finish()
                true
            } else false
        }
    }

    private fun setupRecyclerView() {
        val query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING)
        val options = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query, Note::class.java).build()
        recyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(options, this)
        recyclerView.adapter = noteAdapter
    }

    override fun onStart() {
        super.onStart()
        noteAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        noteAdapter.stopListening()
    }

    override fun onResume() {
        super.onResume()
        noteAdapter.notifyDataSetChanged()
    }
}