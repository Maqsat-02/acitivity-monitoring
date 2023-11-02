package kz.iitu.edu.activity.monitoring.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
@AllArgsConstructor
public class FirebaseUserRepository {
    private final Firestore db;

    public Page<FirebaseUser> findAllPaginated(Pageable pageable) {
        // No need to order by a field to keep the order consistent, since:
        // "By default, a query retrieves all documents that satisfy the query in ascending order by document ID."
        // (See https://firebase.google.com/docs/firestore/query-data/order-limit-data)
        ApiFuture<QuerySnapshot> query = db.collection("users")
                .offset((int) pageable.getOffset())
                .limit(pageable.getPageSize())
                .get();
        List<FirebaseUser> userList = get(query).getDocuments().stream()
                .map(this::documentToUser)
                .toList();
        return new PageImpl<>(userList, pageable, getCountOfAllUsers());
    }

    public List<FirebaseUser> findAllChiefEditors() {
        ApiFuture<QuerySnapshot> query = db.collection("users")
                .where(Filter.arrayContains("role", Role.CHIEF_EDITOR.name()))
                .get();
        return get(query).getDocuments().stream()
                .map(this::documentToUser)
                .toList();
    }

    public Optional<FirebaseUser> findById(String id) {
        ApiFuture<DocumentSnapshot> query = db.collection("users")
                .document(id)
                .get();
        DocumentSnapshot document = get(query);
        if (!document.exists()) {
            return Optional.empty();
        }
        return Optional.of(documentToUser(document));
    }

    private long getCountOfAllUsers() {
        ApiFuture<AggregateQuerySnapshot> countQuery = db.collection("users")
                .count()
                .get();
        return get(countQuery).getCount();
    }

    private FirebaseUser documentToUser(DocumentSnapshot document) {
        return FirebaseUser.builder()
                .id(document.getId())
                .email(document.getString("email"))
                .role(getRole(document))
                .firstName(document.getString("first_name"))
                .lastName(document.getString("last_name"))
                .build();
    }

    private String getRole(DocumentSnapshot userDocument) {
        Object role = userDocument.get("role");
        if (role instanceof ArrayList<?> roles && !roles.isEmpty() && roles.get(0) instanceof String result) {
            return result;
        }
        throw new IllegalStateException("User document does not have a valid role");
    }

    private <T> T get(ApiFuture<T> query) {
        try {
            return query.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
