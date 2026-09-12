package br.edu.unisenai.rangonaregua.data;

import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import br.edu.unisenai.rangonaregua.model.Lugar;

public class LugarRepository {

    public Task<DocumentReference> inserir (Lugar item){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection("lugares").add(item);
    }

    public ListenerRegistration lerRealTime (EventListener<QuerySnapshot> callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection("lugares").orderBy("votos", Query.Direction.DESCENDING).addSnapshotListener(callback);
    }

    public Task<Void> votar (Lugar item){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("lugares").document(item.getId()).update("votos", FieldValue.increment(1));
    }

    public Task<Void> excluir (Lugar item){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("lugares").document(item.getId()).delete();
    }

    public Task<Void> restaurar (Lugar item){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("lugares").document(item.getId()).set(item);
    }
}
