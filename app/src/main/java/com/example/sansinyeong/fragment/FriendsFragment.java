package com.example.sansinyeong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sansinyeong.R;
import com.example.sansinyeong.adapter.PlanListAdapter;
import com.example.sansinyeong.adapter.UserListAdapter;
import com.example.sansinyeong.model.Plan;
import com.example.sansinyeong.model.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UserInfo> users;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Button btn_add;

    public FriendsFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_friends_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        users = new ArrayList<>(); // Plan 객체를 담을 ArrayList (어댑터 쪽으로)
        userListAdapter = new UserListAdapter(users);
        recyclerView.setAdapter(userListAdapter);
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause(){
        super.onPause();
    }
}
